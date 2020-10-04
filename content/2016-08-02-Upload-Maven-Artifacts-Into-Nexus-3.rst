Upload Maven Artifacts Into Nexus 3
###################################

:date: 2016-08-02T21:23:31+01:00
:tags: Maven, Nexus
:category: Infrastructure
:author: Jens Frey
:summary: How to upload artifacts into Nexus 3 from the command line.

Sometimes you need to upload an artifact into Nexus. With Nexus 2 this was very easy if you had filesystem access or access to the Web UI. In Nexus 3 you are no longer able to access the filesystem and do not have a UI to upload your artifacts. This is why you have to do it using the :code:`mvn` CLI.

If you have some strangely proxied system where you have :code:`ssh` access to the machine running Nexus you can also use SSH port forwarding if you are unable to reach the machine directly otherwise.

You need a configuration like this where the user has permission to deploy into your repository.

.. code-block:: xml

    <?xml version="1.0" encoding="UTF-8"?>
    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

        <servers>
            <server>
                <!-- Link this id here to the repo ID in the mirror section -->
                <id>repo</id>
                <username>deployment</username>
                <password>password</password>
            </server>
        </servers>
    </settings>

Get the `Gist here <https://gist.github.com/authsec/b603d2eaf4afc02ca4802a450838e7a1>`_!

If that is out of the way you can use the CLI to upload the artifact like so:

Please note that in this case also a different configuration file is used to
upload the artifact, to avoid working with the deployment user for normal builds
where you want the user only to have read permission.

.. code-block:: bash

    mvn -s ~/.m2/settings.xml.upload deploy:deploy-file \
        -DgroupId=org.wildfly \
        -DartifactId=wildfly-dist \
        -Dversion=10.0.0.Final \
        -DgeneratePom=true \
        -Dpackaging=tar.gz \
        -DrepositoryId=repo \
        -Durl=http://repo.example.com/repository/thirdparty \
        -Dfile=wildfly-10.0.0.Final.tar.gz
