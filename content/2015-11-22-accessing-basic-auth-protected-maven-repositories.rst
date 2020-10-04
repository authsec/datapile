Accessing Basic Auth Protected Maven Repositories
#################################################

:date: 2015-11-22T17:23:31+01:00
:tags: Arquillian, Testing
:category: Infrastructure
:author: Jens Frey
:summary: How to access a basic auth protected maven repository.

When you try to access a basic auth protected maven repository you might actually already run into all sorts of weird errors, but now trying a basic auth protected maven repository during a Arquillian build when it is creating the deployment. 

You migh have figured out the first part of accessing the maven repositoryis setting a :code:`Authorization` header and your configuration looks something like this (the encoded username is :code:`test` and the password is :code:`s3cur3`) :

.. code-block:: xml

   <servers>
   <server>
   <!-- Link this id here to the repo ID in the mirror section -->
   <id>repo</id>
   <configuration>
      <httpHeaders>
         <property>
         <name>Authorization</name>
         <value>Basic dGVzdDpzM2N1cjM=</value>
         </property>
      </httpHeaders>
   </configuration>
   </server>
   </servers>

What you now just need to do on top of that is add the :code:`username` and :code:`password` to your :code:`settings.xml` configuration file like so.

.. code-block:: xml

   <servers>
   <server>
   <!-- Link this id here to the repo ID in the mirror section -->
   <id>repo</id>
   <username>test</username>
   <password>s3cur3</password>
   <configuration>
      <httpHeaders>
         <property>
      <name>Authorization</name>
      <value>Basic dGVzdDpzM2N1cjM=</value>
         </property>
      </httpHeaders>
   </configuration>
   </server>
   </servers>

Get the `Gist here <https://gist.github.com/anonymous/bc3a2bc73d66ecb2a9f163a99618cdcd>`_!

You especially need add values for these two if you run into the following exception.
 
.. code-block:: java

   java.lang.RuntimeException: Could not invoke deployment method: public static org.jboss.shrinkwrap.api.Archive org.coffeecrew.Test.createDeployment()
   at org.eclipse.aether.internal.impl.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:459)
   at org.eclipse.aether.internal.impl.DefaultArtifactResolver.resolveArtifacts(DefaultArtifactResolver.java:262)
   at org.eclipse.aether.internal.impl.DefaultArtifactResolver.resolveArtifact(DefaultArtifactResolver.java:239)
   at org.apache.maven.repository.internal.DefaultArtifactDescriptorReader.loadPom(DefaultArtifactDescriptorReader.java:320)
   at org.apache.maven.repository.internal.DefaultArtifactDescriptorReader.readArtifactDescriptor(DefaultArtifactDescriptorReader.java:217)
   at org.eclipse.aether.internal.impl.DefaultDependencyCollector.process(DefaultDependencyCollector.java:461)
   at org.eclipse.aether.internal.impl.DefaultDependencyCollector.process(DefaultDependencyCollector.java:573)
   at org.eclipse.aether.internal.impl.DefaultDependencyCollector.collectDependencies(DefaultDependencyCollector.java:261)
   at org.eclipse.aether.internal.impl.DefaultRepositorySystem.resolveDependencies(DefaultRepositorySystem.java:342)
   at org.jboss.shrinkwrap.resolver.impl.maven.bootstrap.MavenRepositorySystem.resolveDependencies(MavenRepositorySystem.java:120)
   at org.jboss.shrinkwrap.resolver.impl.maven.MavenWorkingSessionImpl.resolveDependencies(MavenWorkingSessionImpl.java:266)
   at org.jboss.shrinkwrap.resolver.impl.maven.MavenStrategyStageBaseImpl.using(MavenStrategyStageBaseImpl.java:71)
   at org.jboss.shrinkwrap.resolver.impl.maven.MavenStrategyStageBaseImpl.using(MavenStrategyStageBaseImpl.java:40)
   Caused by: org.eclipse.aether.transfer.ArtifactTransferException: Could not transfer artifact org.slf4j:slf4j-log4j12:pom:1.4.3 from/to repo (https://repo.protected.example.com/content/groups/internal/): Access denied to: https://repo.protected.example.com/content/groups/internal/org/slf4j/slf4j-log4j12/1.4.3/slf4j-log4j12-1.4.3.pom
   at org.eclipse.aether.connector.wagon.WagonRepositoryConnector$6.wrap(WagonRepositoryConnector.java:1016)
   at org.eclipse.aether.connector.wagon.WagonRepositoryConnector$6.wrap(WagonRepositoryConnector.java:1004)
   at org.eclipse.aether.connector.wagon.WagonRepositoryConnector$GetTask.run(WagonRepositoryConnector.java:725)
   at org.eclipse.aether.util.concurrency.RunnableErrorForwarder$1.run(RunnableErrorForwarder.java:67)
   at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
   at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
   at java.lang.Thread.run(Thread.java:745)
   Caused by: org.apache.maven.wagon.authorization.AuthorizationException: Access denied to: https://repo.protected.example.com/content/groups/internal/org/slf4j/slf4j-log4j12/1.4.3/slf4j-log4j12-1.4.3.pom
   at org.apache.maven.wagon.providers.http.LightweightHttpWagon.fillInputData(LightweightHttpWagon.java:144)
   at org.apache.maven.wagon.StreamWagon.getInputStream(StreamWagon.java:116)
   at org.apache.maven.wagon.StreamWagon.getIfNewer(StreamWagon.java:88)
   at org.apache.maven.wagon.StreamWagon.get(StreamWagon.java:61)
   at org.eclipse.aether.connector.wagon.WagonRepositoryConnector$GetTask.run(WagonRepositoryConnector.java:660)
   at org.eclipse.aether.util.concurrency.RunnableErrorForwarder$1.run(RunnableErrorForwarder.java:67)
   at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
   at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
   at java.lang.Thread.run(Thread.java:745)

