Customizing Emacs
#################

:date: 2010-11-04 14:00:34 +0100
:tags: Editor, Emacs
:category: Infrastructure
:author: Jens Frey
:summary: This article will how to customize the emacs editor.

.. note:: This tutorial was tested with GNU Emacs 23.0.91.1

Some of you might be forced to spent nearly the whole day in a text editor like e.g. emacs to do their work. This usually is especially true for developers, that have to write code the whole day and therefore are forced to look into their editor the whole day. So why not customize it a little, so work might be more pleasant in the end?

This article will show you how you can customize emacs to hopefully suit your needs. Since I am using Ubuntu, which is a Debian based distribution, the installation of packages might vary for your distribution.

Prerequisites
*************

In order for this customization to work, you need to get some things first. This includes the development version of emacs, which in my distribution is marked as emacs-snapshot, and the Terminus font, which is according to many users *the* font if you have to work long on your computer. That's what I can say too, the font is very pleasant on the eyes.

So, be sure to have the following stuff available:

* `The Terminus font <http://www.is-vn.bg/hamster/>`_
* `Emacs <http://www.gnu.org/software/emacs/emacs-faq.html#Installing-Emacs>`_

For the development (snapshot) version of emacs you'd probably have to check out the CVS repository, if you're lucky, version 23 is released when you read this ;).

On an Ubuntu based installation, these packages are quite easy to get, simply enter the following commands in your terminal.

.. code-block:: bash

   j@sigusr1:$ sudo aptitude install \
      console-terminus xfonts-terminus \
      xfonts-terminus-dos xfonts-terminus-oblique \
      emacs-snapshot emacs-snapshot-gtk


If you do have the non-snapshot edition of emacs already installed on
your system, you might want to update your default, be executing the
following commands and select the snapshot as the default being
started when you type `emacs`:

.. code-block:: bash

   $ sudo update-alternatives --config emacs
   $ sudo update-alternatives --config emacsclient

You should now be able to type :code:`emacs` into your terminal and the emacs-snapshot will be started.

Color Themes
************

I like colors, and I think the basic color theme that comes with emacs is not very nice. So I decided to install some color themes. Luckily these color themes can be easily installed on Ubuntu. If you are using another distribution which might not have support for that, you might want to try `downloading and installing it manually <http://www.nongnu.org/color-theme/>`_. For the automatic version, simply do:

.. code-block:: bash

   $ sudo apt-get install emacs-goodies-el

After the installation completes, you should be able to start emacs and select a color theme like "Subtle Hacker" or "Gnome 2" with :code:`M-x color-theme-select`. The "Save customization" feature in the snapshot I am using (GNU Emacs 23.0.91.1) does not seem to work unfortunately. There is one "trick" though that might work; if the customization file does not yet exist, saving seems to work.

Customization Files
*******************

If you externalize your configuration into a -custom.el file, you end up with two files, the .emacs and the .emacs-custom.el file (or whatever you name it). My files look as follows.

References
**********

* http://www.gnu.org/software/emacs/emacs-faq.html#Installing-Emacs
* http://www.is-vn.bg/hamster
* http://www.nongnu.org/color-theme
* http://homepages.inf.ed.ac.uk/s0243221/emacs
* http://amrmostafa.org/bearable-emacs-recipe
* http://peadrop.com/blog/2007/01/06/pretty-emacs
* http://fractal.csie.org/~eric/wiki/Terminus_font
* http://copyleft.free.fr/wordpress/index.php/2009/04/24/nice-fonts-with-emacs-23-snapshot
