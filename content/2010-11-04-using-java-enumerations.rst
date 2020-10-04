Using Java Enumerations
#######################

:date: 2010-11-04 08:12:11 +0100
:tags: Java, Enum
:category: Programming
:author: Jens Frey
:summary: This article shows a basic implementation of a Java enumeration type that you can use to do a switch on Strings for example.

This article shows a basic implementation of a Java enumeration type that you can use to do a switch on Strings for example.

Introduction
************

Now for the moment let's assume that we want to write a program that is able tell us if a programming language is more hardware adjacent than another. Since we already know the adjacency of the programming language, we can group that into the enumeration.

So, we're basically enriching the Enumeration constant with some additional information that we later can easily gather from the object. To distinguish the various adjacency levels we are needed to supply some additional information to the enum so we can later ask it if a programming language is more hardware adjacent than another. In addition to that, we want to refer to the programming language with it's long, real name. So instead of just having print ASM we want to be able to extract that information as Assembler from the enum type.

The c0de
********

We'll now have a look at the code and how it's actually implemented. As you can see, you cannot rely on the default implementation of the :code:`toString()` method when you do want to print the enums name. Exactly for this the :code:`name()` method was introduced. For internal handling of the data structure Java holds an ordinal value too, so the enums can be located correctly.


.. include:: examples/java/EnumExample/ProgrammingLanguage.java
    :code: java


Next we probably want to have a sample program calling this wonderful enum ... so why not develop one?

.. include::  examples/java/EnumExample/Main.java
    :code: java

Btw. with this concept you can also do the somehow often wanted switch on strings concept (just switch on the enum and get the String name).
