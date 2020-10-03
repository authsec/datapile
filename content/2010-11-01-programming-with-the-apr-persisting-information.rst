Programming with the APR - Persisting information
#################################################

:date: 2010-11-01 11:00:34 +0100
:tags: APR, Apache, C
:category: Programming
:author: Jens Frey
:summary: This article will show you how to store information in the database that comes with the Apache Portable Runtime (APR).

This article will show you how to store information in the database that comes with the Apache Portable Runtime (APR). Basically a simple struct data type is persisted into that database. You can download the code `here <https://raw.githubusercontent.com/authsec/examples/master/c/aprWithSdbm.c>`_ .

.. note:: The program supplied should actually use :code:`apr_app_initialize(&argc, &argv, NULL);` since :code:`apr_initialize()` is intended for library use only.

Function description
********************

In order to store the information in that database you probably might want to create some functions to better break down work into smaller parts. Since the database mechanism requires you to have information available as an :code:`apr_datum_t` type, you probably want to introduce some helper functions which exactly deal with that issue. Converting your normal data types into a :code:`apr_datum_t`.

Helper functions
****************

My helper functions which convert a normal :code:`char*` or an :code:`apr_time_t` into the required :code:`apr_datum_t` are called:

*   :code:`string2datum(apr_pool_t *p, char *toDatum)`
*   :code:`time2datum(apr_pool_t *p, apr_time_t *toDump)`
*   :code:`checkError(apr_status_t rv)`

You eventually might ask what the pool is for. Since the :code:`apr_datum_t` type holds a :code:`char *` and the according size of that pointer, we have to allocate space somewhere. That space is allocated on the pool you give that function. So make sure your pools lifetime is big enough so you can retrieve the value the function built for you. The :code:`checkError` function is used for checking the return value that the various database access functions might return. This is basically to avoid lots of typing.

Worker functions
================

Now you need a few functions that do the actual work for you. APRs abstraction is not bad, but cannot handle automatic key generation in this case. APR uses a key for every saved :code:`apr_datum_t`. We want to store a more complex datatype to disc, so we need to have a key for every field of our structure when we do want to save it.

The basic idea is to flatten the structure when storing it. So we are writing every field of the structure linearly to disc. When we read the data from disc we do that in the same order as we have written it. This mechanism should restore the original data structure we were working with. The following functions persist a single datum, a whole struct and the read function reads back the whole struct from the disc.

*   :code:`apr_status_t persistUserRecord(unsigned int *key, apr_dbm_t *database, struct userRecord *user)`
*   :code:`apr_status_t persistDatum(unsigned int *key, apr_dbm_t *database, apr_datum_t *toDump)`
*   :code:`struct userRecord *readUserRecord(apr_pool_t *resultPool, apr_dbm_t *db, apr_datum_t *lastReadKey)`

These functions are not implemented, but might give you an idea of how the API can look like if you want to serialize a whole bunch of data.

*   :code:`void persistUserRecords(apr_table_t *userRecords)`
*   :code:`apr_table_t *readUserRecords(apr_pool_t *resultPool)`

You surely are interested in how the actual code looks by now. Now i will no longer deprive you from that.

.. include:: ../examples/c/aprWithSdbm.c
    :code: c

Hope you enjoyed it.
