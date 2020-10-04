Programming with the APR - Using APR HashTables
###############################################

:date: 2010-11-02 12:00:34 +0100
:tags: APR, Apache, C
:category: Programming
:author: Jens Frey
:summary: This article will show you how to use the OpenID delegation feature which allows you to use your own domain as an identifier for OpenID.

This short article will show how to use the Apache Portable Runtime
(APR) with it's built in hashtable.

Introduction
************

If you are in need to place data in a memory structure to access it
quickly, you are probably best with a hash table data type. The Apache
Portable Runtime (APR) luckily, along some others like
e.g. :code:`apr_table_t` or :code:`apr_array_header_t` (both defined
in :code:`apr_tables.h`), provides such a data type to you.

Hashtables do have the big advantage that you can supply them any data type you like. The next big thing with hashtables is that they are usually very efficient if the number of elements that they are holding
grows.

You can download the file `here <https://raw.githubusercontent.com/authsec/examples/master/c/aprHashtable.c>`_.

Let's hash
**********

In order to demonstrate hashtables we are using a very simple data structure which holds a date, a username and a password. We then set some values onto this data structure and save the created structure into the hashtable.

After storing that object into the hashtable we are reading the objects back from the hashtable and output it's contents.

The c0de
********

Now that you do have some overview of what we are actually doing here, it's time to show you some code. I think it pretty much speaks for itself.

You should however, if you are using strings as keys, use the special :code:`APR_HASH_KEY_STRING` value to indicate a string valued key to APR. This will use :code:`strlen(key)` to compute the length (:code:`NUL` terminator is not included there).

.. include:: examples/c/aprHashtable.c
    :code: c
