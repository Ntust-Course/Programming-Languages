
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;

class BankingException extends Exception {
    BankingException() {
        super();
    }

    BankingException(String name) {
        super(name);
    }
}

interface BasicAccount {
    String name();

    double balance();
}

interface WithdrawableAccount extends BasicAccount {
    double withdraw(double amount) throws BankingException;
}

interface DepositableAccount extends BasicAccount {
    double deposit(double amount) throws BankingException;
}

interface InterestableAccount extends BasicAccount {
    double computeInterest() throws BankingException;
}

interface FullFunctionalAccount extends WithdrawableAccount, DepositableAccount, InterestableAccount {
}

public abstract class Account {

    // protected variables to store commom attributes for every bank accounts
    protected String accountName;
    protected double accountBalance;
    protected double accountInterestRate;
    protected Date openDate;
    protected Date lastInterestDate;

    // constructor for every bank accounts
    public Account(String name, double firstDeposit) {
        this(name, firstDeposit, new Date());
    }

    public Account(String name, double firstDeposit, Date firstDate) {
        accountName = name;
        accountBalance = firstDeposit;
        openDate = firstDate;
        lastInterestDate = openDate;
        accountInterestRate = 0.12;
    }

    // public methods for every bank accounts
    public String name() {
        return accountName;
    }

    public double balance() {
        return accountBalance;
    }

    public double deposit(double amount) throws BankingException {
        accountBalance += amount;
        return accountBalance;
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        accountBalance -= amount;
        return accountBalance;
    }

    public double withdraw(double amount) throws BankingException {
        Date withdrawDate = new Date();
        return withdraw(amount, withdrawDate);
    }

    abstract double computeInterest(Date interestDate) throws BankingException;

    public double computeInterest() throws BankingException {
        Date interestDate = new Date();
        return computeInterest(interestDate);
    }
}

/**
 * Derived class: CheckingAccount
 *
 * Description: Interest is computed daily; there's no fee for withdraw; there
 * is a minimum balance of $1000.
 */
class CheckingAccount extends Account implements FullFunctionalAccount {

    CheckingAccount(String name, double firstDeposit) {
        super(name, firstDeposit);
    }

    CheckingAccount(String name, double firstDeposit, Date firstDate) {
        super(name, firstDeposit, firstDate);
        System.out.println("accountBalance: " + accountBalance);
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // minimum balance is 1000, raise exception if violated
        if ((accountBalance - amount) < 1000) {
            throw new BankingException("Underfraft from checking account name: " + accountName);
        }
        return super.withdraw(amount, withdrawDate);
    }

    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name: " + accountName);
        }

        int numberOfDays = (int) ((interestDate.getTime() - lastInterestDate.getTime()) / Time.day);
        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 * accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return accountBalance;
    }
}

/**
 * Derived class: SavingAccount
 *
 * Description: fee of $1 for every transaction, except the first three per
 * month are free; no minimum balance.
 * 
 * @author SheiUn
 */
class SavingAccount extends Account implements FullFunctionalAccount {

    private int transactionCount = 0;
    private Date lastTransactionDate = new Date();
    private int transactionFee = 1;

    /**
     * @param name         accountName
     * @param firstDeposit amount of first deposit when account opening
     * @param firstDate    first open date of this account
     */
    SavingAccount(String name, double firstDeposit) {
        super(name, firstDeposit);
    }

    SavingAccount(String name, double firstDeposit, Date firstDate) {
        super(name, firstDeposit, firstDate);
    }

    /**
     * no minimum balance.
     */
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        processTransaction(withdrawDate);
        return super.withdraw(amount, withdrawDate);
    }

    public double deposit(double amount) throws BankingException {
        processTransaction();
        return super.deposit(amount);
    }

    private void processTransaction() {
        processTransaction(new Date());
    }

    private void processTransaction(Date transactionDate) {
        // if today is a new month from last transaction date reset count to 0
        if (isNewMonth(transactionDate)) {
            transactionCount = 0;
        }
        lastTransactionDate = transactionDate;
        transactionCount++;

        // the first three per month are free
        if (transactionCount > 3)
            // fee of $1 for every transaction
            accountBalance -= transactionFee;
    }

    public double computeInterest(Date interestDate) throws BankingException {
        // monthly interest
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" + accountName);
        }

        int numberOfMonths = (int) ((interestDate.getTime() - lastInterestDate.getTime()) / Time.month);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        double interestEarned = (double) numberOfMonths / 12.0 * accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return accountBalance;
    }

    /**
     * Check Year and Month is the same in current date and last transaction date.
     */
    private boolean isNewMonth(Date transactionDate) {
        Calendar currentTransactionCal = Calendar.getInstance();
        currentTransactionCal.setTime(transactionDate);
        Calendar lastTransactionCal = Calendar.getInstance();
        lastTransactionCal.setTime(lastTransactionDate);
        return lastTransactionCal.get(Calendar.YEAR) != currentTransactionCal.get(Calendar.YEAR)
                || lastTransactionCal.get(Calendar.MONTH) != currentTransactionCal.get(Calendar.MONTH);
    }
}

