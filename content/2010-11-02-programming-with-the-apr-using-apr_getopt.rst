Programming with the APR - Using apr_getopt
###############################################

:date: 2010-11-02 10:00:34 +0100
:tags: APR, Apache, C
:category: Programming
:authors: Jens Frey
:summary: This article will show you how to use the built in command line parser that comes with the APR.

This article will show you how to use the built in command line parser that comes with the APR. There will be two source files, one showing you how to use :code:`apr_getopt` just to parse simple, single options and the other peace of source will show you how to use it with so called long options. 

You can download the `single option parser here <https://raw.githubusercontent.com/authsec/examples/master/c/aprGetopt.c>`_ and the `long options parser here <https://raw.githubusercontent.com/authsec/examples/master/c/aprGetoptLong.c>`_.

.. note:: The program supplied should actually use :code:`apr_app_initialize(&argc, &argv, NULL);` since :code:`apr_initialize()` is intended for library use only.

Prerequisites
*************

As always, you need to have APR libraries installed in order to be able to use them. You'll additionally need the header files too, so if you're installing from some distribution specific mechanism, be sure to have the "-dev" packages installed as well.

Explanation
***********

There is really not that much to explain this time. We first initialize APR, create a pool, then initialize the :code:`getopt` parser and then we're walking through the options specified on the command line. 

The most notable part here is the line: :code:`cmdLineArgs->interleave= 1;`. Setting the interleave to one allows us to have options and final arguments specified "wildly" on the command line. That means that the order of arguments is not relevant. You can either supply the options like :code:`./aprGetoptLong --choose what -h lalalal` or you can type: :code:`./aprGetoptLong --choose what lalala -h` which, in this case would lead to the same result.

Simple options code
*******************

.. include:: ../examples/c/aprGetopt.c
    :code: c

Now that you have seen how it works with simple options, long options is no more magic. You just have to additionally initialize an array basically, but see for yourself.

Long options code
*****************

.. include:: ../examples/c/aprGetoptLong.c
    :code: c

Hope you enjoyed it.

Downloads
  * `aprGetopt.c <https://raw.githubusercontent.com/authsec/examples/master/c/aprGetopt.c>`_
  * `aprGetoptLong.c <https://raw.githubusercontent.com/authsec/examples/master/c/aprGetoptLong.c>`_
