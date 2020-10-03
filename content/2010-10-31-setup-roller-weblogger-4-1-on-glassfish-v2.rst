Setup roller weblogger 4.1 on Glassfish v2
##########################################

:date: 2010-10-31 11:11:11 +0100
:tags: Glassfish, Roller
:category: Infrastructure
:author: Jens Frey
:summary: This guide will walk you through installing the Apache Roller Blog Software, version 4.1(-dev).

This guide will walk you through installing the Apache Roller Blog Software, version 4.1(-dev). The installation I did, resides on a Debian based distribution (Ubuntu works too, just prefix the commands with sudo).

.. note:: This was implemented/tested with Roller Weblogger 4.1 and Glassfish v2UR2

Quick install
*************

This is for the impatient, you need to have :code:`jar` and :code:`wget` in your $PATH

If you just want to have roller to be set up for you to do e.g. some work on roller templates, or just to basically play with it a bit, here is the real quick deal for you (This will install everything into /opt/roller).

Download the `automated installer script <https://raw.githubusercontent.com/authsec/examples/master/sh/rollerGlassfish.sh>`_, save it to e.g. your home directory. Then call the script like :code:`./rollerGlassfish.sh realLongKey1 realLongKey2 run-as-username run-as-groupname`.

.. note:: You can get those long keys easily from `GRC <https://www.grc.com/passwords.htm>`_.

Manual installation
*******************

The following text basically describes what the script is automatically doing for you. This way you can change the according parameters which will affect your installation. Most probably you will just have to change the following variables inside the install script.

Prerequisites
=============

You have to install a few things before you can actually start installing roller on your system. At first you need to be sure that the :code:`jar` command is installed on your system, since this is required for the build you will be doing on your system. On a Debian based system you can do that with the following command:

.. code-block:: console

	root@sigusr1:$ aptitude install sun-java5-jdk


After the JDK installation you basically have everything ready to go, but wait if you want to use my `automated installer script <https://raw.githubusercontent.com/authsec/examples/master/sh/rollerGlassfish.sh>`_ you need to have the :code:`wget` command installed too, since the script tries to download the Glassfish application server and the given Apache Roller version by itself in order to be able to install it for you.

.. code-block:: bash

	root@sigusr1:$ #
		TARGET=/opt/roller
		TARGET_TMP=${TARGET}/tmp
		UPLOADS=$TARGET/uploads
		THEMES=$TARGET/themes
		PLANET_CACHE=$TARGET/planetcache
		SEARCH_INDEX=$TARGET/searchindex
		#ROLLER_FILENAME="apache-roller-4.0.1-snapshot-20080211.tar.gz"
		ROLLER_FILENAME="apache-roller-4.1-snapshot-m1.tar.gz"
		CTX_ROOT="blogs"

Gist: https://gist.github.com/authsec/455940a3f6bd5673c1c9dd16ea4ec0af

- `Download Glassfish <https://glassfish.dev.java.net/downloads/v2ur2-b04.html>`_

Choose install location
-----------------------

As a last prerequisite step you need to choose an install location. I chose :code:`/opt/roller` to be the install location of my choice.

Setup user and group
====================

As you probably won't be running roller as root, you'll have to setup a user and a group for your roller installation. This, again on Debian based systems, is done using the following commands:

.. code-block:: bash

	root@sigusr1:$
		mkdir /opt/roller
		addgroup --system roller
		adduser --home /opt/roller \
		--shell /bin/bash \
		--no-create-home \
		--ingroup roller \
		--disabled-password \
		--system roller

Setup Glassfish
===============

I assume you downloaded roller and glassfish into :code:`/opt/roller/tmp`. In order to get the Glassfish installation working, you need to have :code:`JAVA_HOME` exported into your environment. Then you can start running the Glassfish installer. On a bourne shell do (inside the directory where you saved the Glassfish installer):

