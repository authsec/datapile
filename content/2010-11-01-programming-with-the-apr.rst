Programming with the APR
#################################################

:date: 2010-11-01 10:00:34 +0100
:tags: APR, Apache, C
:category: Programming
:author: Jens Frey
:summary: This article will give you an introduction of how to program with the Apache Portable Runtime (APR).

This article will give you an introduction of how to program with the
Apache Portable Runtime (APR). It illustrates a simple command line
program.

.. note:: The program supplied should actually use :code:`apr_app_initialize(&argc, &argv, NULL);` since :code:`apr_initialize()` is intended for library use only.

I was playing around with the Apache Portable Runtime (APR) recently and found out, probably the most difficult part was to find out how to compile the program you have just written. The probably most famous projects using APR are the Apache HTTPd and Subversion.

Overview
********

APR's goal is to provide a platform independent API that provides a consistent interface to the platform specific implementation. The APR code itself is pretty good documented. But i wouldn't say you'd find plenty of resources on the web. Especially a simple example on how to program with the APR was missing for me.

Example program
***************

I put together an example program which shows how to use the APR. This includes instructions on how to get the program compiled after you have written it. This seems to be so self-evident to people, that no one seems to write that up. The heart of this process lies in the usage of the :code:`apr-config`, or sometimes called :code:`apr-1-config` utility.

Preconditions
=============

Make sure you have the APR development files installed. Since i am mostly working with Debian based distributions, like Debian itself or Ubuntu, i install the libraries with my package management system. Of course make sure you install the "-dev" versions of APR. For me that have been the packages

*   libapr1
*   libapr1-dbg
*   libapr1-dev
*   libaprutil1
*   libaprutil1-dbg
*   libaprutil1-dev

You can get those packages by issuing the following command on the command line (this may of course vary if you are not using a Debian based distribution or if you install from source)::

   $ sudo apt-get install libapr1 libapr1-dbg libapr1-dev libaprutil1 libaprutil1-dbg libaprutil1-dev


Program code
============

The program itself is obviously a pretty easy one, it basically allocates resources from a memory pool managed through APR onto a struct and later simply prints the allocated values. The programs code is as follows.

Compile the program
===================

Now there comes the next crucial step in getting your program to fly. To do so you probably best export a variable as suggested by the :code:`apr-1-config` tool. Then you can go on an compile your program the "normal" way you'd do that. If you do not want debug symbols compiled into your code, you of course would remove the :code:`-g` option in front of the :code:`APR_LIBS` variable::

   $> export APR_LIBS="`apr-1-config --cflags --cppflags --includes --ldflags --link-ld --libs`"
   $> gcc -g simple_apr.c -o simple_apr $APR_LIBS

By executing the program your output should now look something like that::

   $ ./simple_apr
   Username: Jens Frey
   Password: secret
   Time: 1231102877630911
   Time readable: Sun, 04 Jan 2009 21:01:17 GMT

I hope you achieved similar results.

References
**********

* `The APR homepage <http://apr.apache.org/>`_
* `The APR API documentation <http://apr.apache.org/docs/apr/trunk/>`_
* `Using APR Pools <http://svn.apache.org/viewvc/apr/apr/trunk/docs/pool-design.html?view=co>`_
* `Writing Portable C Code with APR <http://people.apache.org/~rooneg/talks/portable-c-with-apr/apr.html>`_ 
