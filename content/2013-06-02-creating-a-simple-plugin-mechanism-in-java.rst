Creating A Simple Plugin Mechanism In Java
##########################################

:date: 2013-06-02 16:36:18 +0100
:tags: Java, Lookup API, Service Loader
:category: Programming
:author: Jens Frey
:summary: This article shows how to create a simple plugin mechanism in Java.

Our goal is to create a simple plugin mechanism in Java. On top of that we want our execution environment to be divided into three
distinct phases. These phases are named:

#.   PRE_PROCESS
#.   PROCESS
#.   POST_PROCESS

This will allow us to run certain plugins at specific phases during program execution. So if a plugin is for example registered on the :code:`PRE_PROCESS` hook, we can - and must - guarantee that this plugin will be executed before the :code:`PROCESS` phase.

After we’ve laid out the basic requirements now, the question is: “How do we implement that?”.

The first thing that probably comes to mind when talking about a plugin mechanism is the standard `Java ServiceLoader <http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html>`_ that was officially opened with the JDK6 and has been in there since the JDK 1.3 days. Another mechanism that can be used to implement a system like this is the Netbeans Lookup API.

After defining a basic interface for the plugins we want to create, we’ll have a look at both mechanisms to see how they compare.

Create Domain Objects
*********************
.. _create-domain-objects:

Ok, so let’s start defining the actual :code:`Plugin` interface so we have a common hook to execute our plugins on.

We make the interface itself very easy and just define a process method in which we’ll later have implementations append the actual phase and class name to the :code:`String` instance that was passed as a parameter. We’ll return that :code:`String` to the caller, so we can subsequently use it and demonstrate very easily when and where the plugin was executed.

The interface we want to implement looks like this:

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/Plugin.java
    :code: Java

As a next step we define three interfaces that act as marker interfaces so we can differentiate the lifecycle phase the plugin should be run in by the name of the implemented interface. These interfaces are defined as shown below.

For the :code:`PRE_PROCESS` phase: 

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/PreProcessable.java
    :code: Java

For the :code:`PROCESS` phase: 

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/Processable.java
    :code: Java

For the :code:`POST_PROCESS` phase:

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/PostProcessable.java
    :code: Java

Now that we’ve defined the interfaces we need some implementations that actually do something. At first we implement a plugin that should be run in the :code:`PRE_PROCESS` phase. It looks like:

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/PreProcessPlugin.java
    :code: Java

The following implementations are running on the main processing hook :code:`PROCESS` and look as follows:

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/ProcessPlugin.java
    :code: Java

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/MoreProcessPlugin.java
    :code: Java


And finally some implementation for the :code:`POST_PROCESS` hook:

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/PostProcessPlugin.java
    :code: Java

Now that was quite a bit of work, wasn’t it? But you’ll be shortly rewarded with a nice and pluggable system. Now all we need to be happy is a piece of code that actually runs all of these plugins. Since this code looks slightly different whether we use Java’s ServiceLoader mechanism directly or the Netbeans :code:`Lookup` all the bits ’n pieces required to make this work are outlined in separate sections shown below.

Service Loader
**************

So let’s first have a look at the ServiceLoader mechanism. To prepare implementations of a specific interface for automatic lookup by the ServiceLoader you need to create files in the :code:`META-INF/services` of your resulting JAR file. I will describe inclusion of that folder on a Maven based project.

In order to get the :code:`META-INF/services` folder into your maven output structure you have to create a folder named :code:`resources` under :code:`src/main`. Inside the created :code:`resources` folder you must place the folder :code:`META-INF` and underneath that the folder :code:`services` which Maven will copy when you launch the build.

As initially mentioned you need to put files inside the :code:`services` folder. These files must be named after the interface you want the automatic lookup mechanism to find implementations for.

The content of these files is the actual class name of every implementation you want the ServiceLoader to find. If you have multiple implementations of a interface you need to include every implementation class name inside that file delimited by a new line.

In our case these three files we need to create must be named (please ensure you choose the correct package name that prepends the interface name):

*   :code:`org.coffeecrew.tutorials.simplepluginmechanism.PreProcessable`
*   :code:`org.coffeecrew.tutorials.simplepluginmechanism.Processable`
*   :code:`org.coffeecrew.tutorials.simplepluginmechanism.PostProcessable`

These files must be filled with the following content (please ensure you choose the correct package name that prepends the implementation class name).

