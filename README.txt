RabbitMQ must be installed

To access the server externally, open port 4567 (SparkJava default http://sparkjava.com/documentation.html)
To use rabbitMQ externally, open port 5672 (rabbitMQ default https://www.rabbitmq.com/configure.html)

The main class is DistribullyController.
The server ip and port are defined in DistribullyModel. Currently the ip is set to localhost, so that it can run in every location, independent of router settings.

When running the application, ensure that the port you enter for your sockets is opened.
When running, make sure the firewall is not blocking the ports. Windows firewall has a tendency to do that.

--------

When using an external IP, make sure to use the included config file for RabbitMQ. The default config does not allow unauthorized connections from external ip's. (https://www.rabbitmq.com/access-control.html)
This config on windows should be placed in %APPDATA%/RabbitMQ. There will probably already be an example config there.
On windows, after updating the config, remove and install the service, then start both server and service. This is the only way to update the config in use on windows. (https://www.rabbitmq.com/configure.html) 