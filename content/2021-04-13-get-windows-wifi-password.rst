Get Windows WiFi Password
#########################

:date: 2021-04-13T13:03:43+01:00
:tags: Windows, Security
:category: Infrastructure
:summary: How to show your Windows Wi-Fi password.

If you forgot your Wi-Fi password, but Windows still connects to your Wi-Fi, you can execute the following command in a command prompt to retrieve it in plain text:

.. code-block:: bash

   #> netsh wlan show profile name=profilename key=clear

To see which WLAN profiles (SSIDs/network names) exist, simply execute:

.. code-block:: bash

   #> netsh wlan show profiles