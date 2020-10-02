Programming with the APR - Using MD5
####################################

:date: 2010-11-02 11:00:34 +0100
:tags: APR, Apache, C
:category: Programming
:summary: This article will show you how to use the OpenID delegation feature which allows you to use your own domain as an identifier for OpenID.

This article intends to show how to basically use the MD5 hashing
algorithm that ships with the Apache Portable Runtime (APR) library.

Introduction
************

Sometimes you are really in need for some MD5 hashed strings. Most of
the time you are in such a need if you want to create a custom
password store where you don't want an attacker to easily get
the password if he compromises your system. So MD5 hashed password are
great for such a purpose. Luckily the Apache Portable Runtime (APR)
provides such a MD5 hashing mechanism to you, so you can easily use
that and do not have to struggle with the complex details of how to
actually create a MD5 hash.

Hashing
=======

Hashing basically describes a way of taking an arbitrary length input
and calculating that down into a secure, tamperproof cryptographic
value that can only be recalculated if the same input was given
again. The basic idea is that the calculation that is necessary to
produce the hashed output is very easy to perform, but if you just
have the hashed version of the password you cannot easily reverse that
operation (the reverse operation is very complex).

Salting
=======

If you are creating a password store on your own, you probably don't
want a simple MD5 hashing mechanism, which the APR provides to
you. You probably want to add some salt to your password management
mechanism, since salting the password makes brute force attacks with
rainbow tables onto the hashed password repository so much more
inconvenient for the attacker.

Salting, for those who don't know, simply sort of "enriches" your
password with some random bytes, that will be thrown into the mix when
hashing the actual password. Since a hash algorithm is only a good one
if it flips about 50 percent of the bits in the output if you change a
single input bit, you can imagine that a few added bits should
absolutely be sufficient to change the result of the hash operation.

In order for the algorithm to be able to reconstruct the same hash
again, he of course needs to know the salt that was initially used to
create the corresponding hash. Therefore this salt value will be
written in front of the final hashed password string.

A typical MD5 hash, or sometimes called an MD5 digest will look like::

   $apr1$XGOEPMa5$eUAF1NzTmHoqGZJSD5P4q1


The c0de
********

The code itself is pretty straight forward. You might notice that I'm
not using any APR data pool within the program itself. This is simply
due to the fact that I'm not needing any ;).

The program first initializes internal APR data structures and
registers a termination function. Then a randomized salt value is
generated (you could also use a fixed salt, but that is not
recommended). Finally the MD5 encoding is performed. The most
problematic part here is to figure out what the actual length of the
result will be. From what I observed the result is no longer than 37
characters, but that might change if the APR people e.g. decide to
change their :code:`$apr1$` prefix which indicates the custom salting
algorithm to the library.
