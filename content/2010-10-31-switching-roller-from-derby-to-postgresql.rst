Switching roller from Derby to PostgreSQL
#########################################

:date: 2010-10-31 14:00:34 +0100
:tags: Glassfish, Roller, Database, PostgreSQL
:category: Infrastructure
:author: Jens Frey
:summary: This guide will walk you through installing the PostgreSQL database for use in Apache Roller.

Since Roller seems to have problems with the Glassfish built-in Derby database, especially when it comes to "Full preview" (you might end up with an HTTP 404 error code if you try to preview your post), you might want to switch your Roller installation to PostgreSQL_.

This article will explain, how you can switch the installed Roller instance from Glassfishs built-in derby database to the PostgreSQL database.

.. note:: This was implemented/tested with Roller Weblogger 4.1 and Glassfish v2UR2

Quick install
*************

For the people who don't care if anything goes wrong or just don't like to type at all, `here is a little script <https://gist.github.com/authsec/a048fb1d1ade8004dd55f4ede17c077b>`_, which might help you save a few keystrokes. Just be aware of the fact that you might have to change a few variables inside the script.

Manual installation
*******************

This describes basically what the script in the quick installation tries to achieve by being run.

Prerequisites
=============

As with the roller install itself, you need of course a few things before you can start. In this case you need of course the PostgreSQL_ database itself and a JDBC driver to attach roller to the database.

The installation of the database depends on the type of operating system you are using. I describe the setup using a Debian_ based system, since this is so nice and easy :).

Get the database driver
=======================

To enable Glassfish to talk to your postgres installation, you have to supply it some code. Glassfish likes Java the best and you can get some JDBC drivers from `here <http://jdbc.postgresql.org/download.html>`_ , but specifically the `postgresql-8.3-604.jdbc3.jar <http://jdbc.postgresql.org/download/postgresql-8.3-604.jdbc3.jar>`_ is of interest to us.

So go get the JDBC driver and save it in :code:`/opt/roller/glassfish/domains/roller/lib`

Install database
================

For a debian based system just enter the following command as user root (or prefix it with sudo on ubuntu). You might of course have a newer version of postgresql available with you current installation.

.. code-block:: bash

	root@sigusr1:$ aptitude install postgresql-8.1


After the installation finishes, you might want to create the database roller will be accessing later and also create a user for use with roller. This can be achieved by entering the following sequence of commands into a terminal of your choice.

.. note:: Be aware of the fact that you have to *execute* the commands as user *postgres*.

.. code-block:: bash

	root@sigusr1:$
		# Get root
		su -

		# Get postgres user
		su - postgres

		# create database
		createdb rollerdb

		# create roller user
		createuser roller
		# Answers
		# Shall the new role be a superuser? (y/n) n
		# Shall the new role be allowed to create databases? (y/n) n
		# Shall the new role be allowed to create more new roles? (y/n) n

		# Enter postgres prompt
		psql

When you are in the prompt which will look something like this:

.. code-block:: sql

	Welcome to psql 8.3.4, the PostgreSQL interactive terminal.

	Type:  \copyright for distribution terms
			\h for help with SQL commands
			\? for help with psql commands
			\g or terminate with semicolon to execute query
			\q to quit

	postgres=#

Enter the following SQL statements which change the passwords for the roller  
and postgres user accordingly:

.. code-block:: sql

	alter user roller with password 'roller';
	alter user postgres with password 'postgres';

You are now finished using the postgres user. You may want to continue as user roller with the following commands.

If you followed the `Setup Roller Weblogger 4.1 On Glassfish V2 <{filename}/2010-10-31-setup-roller-weblogger-4-1-on-glassfish-v2.rst>`_  tutorial, you do have to do an additional step to get rid of the previously assigned JDBC connection; that is deleting the JDBC connection pool. Aside from that we simply create a JDBC connection pool and the according resource. Once that is done you might want to start configuring your roller installation.

.. code-block:: bash

	root@sigusr1:$
		#if you followed glassfish ... (cleans jdbc resource too)
		./bin/asadmin delete-jdbc-connection-pool --cascade rollerpool

		./bin/asadmin create-jdbc-connection-pool
		--datasourceclassname org.postgresql.ds.PGSimpleDataSource \
		--restype javax.sql.DataSource \
		--property portNumber=5432:password=roller:user=roller:serverName=localhost:databaseName=rollerdb \
		rollerpool

		./bin/asadmin ping-connection-pool rollerpool
		./bin/asadmin create-jdbc-resource --connectionpoolid=rollerpool jdbc/rollerdb

Once that is finished, you should restart Glassfish in order to make changes effective. This is done through the following command:

.. code-block:: bash

	root@sigusr1:$ su - roller -c "cd glassfish; ./bin/asadmin stop-domain roller" && \
					   su - roller -c "cd glassfish; ./bin/asadmin start-domain roller"

When you have finished all that, you now should be able to point you browser to http://localhost:8080/blogs and start configuring your Roller installation. Have fun.

.. _PostgreSQL: https://www.postgresql.org/
.. _Debian: http://www.debian.org/
