How To Setup Hovercraft On Mac OS
#################################

:date: 2016-05-27T20:02:00+01:00
:tags: Presentation, Hovercraft
:category: Infrastructure
:author: Jens Frey
:summary: How to set up Hovercraft on Mac OS X.

If you want to setup hovercraft for creating presentations on your Mac, the following steps may help if you already have `MacPorts <https://www.macports.org/>`_ installed.

At first install python if you haven't already:

.. code-block:: shell

   $#> sudo port install python33 py33-pip

As you'll run into problems, you have to export the language for the next command to run successfully. So add these two lines to your :code:`~/.bash_profile`

.. code-block:: shell

   export LC_ALL=en_US.UTF-8
   export LANG=en_US.UTF-8

And while we're at it, add the following path to your :code:`PATH` variable as well, so you can later access the :code:`hovercraft` command directly.

.. code-block:: shell
   
   $#> /opt/local/Library/Frameworks/Python.framework/Versions/3.3/bin

Your path then looks something like this:

.. code-block:: shell
   
   $#> export PATH="/opt/local/bin:/opt/local/sbin:/opt/local/Library/Frameworks/Python.framework/Versions/3.3/bin:$PATH"

Now you can finally install hovercraft like this:

.. code-block:: shell
   
   $#> sudo -H pip install hovercraft

Happy hovercrafting!