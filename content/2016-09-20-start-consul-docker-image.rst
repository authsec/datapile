How To Start A Consul Docker Image
##################################

:date: 2016-09-20T21:48:12+01:00
:tags: Consul, Database
:category: Infrastructure
:author: Jens Frey
:summary: How to start a local consul server.

If you quickly have to start a `Consul <https://www.consul.io/>`_  docker image you can use the following command:

.. code-block:: bash

   docker run -p 8400:8400 -p 127.0.0.1:8500:8500 -p 8600:8600/udp -h node1 consul agent -dev -client 0.0.0.0

This is especially useful for development purposes and quick tests and should not be used in a productive environment.