.. code-block:: bash

	root@sigusr1:$
		cd /opt/roller/tmp
		export JAVA_HOME=/usr/lib/jvm/java-1.5.0-sun
		java -Xmx256m -jar glassfish-installer-v2ur2-b04-linux.jar

The installer will ask you if you are willing to accept the license agreement. If you are running this on a remote machine, it will ask you on the command line, whether or not you are willing to accept; on your local machine it will show a nice graphical dialog for you to accept. After that setup is completed, do (machine name is included, so you're able to see in which directory I'm operating)

.. code-block:: bash

	j@sigusr1:/opt/roller/tmp$ mv glassfish ..
	j@sigusr1:/opt/roller/tmp$ cd ../glassfish/
	j@sigusr1:/opt/roller/glassfish$ chmod -R +x lib/ant/bin

As a next step you have to setup a domain for roller within Glassfish, this is a rather easy task to do:

.. code-block:: bash

	j@sigusr1:/opt/roller/glassfish$ lib/ant/bin/ant -f setup.xml -Ddomain.name=roller

Now start Glassfish, so we can do s.th. with it ...

.. code-block:: bash

	j@sigusr1:/opt/roller/glassfish$ ./bin/asadmin start-domain roller

Setup built in database
-----------------------

The following series of command all takes place inside the :code:`/opt/roller/glassfish` directory.

.. code-block:: bash

	root@sigusr1:$ ./bin/asadmin create-jdbc-connection-pool \
		--datasourceclassname org.apache.derby.jdbc.EmbeddedDataSource \
		--property databaseName=\$\{com.sun.aas.instanceRoot\}/databases/rollerdb:\
		connectionAttributes=\;create\\=true rollerpool
		./bin/asadmin ping-connection-pool rollerpool
		./bin/asadmin create-jdbc-resource --connectionpoolid=rollerpool jdbc/rollerdb

Setup JNDI mail resource
------------------------

If that would work in roller 4.1(-dev) with Glassfish, you would do:

.. code-block:: bash

	root@sigusr1:$ ./bin/asadmin create-javamail-resource --mailhost localhost --mailuser rollermail --fromaddress roller\@blogs\.coffeecrew\.org mail/Session

Securing Glassfish
------------------

Some of you might be thinking about running Glassfish behind an Apache reverse proxy. This is exactly what I am thinking about. So if you plan to do that, it might come in handy that Glassfish would only accept connections from the local machine and therefore not let anyone easily bypass your secured apache instance. First we delete both HTTP listener instances that are listening on ports 8080 and 8443 and then recreate the 8080 one. As we are proxying with apache, we do not need the SSL listener (port 8443) anyway.

And while we're just doing it, let's rebind those IIOP services to.

.. code-block:: bash

	root@sigusr1:$
		./bin/asadmin delete-http-listener http-listener-1
		./bin/asadmin delete-http-listener http-listener-2
		./bin/asadmin create-http-listener --listeneraddress 127.0.0.1 --listenerport 8080 --acceptorthreads 32 --enabled=true --defaultvs server --securityenabled=false roller-listener

		# Configure admin page to listen locally too
		./bin/asadmin set server.http-service.http-listener.admin-listener.address=127.0.0.1

		# Disable IIOP stuff to listen globally, we do not need that right now.
		./bin/asadmin set server.iiop-service.iiop-listener.SSL.address=127.0.0.1
		./bin/asadmin set server.iiop-service.iiop-listener.SSL_MUTUALAUTH.address=127.0.0.1
		./bin/asadmin set server.iiop-service.iiop-listener.orb-listener-1.address=127.0.0.1

		# Disable JMX connector for remote access
		./bin/asadmin set server.admin-service.jmx-connector.system.enabled=false

		# JMS
		./bin/asadmin set server.jms-service.jms-host.default_JMS_host.host=localhost

		# Require client authentication, just to be sure ...
		./bin/asadmin set server.iiop-service.client-authentication-required=true

