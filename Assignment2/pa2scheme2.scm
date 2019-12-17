(define (dbl_atm atom list1)
  (cond ((not (null? list1))
    (cond ((list? list1)
      (cond ((list? (car list1))
        (list (append (dbl_atm atom (car list1)) (dbl_atm atom (cdr list1)))))
        (else
          (append (dbl_atm atom (car list1)) (dbl_atm atom (cdr list1))))))
      (else 
        (cond ((eq? atom list1)
          (append (list atom) (list atom)))
          (else 
            (list list1))))))
    (else '())))

(print (dbl_atm 'a '(a (b c a (a d)))))
(print (dbl_atm 'c '(a (b c a (a d)))))