/**
 * Derived class: CDAccount
 *
 * Description: monthly interest; fixed amount and duration (e.g., you can open
 * 1 12-month CD for $5000; for the next 12 months you can't deposit anything
 * and withdrawals cost a $250 fee); at the end of the duration the interest
 * payments stop and you can withdraw w/o fee.
 */
class CDAccount extends Account implements FullFunctionalAccount {

    /**
     * 12 months by default, save as long just convenient calc with long time
     */
    private long duration = 12 * Time.month;

    /**
     * Constructor of CDAccount another params are same as Account
     * 
     * @param month duration of interest 12 months by default
     */
    CDAccount(String name, double firstDeposit) {
        super(name, firstDeposit, new Date());
    }

    CDAccount(String name, double firstDeposit, Date firstDate) {
        super(name, firstDeposit, firstDate);
    }

    CDAccount(String name, double firstDeposit, Date firstDate, int month) {
        super(name, firstDeposit, firstDate);
        this.duration = month * Time.month;
    }

    /**
     * withdraw cost $250 fee before duration
     */
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        if (!afterDuration()) {
            System.out.println("NOTICE: withdraw cost $250 fee before duration!");
            accountBalance -= 250;
        }
        return super.withdraw(amount, withdrawDate);
    }

    public double deposit(double amount) throws BankingException {
        // can't deposit before duration end
        if (!afterDuration()) {
            throw new BankingException("Can't deposit during interest.");
        }
        return super.deposit(amount);
    }

    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name: " + accountName);
        }

        if (!afterDuration()) {
            int numberOfMonths = (int) ((interestDate.getTime() - lastInterestDate.getTime()) / Time.month);
            System.out.println("Number of months since last interest is " + numberOfMonths);
            double interestEarned = (double) numberOfMonths / 12.0 * accountInterestRate * accountBalance;
            System.out.println("Interest earned is " + interestEarned);
            lastInterestDate = interestDate;
            accountBalance += interestEarned;
        }
        return accountBalance;
    }

    public boolean afterDuration() {
        return new Date().after(new Date(openDate.getTime() + duration));
    }
}

/**
 * Derived class: LoanAccount
 *
 * Description: like a saving account, but the balance is "negative" (you owe
 * the bank money, so a deposit will reduce the amount of the loan); you can't
 * withdraw (i.e., loan more money) but of course you can deposit (i.e., pay off
 * part of the loan).
 * 
 * @param computeInterest same as SavingAccount
 */
class LoanAccount extends SavingAccount {

    /**
     * TODO: throw Exception on firstDeposit greatEqual 0 <br />
     * if (firstDeposit >= 0) { throw new BankingException("Balance is always
     * negative: " + firstDeposit); }
     */
    LoanAccount(String name, double firstDeposit) {
        super(name, firstDeposit);
    }

    LoanAccount(String name, double firstDeposit, Date firstDate) {
        super(name, firstDeposit, firstDate);
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        throw new BankingException("LoanAccount can't withdraw.");
    }

    public double deposit(double amount) throws BankingException {
        // deposit will reduce the amount of the loan
        accountBalance -= amount;
        return accountBalance;
    }
}

/**
 * Use to calculate common time units.
 * 
 * @author SheiUn
 * @param day   milliseconds of a day
 * @param month milliseconds of a month
 * @param year  milliseconds of a year
 */
class Time {
    final static long day = 30 * 24 * 60 * 60 * 1000L;
    final static long month = 30 * day;
    final static long year = 12 * month;
}