mother(X, Y) :- female(X), parent(X, Y).
father(X, Y) :- male(X), parent(X, Y).
daughter(X, Y) :- female(X), parent(Y, X).
son(X, Y) :- male(X), parent(Y, X).
child(X, Y) :- parent(Y, X).
sibling(X, Y) :- parent(Z, X), parent(Z, Y).
sister(X, Y) :- female(X), sibling(X, Y).
brother(X, Y) :- male(X), sibling(X, Y).
uncle(X, Y) :- parent(Z, Y), brother(X, Z).
aunt(X, Y) :- parent(Z, Y), sister(X, Z).
cousin(X, Y) :- parent(Z, Y), parent(W, X), sibling(W, Z).

male(zues).
male(alan).
male(mark).
male(mel).
male(richard).

female(bitsy).
female(fran).
female(amy).
female(janny).
female(joan).
female(rosa).

parent(amy, janny).
parent(amy, richard).
parent(amy, joan).
parent(janny, bitsy).
parent(janny, alan).
parent(joan, fran).
parent(mark, janny).
parent(mark, richard).
parent(mel, joan).
parent(richard, zues).
parent(richard, rosa).
