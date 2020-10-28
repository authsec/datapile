Fixing Vagrant and VMware Fusion
################################

:date: 2020-10-21T21:56:00+01:00
:tags: Vagrant, VMware Fusion
:category: Infrastructure
:summary: This post shows you how you can fix the port forward conflict on host port 2222.

For some reason `Vagrant <https://www.vagrantup.com/>`_ is sometimes unable to start the virtual machine and aborts with an error message like:

.. code-block:: bash

   Some of the defined forwarded ports would collide with existing
   forwarded ports on VMware network devices. This can be due to
   existing Vagrant-managed VMware machines, or due to manually
   configured port forwarding with VMware. Please fix the following
   port collisions and try again:

   2222

If you run into that problem there are a few things that might help you fix them. You can for example shut down your :code:`vagrant` machine and remove it from the virtual machine "Virtual Machines" sidebar in the Fusion UI. If you're lucky that will already solve your problem.

Some other time the following series of commands seems to help solve the issue at hand:

.. code-block:: bash

   $#> vagrant halt && \
   sudo rm -f /opt/vagrant-vmware-desktop/settings/nat.json && \
   sudo killall vagrant-vmware-utility && \
   vagrant up

If that doesn't solve you're problem, you can try a combination of both approaches. If this doesn't work you have to continue googling ;)