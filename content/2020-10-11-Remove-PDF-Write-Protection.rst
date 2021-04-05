How To Remove PDF Write Protection
##################################

:date: 2020-10-11T14:23:00+01:00
:modified: 2021-04-05T16:35:00+01:00
:tags: PDF
:category: Infrastructure
:author: Jens Frey
:summary: This post shows you how you can remove write protection from a locked PDF.

Some PDFs you download are write protected. This is especially annoying if you do like to mark up the PDF in a utility like e.g. `DEVONthink <https://devontechnologies.com/apps/devonthink>`_. It is not even possible to simply highlight something as long as the PDF is write protected.

Luckily the problem can be solved with a little command line application called :code:`qpdf`.

Installation
============

There are multiple ways to install the software. You can either use the package manager of your operating system or you can use Docker_. This will allow you to quickly use the command and discard the whole execution environment if you no longer need it.

Homebrew
--------

I'm on a Mac and if you have `Homebrew <https://brew.sh/>`_ installed, you can execute :code:`brew install qpdf` in your terminal application. This will download and install the :code:`qpdf` command line tool that you can use to remove the PDF write protection.

.. note::
   I'm running the following version::

      #$> qpdf --version
      qpdf version 10.0.1
      Run qpdf --copyright to see copyright and license information.

   Keep this in mind if a feature is not available or does not work. 

If you're not on a Mac you need to use the package manager of your operating system to install the application.

You can use the utility like so:

.. code-block:: bash

   #$> cd folder-with-pdf
   #$> qpdf --decrypt --replace-input my.pdf

Docker
------

If you are already using Docker_ then why not employ a container to get the job done. The container I am using is pretty heavyweight if you just need to use the little :code:`qpdf` utility and you may want to use a smaller container. However, if you're additionally documenting stuff with  `Sphinx <https://www.sphinx-doc.org/en/master/>`_ or use `Pelican <https://blog.getpelican.com/>`_ to write your blog, the heavyweight container might be a good choice for you anyway.

You can use the `custom Sphinx <https://github.com/authsec/sphinx>`_ container as follows:

.. code-block:: bash

   #$> cd folder-with-pdf
   #$> docker run --rm -it -v $(pwd):/docs authsec/sphinx qpdf --decrypt --replace-input my.pdf

.. note:: If you're on Windows, the current working directory needs to be specified with :code:`${PWD}`.

.. _Docker: https://www.docker.com/

Bulk Update
-----------

If you do have a folder full of PDFs you want to convert, open up a terminal and navigate to the folder that contains the PDFs. Next execute the following command to bulk remove the PDF write protection:

.. code-block:: bash

   #$> cd folder-with-pdf
   #$> find . -name \*.pdf -exec sh -c 'qpdf --decrypt --replace-input "{}"' \;

You can also add another command that moves the converted file into another folder like so (note that doing it as shown below will flatten your directory structure):

.. code-block:: bash

   #$> cd folder-with-pdf
   #$> find . -name \*.pdf -exec sh -c 'qpdf --decrypt --replace-input "{}"; mv "{}" ../writeProtectionRemoved' \;
