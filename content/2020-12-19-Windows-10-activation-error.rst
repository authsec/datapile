Windows 10 Pro activation error 0x8004FE33
##########################################

:date: 2020-12-19T21:22:23+01:00
:tags: Windows 10, Proxy
:category: Infrastructure
:summary: How to solve Windows 10 activation error 0x8004FE33

If you recently installed a proxy server yourself (like e.g. `my squid proxy server <https://github.com/authsec/squid>`_) or your organization set up a (new) proxy server for you, you might run into issues when trying to activate Windows.

The error I ran into was `Error code: 0x8004FE33 Description: Acquisition of Secure Processor Certificate failed.`, which can easily be resolved by stopping to intercept (ssl_bump) traffic to certain Microsoft domains. If you are using my proxy solution, you can create a new acl for Microsofts' activation servers and exclude them from being bumped like so:

.. code-block:: squid

   # Microsoft activation services (partially needed for license activation)
   acl serverIsMicrosoftActivation ssl::server_name .go.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .login.live.com
   acl serverIsMicrosoftActivation ssl::server_name .activation.sls.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .validation.sls.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .activation-v2.sls.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .validation-v2.sls.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .displaycatalog.mp.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .licensing.mp.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .purchase.mp.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .displaycatalog.md.mp.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .licensing.md.mp.microsoft.com
   acl serverIsMicrosoftActivation ssl::server_name .purchase.md.mp.microsoft.com

   # Listen on 4128 for SSL bumping
   http_port 4128 ssl-bump generate-host-certificates=on dynamic_cert_mem_cache_size=4MB cert=/etc/ssl/proxy/certs/proxy-ca.crt key=/etc/ssl/proxy/private/proxy-ca.key
   acl step2 at_step SslBump2
   ssl_bump peek step2
   ssl_bump bump all !serverIsBank !serverIsMicrosoftActivation
   ssl_bump splice all

For more information see the `Microsoft Support Site <https://support.microsoft.com/en-us/help/921471/windows-activation-or-validation-fails-with-error-code-0x8004fe33>`_.