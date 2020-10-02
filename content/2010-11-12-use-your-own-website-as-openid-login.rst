.. include:: roles.inc

Use your own website as OpenID login
####################################

:date: 2010-11-12 14:00:34 +0100
:modified: 2010-12-04 18:40
:tags: OpenID, Security
:category: Security
:summary: This article will show you how to use the OpenID delegation feature which allows you to use your own domain as an identifier for OpenID.

If you want to use OpenID as an authentication mechanism, but are
afraid of the fact that your OpenID provider may die in the future, or
you simply might want to use another provider in the future, you can
use a delegation model with OpenID.

A reason to change the OpenID provider might be, that your current
OpenID provider does not support an authentication mechanism you like
to, as for example the YubiKey or something like that.

Let's say you want to use http://xyz.example.com as your OpenID and
you want to use http://clavid.com as your Identity Provider, you have to
execute the following steps.

Create a (sub-)domain for example.com called xyz.
*************************************************

This can for example be done by adding another virtual host to your
apache configuration. The configuration might look like the following
one:

.. code-block:: apache

	<VirtualHost xx.xx.xx.xx:80>
		ServerAdmin webmaster@example.com
		ServerName xyz.example.com

		DocumentRoot /var/www/xyz/

		<Directory /var/www/xyz/>
			# Options Indexes MultiViews
			AllowOverride None
			Order allow,deny
			allow from all
		</Directory>

		ErrorLog /var/log/apache2/error_xyz.log

		# Possible values include: debug, info, notice, warn, error, crit,
		# alert, emerg.

		LogLevel warn
		CustomLog /var/log/apache2/access_xyz.log combined
	</VirtualHost>


Create a index.html file inside your document root
**************************************************

As a next step you have to create a index.html file in your document
root (in this case :file:`/var/www/xyz`) with the following content in order
for OpenID services being able to find your current identity provider:

.. code-block:: html

	<html>
	<head>
		<title>Jens' OpenID delegation page</title>
		<link rel="openid.server" href="http://www.clavid.com/provider/openid" />
		<link rel="openid2.provider" href="http://www.clavid.com/provider/openid" />
		<link rel="openid.delegate" href="http://jens.clavid.com" />
		<link rel="openid2.local_id" href="http://jens.clavid.com" />
	</head>
	<body>
		<h1>This page is used for OpenID delegation.</h1>
		For more information on OpenID either visit 
		<a href="http://openid.net">http://openid.net<a> or 
		<a href="http://clavid.com">http://clavid.com</a>
	</body>
	</html>


Ready
*****

From now on you can use http://xyz.example.com as your OpenID, which will, as currently configured, be redirecting to http://clavid.com where authentication will take place.


References
**********

You can read all about OpenID in the specification, but the part used for this example is available at:

http://openid.net/specs/openid-authentication-1_1.html#delegating_authentication