org.coffeecrew.tutorials.simplepluginmechanism.PreProcessable
  .. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/resources/META-INF/services/org.coffeecrew.tutorials.simplepluginmechanism.PreProcessable
      :code: text

org.coffeecrew.tutorials.simplepluginmechanism.Processable
  .. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/resources/META-INF/services/org.coffeecrew.tutorials.simplepluginmechanism.Processable
      :code: text

org.coffeecrew.tutorials.simplepluginmechanism.PostProcessable
  .. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/resources/META-INF/services/org.coffeecrew.tutorials.simplepluginmechanism.PostProcessable
      :code: text

Now we’ve hopefully prepared everything the ServiceLoader needs to do it’s job. All that’s missing now is a simple program that executes our plugin mechanism.

Plugin Executor
===============

A very simple implementation that will execute all plugins we have written with the ServiceLoader mechanism is quickly written. To guarantee the execution order of the plugins as we initially defined we must implement the code as shown below.

.. include:: examples/java/SimplePluginMechanismServiceLoader/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/SimplePluginExecutorServiceLoader.java
    :code: Java

As you can see the code is a pretty straight forward implementation. It runs through every implementation that is available for a given interface type and runs the :code:`process()` method on it.

If you did everything right you should now see some output similar to:

Sample Output
   .. code-block:: text

      [PRE_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PreProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.ProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.MoreProcessPlugin
      [POST_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PostProcessPlugin

As we can see from the output every phase was executed. Where we had two plugins available, both were run as well. This fulfills our basic requirements.

Netbeans Lookup
***************

By utilizing the Lookup library you can very easily achieve loose coupling, as you can with the ServiceLoader described above. On top of that you get some nice annotations and the ability to give your service implementation a certain priority when it should be run.

Now let’s have a look at the `Netbeans Lookup API <http://bits.netbeans.org/dev/javadoc/org-openide-util-lookup/org/openide/util/lookup/doc-files/lookup-api.html>`_ and see what’s the difference to the ServiceLoader.

Well, the obvious first difference is that you need to include a additional library in your project. That library, which was initially strongly coupled to the Netbeans platform itself, was over time separated from the platform so it can be used in applications that are not based on the Netbeans platform and is available as a separate Maven dependency these days.

For the dependency resolution to work you need to include the Netbeans repository in your :code:`pom.xml` so you’re able to get a hold of the library as this library is not available in one of the well know central repositories as for example `The Central Repository <http://repo.maven.apache.org>`_. There is a `discussion <https://netbeans.org/bugzilla/show_bug.cgi?id=202041>`_ going on about this which may or may not lead to a centrally available dependency in the future.

You can however reference the library by appending the following information into your projects :code:`pom.xml` file.

Netbeans Lookup Maven Dependencies
   .. code-block:: xml

      <repositories>
         <repository>
            <id>Netbeans Repository</id>
            <url>http://bits.netbeans.org/maven2/</url>
         </repository>
      </repositories>

      <dependencies>
         <dependency>
            <groupId>org.netbeans.api</groupId>
            <artifactId>org-openide-util-lookup</artifactId>
            <version>RELEASE73</version>
         </dependency>
      </dependencies>

Don’t forget to execute a build of your project, so maven can download the newly added dependency for you.

If you’re still working on the initial project where we’ve used the ServiceLoader mechanism please make sure you delete the :code:`resources` folder now, you don’t need it in conjunction with the Lookup API.

For the Lookup API to work you need to add annotations to your domain objects. These annotations will create the files with the interface name and fill their content with the class names that provide an implementation for that interface.

This probably sounds more difficult than it actually is now, but it’s actually a trivial thing to do. You simply need to add a little :code:`@ServiceProvider` annotation in front of your class name.

To make this as simple as possible I print the four classes again, their content is the same as outlined in the `Create Domain Objects <{filename}/2013-06-02-creating-a-simple-plugin-mechanism-in-java.rst#create-domain-objects>`_ section, just with the added annotation.

PreProcessPlugin.java
   .. include:: examples/java/SimplePluginMechanismLookupAPI/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/PreProcessPlugin.java
      :code: Java

ProcessPlugin.java
   .. include:: examples/java/SimplePluginMechanismLookupAPI/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/ProcessPlugin.java
      :code: Java

MoreProcessPlugin.java
   .. include:: examples/java/SimplePluginMechanismLookupAPI/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/MoreProcessPlugin.java
      :code: Java

PostProcessPlugin.java
   .. include:: examples/java/SimplePluginMechanismLookupAPI/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/PostProcessPlugin.java
      :code: Java

Now that we’ve changed our domain object to utilize the annotations provided by the Lookup API we need an executor method to run our plugins in. All that changes compared to the ServiceLoader approach is the way you look up the implementations. It’s just a different API call.

Plugin Executor
===============

Again we’ll show a very simple implementation that will execute all plugins we have written.

SimplePluginExecutorLookupAPI.java
   .. include:: examples/java/SimplePluginMechanismLookupAPI/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/SimplePluginExecutorLookupAPI.java
      :code: Java

As you can see this code is basically the same as with the ServiceLoader approach. It runs through every implementation that is available for a given interface type and runs the :code:`process()` method on it.

However, you might notice that the output slightly changed. With the Lookup API the :code:`MoreProcessPlugin` is executed before the :code:`ProcessPlugin`. This is something we may not want in case we care about the order in which these plugins need to run when they are executed within the same phase.

Service Loader Output
   .. code-block:: text

      [PRE_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PreProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.ProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.MoreProcessPlugin
      [POST_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PostProcessPlugin

Lookup API Output
   .. code-block:: text

      [PRE_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PreProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.MoreProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.ProcessPlugin
      [POST_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PostProcessPlugin

Luckily there s a mechanism which allows you to control exactly that behavior. And it’s built in right into the Lookup API. The :code:`@ServiceProvider` annotation let’s you set a position attribute. This position attribute defines when this implementation should be run, if another implementation is also available. The higher the position number is, the earlier the implementation is run.

Since the default position value is 0 it is sufficient to assign a value only to the :code:`ProcessPlugin` implementation. Changing the :code:`@ServiceProvider` line in the :code:`ProcessPlugin` to

.. code-block:: Java

   @ServiceProvider(service = Processable.class, position = 10)

Will result in the following output which reflects the actual processing order of the ServiceLoader example.

Comparison
**********

As you could see the Lookup API simplifies work by generating the files you’d normally have to create in :code:`META-INF/services` manually. By the generative approach and the position attribute you can also guarantee a specific execution order without the hassle to manually reorder the content of the files in :code:`META-INF/services`.

The Lookup API offers much more advanced features aside from the ones that were mentioned here, like for example Lookup templates that allow you to query information on an object without instantiating it or listening to changes in a Lookup. You could also lookup different implementations for a service based on it’s MIME type.

One More Thing
**************

Now I have actually one more thing I want to share with you. This approach works for both previously introduced plugin mechanisms, but will be included in the Lookup API example only.

It allows you to add or remove processing phases without having to rewrite all your processing code. This can simply be achieved by assigning the interface classes to an enumeration type. So let’s first implement the enumeration type that ties together the processing phase and the interface assigned to it.

Phase.java
   .. include:: examples/java/SimplePluginMechanismLookupAPI/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/Phase.java
      :code: Java

As you can see the implementation is fairly trivial, yet effective. Now all we need is an adapted executor for this to work. An implementation that utilizes the new idea looks as follows:

SimplePhaseExecutor.java
   .. include:: examples/java/SimplePluginMechanismLookupAPI/src/main/java/org/coffeecrew/tutorials/simplepluginmechanism/SimplePhaseExecutor.java
      :code: Java

Execution of the above code will result in the following output:

Lookup API Output
   .. code-block:: text

      [PRE_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PreProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.ProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.MoreProcessPlugin
      [POST_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PostProcessPlugin

Which is what we have expected it to do. The cool part however is - if you need to change the execution order of the phases, add a new phase, or delete a phase - you do not need to change the executor code but only the order or number of elements in the :code:`Phase` enumeration.

So should you decide to no longer include the :code:`PRE_PROCESS` phase anymore you just need to remove the phase from the enumeration type, re-run the example aaaand:

Output - PRE_PROCESS Phase Removed
   .. code-block:: text

      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.ProcessPlugin
      [PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.MoreProcessPlugin
      [POST_PROCESS] org.coffeecrew.tutorials.simplepluginmechanism.PostProcessPlugin

There you go, no more annoying :code:`PRE_PROCESSING`, just as expected :)

If you like you can clone the two projects from Github:

* `ServiceLoader Version <https://github.com/authsec/examples/tree/master/java/SimplePluginMechanismServiceLoader>`_
* `LookupAPI Version <https://github.com/authsec/examples/tree/master/java/SimplePluginMechanismLookupAPI>`_

Hope you enjoyed the post! Leave comments, like ’n share!
