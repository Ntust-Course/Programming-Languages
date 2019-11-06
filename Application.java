
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;

public class Application {

    public static void main(String args[]) {
        Account a;
        Date d;
        double ret;

        a = new CheckingAccount("John Smith", 1500.0);

        try {
            ret = a.withdraw(100.00);
            System.out.println("Account <" + a.name() + "> now has $" + ret + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());
        }

        a = new CheckingAccount("John Smith", 1500.0);
        try {
            ret = a.withdraw(600.00);
            System.out.println("Account <" + a.name() + "> now has $" + ret + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());
        }

        a = new SavingAccount("Pie", 1500.0);
        try {
            ret = a.withdraw(600.00);
            System.out.println("Account <" + a.name() + "> now has $" + ret + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());
        }

        try {
            a = new LoanAccount("Oreo", -1500.0);
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());
        }

        try {
            a.withdraw(1500);
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());
        }
        System.out.println(a.balance());

        /* put your own tests here ....... */
        /*
         * if your implementaion is correct, you can do the following with polymorphic
         * array accountList
         */
        Account[] accountList = new Account[4];

        // buid 4 different accounts in the same array
        accountList[0] = new CheckingAccount("John Smith", 1500.0);
        accountList[1] = new SavingAccount("William Hurt", 1200.0);
        accountList[2] = new CDAccount("Woody Allison", 1000.0);
        accountList[3] = new LoanAccount("Judi Foster", -1500.0);

        // compute interest for all accounts
        for (int count = 0; count < accountList.length; count++) {
            double newBalance = accountList[count].computeInterest();
            System.out.println("Account <" + a.name() + "> now has $" + newBalance + " balance\n");
        }
    }

    static void stdExceptionPrinting(Exception e, double balance) {
        System.out.println("EXCEPTION: Banking system throws a " + e.getClass() + " with message: \n\t" + "MESSAGE: "
                + e.getMessage());
        System.out.println("\tAccount balance remains $" + balance + "\n");
    }
}