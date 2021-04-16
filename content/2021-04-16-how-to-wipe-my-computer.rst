How to wipe my hard drive
#########################

:date: 2021-04-16T13:03:43+01:00
:tags: Linux, Windows 
:category: Fundamentals
:summary: How to wipe all data from my hard drive.

.. warning:: If you are using these commands your files will be irrevocably deleted! So make sure you understand what you are doing!

To securely delete all your data from your hard drive, you can boot your computer with a Linux distribution either from a CD or DVD drive or a simple USB stick. You can find a how to on creating such a USB stick `here <{filename}/2021-04-15-boot-linux-from-usb-stick.rst>`_.

Once you've booted up your computer with the thumb drive, you need to open a terminal and enter the following command:

.. code-block:: bash

   #> shred -v /dev/sdX

Where :code:`X` is the drive identifier. Your first hard drive is likely to be :code:`/dev/sda`, but your mileage may vary. If you are unsure that this is the correct drive, you can execute

.. code-block:: bash

   #> cfdisk /dev/sda

and see from the size of the drive and partitions if this is the expected drive. 
