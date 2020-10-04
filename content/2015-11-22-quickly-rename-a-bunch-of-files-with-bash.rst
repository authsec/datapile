Quickly Rename a Bunch Of Files With Bash
#########################################

:date: 2015-11-22T18:36:12+01:00
:tags: Shell, Bash
:category: Infrastructure
:author: Jens Frey
:summary: How to bulk rename files with bash.

Sometimes you might feel the need to rename a bunch of files from one file extension to another.

This can easily be done with built in shell functions. To quickly rename a bunch of file endings from say for example :code:`*.markdown` to :code:`*.md` you simply can execute the following (:code:`bash`) command.

.. code-block:: shell

   for i in *.markdown; do mv $i "${i%.markdown}.md"; done
