(define (palindrome list1)
  (append list1 (reverse list1)))

(define (reverse list1)
  (cond (
    (= 1 (length list1)) list1)
    (else (append (reverse (cdr list1)) (list (car list1))))))

(print (palindrome '(1 2 3)))
(print (palindrome '(a b c)))