Now, to make changes effective we have to restart Glassfish. But before that we want to make sure everything has correct permissions for our newly created user, won't we?

.. code-block:: bash

	root@sigusr1:$ # Fix permissions
		chown -R roller:roller /opt/roller

		# Restart gf to make changes effective
		./bin/asadmin stop-domain roller

		# Start as roller user
		su -c "./bin/asadmin start-domain roller" roller

Setup and configure Roller
==========================

Setting up Roller 4.1(-dev) is pretty easy. We start by extracting the tarball we downloaded earlier. Since we have set our theme directory to be :code:`/opt/roller/themes` we do have to copy the themes we want to use there. As a next step we *really want to have security keys **changed***. This is done by either editing the :code:`security.xml` file manually or using a :code:`sed` expression. After we have changed the keys, we can pack us a nice :code:`roller.war` file. The few commands below execute the described actions.

.. code-block:: bash

	root@sigusr1:$ #Roller setup
		cd /opt/roller/tmp
		tar zxvf apache-roller-4.1-snapshot-m1.tar.gz

		#Copy themes
		cd apache-roller*/webapp/roller/themes
		cp -vR * /opt/roller/themes

		cd ../WEB-INF
		cp security.xml /tmp

		# actually change keys
		cat /tmp/security.xml | \
		sed "s/name=\"key\" value=\"anonymous\"/name=\"key\" value=\"myOwnLongKey\"/" | \
		sed "s/name=\"key\" value=\"rollerlovesacegi\"/name=\"key\" value=\"myOwnLongKey2\"/" \
		> security.xml

		#Pack war file
		cd ..
		jar cvf ../../../roller.war *


Now we're nearly finished ... just a few seconds away from experimenting with your own roller instance now :). As a next necessary step we need to create a custom configuration file for Roller. That configuration file has to be saved in :code:`/opt/roller/glassfish/domains/roller/lib/classes/roller-custom.properties` to take effect. The configuration file can be built as follows:

.. code-block:: bash

	root@sigusr1:$ #Build roller-custom.properties
	cat <<EOF > $TARGET/glassfish/domains/roller/lib/classes/roller-custom.properties

	installation.type=auto

	#Should work with JNDI but maybe not with glassfish
	mail.configurationType=properties
	mail.hostname=localhost

	planet.aggregator.enabled=true
	uploads.dir=$UPLOADS
	themes.dir=$THEMES
	search.index.dir=$SEARCH_INDEX
	planet.aggregator.cache.dir=$PLANET_CACHE
	EOF

Now that the configuration is in place, we are finally ready to deploy the Roller application. With the deployment we are able to specify a context root, which is the (URL) location where your application can be reached later on. So if you specify :code:`blogs` then your application can later be accessed at :code:`http://your.example.com:8080/blogs`.

.. code-block:: bash

	root@sigusr1:$ #Deploy application
		cd $TARGET/glassfish
		./bin/asadmin deploy --contextroot blogs ../tmp/roller.war

Just to be sure everything you've installed so far has correct permission, you might want to run the following commands again. It fixes your permissions and runs Glassfish as roller user.

.. code-block:: bash

	root@sigusr1:$ # Fix permissions
		chown -R roller:roller /opt/roller

		#Restart gf to make changes effective
		./bin/asadmin stop-domain roller

		# Start as roller user
		su -c "./bin/asadmin start-domain roller" roller

After you've done all that you now should have a ready to go roller installation. Now go visit :code:`http://localhost:8080/blogs` and configure your shiny new roller installation. It is pretty much self explanatory, but if you need further assistance, you might want to have a look into the installation guide, which you can get `here <http://roller.apache.org/download.cgi>`_ (see chapter 8ff).

Now after you've set up everything exactly as you like, you should change :code:`/opt/roller/glassfish/domains/roller/lib/classes/roller-custom.properties` to read :code:`installation.type=manual`
