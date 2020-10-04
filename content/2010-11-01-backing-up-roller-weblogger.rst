Backing up roller weblogger
###########################

:date: 2010-11-01 09:00:34 +0100
:tags: Glassfish, Roller, Backup
:category: Infrastructure
:author: Jens Frey
:summary: This guide will walk you through installing the PostgreSQL database for use in Apache Roller.

If you followed the `Setup Roller Weblogger 4.1 On Glassfish V2 <{filename}/2010-10-31-setup-roller-weblogger-4-1-on-glassfish-v2.rst>`_ tutorial, or you do have a roller installation already in place, you probably want to back that up somehow.  

Backup the file system
**********************

Since I do not know where you possibly will have installed roller to, I am using the locations from the previous how to. The backup procedure should not be that complex after all.

The data residing on your filesystem (I know, the database resides on the file system too), is the data you uploaded into you blog. You do have a :code:`/opt/roller/uploads` directory for example. Depending on how many blogs you have created, there are numerous folders inside. Each sub-folder represents data for another blog. So if your blog is called "datapile", then there will be a "datapile" sub-folder.

For the sake of simplicity you can of course just backup the whole :code:`/opt/roller` folder which should also save the custom themes you created eventually. If you installed it the way as described in the tutorial at the beginning, you do also backup search indexes and the glassfish installation with this.

.. warning:: You might not have deleted the :code:`/opt/roller/tmp` directory yet. You might backup this temporary files too in this case. If you backup the full :code:`/opt/roller` directory, you might remove the :code:`/opt/roller/tmp` folder manually, or at least move it to another location, so your compressed archive will not pack that (unnecessary) information too. A simple command line might look like as follows:

.. code-block:: bash

	$ cd /opt/ && tar cjvf /tmp/roller.tar.bz2 roller/


Restore the file system
***********************

In order to restore the file system you have to recreate your installation and then copy the files back to the location where the installation can find them, or simply :code:`untar` your backed up :code:`/opt/roller` folder.

Backup the database
*******************

The database can be backed up pretty easily. The part to remember is that you *switch to the postgres user* when you execute the :code:`pg_dumpall > /tmp/rollerDb.dump` command.

The backup command order in this case is:

.. code-block:: bash

	root@sigusr1:$
		# su - postgres
		$ pg_dumpall > /tmp/rollerDb.dump
		$ pg_dumpall | bzip2 > /tmp/rollerDb.dump.bz2 #bzipped version

Restore the database
********************

Restoring the full database is a pretty easy task. On a test installation I did, this worked like a charm. I really hope there's nothing more to add to it.

.. code-block:: bash

	root@sigusr1:$
		# su - postgres
		$ createdb rollerdb
		$ cat /tmp/rollerDb.dump.bz2 | bunzip2 | psql postgres
		$ psql -f /tmp/rollerDb.dump postgres #restore from non-compressed file

After having played back the information into the database, you might need to adopt the postgres passwords back to the values you have set them during your installation on the servlet container (in this case glassfish). You might otherwise end up with some ugly exceptions. To do that start the :code:`psql` tool as postgres user and enter the following statements (of course adopt to your password ;) ).

.. code-block:: sql
	
	postgres@sigusr1:~$ psql
	postgres=# alter user roller with password 'roller';
	postgres=# alter user postgres with password 'postgres';

References
**********

Especially when it comes to handling the database stuff, the PostgreSQL documentation was very helpful.

* http://www.postgresql.org/docs/8.1/static/backup.html

