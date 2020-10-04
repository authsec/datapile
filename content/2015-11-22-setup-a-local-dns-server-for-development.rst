Setup A Local DNS Server For Development
########################################

:date: 2015-11-22T18:02:00+01:00
:tags: DNS
:category: Infrastructure
:author: Jens Frey
:summary: How to set up a local DNS server for Development.

.. note:: This was implemented and tested with Mac OS 10.10.4 and Dnsmasq 2.75

When you develop a web site or when you want to address multiple virtual machines by name on your local machine, you need to find a solution to implement this.

You can either input all your host names into your local :code:`/etc/resolv.conf` which may not result in a managable scenario after some time, or you can just install :code:`Dnsmasq` which is a local DNS responder that allows you to manage your own custom domain or extend the dns domain name space of another service.

You can install :code:`Dnsmasq` from :code:`ports` or probably :code:`homebrew` if you're on a Mac or get it from your local packet manager on your favourite Linux distribution.

After the installation just make sure that you are pointing you operating systems DNS resolution to your local host instance.

To resolve the usual internet domains via dnsmasq as well be sure to include your official DNS server in the configration.

The Dnsmasq configuration file can be found at :code:`/opt/local/etc/dnsmasq.conf` if you installed from ports. My configuration file is somewhat along these lines.

You probably want to use sudo to change this file for editing like for example so :code:`sudo vim /opt/local/etc/dnsmasq.conf`.

.. code-block:: dnsmasq

   resolv-file=/etc/resolv.conf
   # My local IP address
   server=192.168.221.1
   # Both are googles DNS servers
   server=8.8.8.8
   server=8.8.4.4

   # here go the IP addresses you want to remap or setup as new
   address=/alpha1.cluster.local.domain/172.16.232.135
   address=/red.alpha1.cluster.local.domain/172.16.232.141
   address=/black.alpha1.cluster.local.domain/172.16.232.141
   address=/green.alpha2.cluster.local.domain/172.16.232.132


After you have changed the configuration you probably want to flush the DNS cache and reload the service by issuing the following commands:

.. code-block:: shell

   sudo port unload dnsmasq && \
   sudo killall -HUP mDNSResponder && \
   sudo port load dnsmasq
