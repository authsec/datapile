No Fuzz LaTeX Document Compilation
##################################

:date: 2021-04-11T13:03:43+01:00
:tags: Docker, LaTeX
:category: Infrastructure
:summary: How to easily compile a LaTeX document with docker

You downloaded a :code:`.tex` document from somewhere, made a few modifications and want to compile it into a PDF file now. Of course you don't want to permanently install a plethora of software for that on your device too, as you just need to compile that single document.

If you have `Docker <https://www.docker.com/products/docker-desktop>`_ installed, I've got you covered. 

We use the following :code:`article.tex` file to check if compilation into a PDF file works:

.. code-block:: latex

   \documentclass{article}
   \begin{document}
   Hello World!
   \end{document}

Simply navigate to the folder where you've put your :code:`article.tex` file and execute the following command in your terminal:

.. code-block:: bash

   #> docker run --rm -v $(pwd):/docs authsec/sphinx pdflatex article.tex

.. note:: If you're running Windows the command is basically the same, but you need to replace :code:`$(pwd)` with :code:`${PWD}` for the above command to work.

This command generates an :code:`article.pdf` file with the content "Hello World!"

For advanced use cases you can of course add additional :code:`pdflatex` command line flags like :code:`-interaction=batchmode`. Just append after the :code:`pdflatex` command as if you'd use the regular command.

.. code-block:: bash

   #> docker run --rm -v $(pwd):/docs authsec/sphinx pdflatex -interaction=batchmode article.tex

Cleaning Up
-----------

In order to remove the downloaded container from your system you can run:

.. code-block:: bash

   #> docker image rm authsec/sphinx

If you want to clean up everything docker, simply run:

.. code-block:: bash

   #> docker system prune

The docker image used to compile the LaTeX document, can also be used to compile sphinx projects or a pelican blog. You can find further information in my `Github Repository <https://github.com/authsec/sphinx>`_.