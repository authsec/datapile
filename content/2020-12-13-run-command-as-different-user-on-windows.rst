Run command as different user on Windows
########################################

:date: 2020-12-13T22:33:44+01:00
:tags: Windows 10, Powershell
:category: Fundamentals
:summary: How to get the IP address from host group.

There are times when you do want to run a command on Windows, such as for example a powershell window with elevated privileges. There may also be a reason that you want to run the command as an administrative user that is not named 'Administrator'.

You can do this by spawning a non-privileged powershell window first. From there enter the command below, and, after entering your password, you should have an administrative terminal.

.. code-block:: guess

   PS C:\Users\you> runas /user:theAdminUsername powershell

This way you can spawn a "root" Terminal in Windows as you would do with e.g. :code:`sudo su -` on a Linux machine.