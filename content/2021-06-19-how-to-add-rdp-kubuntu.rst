How to setup RDP for Kubuntu
############################

:date: 2021-06-19T16:50:00+01:00
:tags: Linux, Windows, RDP
:category: Fundamentals
:summary: Setup Kubuntu RDP server.

If you do have problems setting up a RDP remote connection from Windows or Mac into `Kubuntu <https://kubuntu.org/>`_, this might help you!

You have probably figured out that you need to install :code:`xrdp` so that you can use the remote desktop client to get into your Kubuntu installation. But now you get `login failed for display 0` or you can see the KDE plasma desktop logo for a little bit but end up with a black screen and a cursor?

The first thing may be that you just entered the wrong password. However, if you're sure you have the correct username and password, then you might just need to insert the following three lines into the :code:`/etc/xrdp/startwm.sh` script right before the :code:`test -x /etc/X11/Xsession && exec /etc/X11/Xsession` line.

.. code-block:: bash

   unset DBUS_SESSION_BUS_ADDRESS
   unset XDG_RUNTIME_DIR
   . $HOME/.profile

Once that is done you need to restart the xrdp service, so the changes are applied.

.. code-block:: bash

   $#> sudo service xrdp restart

Once that is done you can use your RDP client to connect to the machine.

All in One
----------

Now that you understand how it works you probably want to run it all in one go:

.. code-block:: bash
  
   $#> sudo sed -i '/^test -x \/etc\/X11\/Xsession.*/i unset DBUS_SESSION_BUS_ADDRESS\nunset XDG_RUNTIME_DIR\n. $HOME/.profile\n' /etc/xrdp/startwm.sh && sudo service xrdp restart
   $#>
    

