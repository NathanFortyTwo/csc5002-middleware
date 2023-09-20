
API of the Vlibtour Lobby Room microservice
====

This module defines the API of the lobby room microservice.

Class `VLibTourLobbyService` is a proposal and you are free to modify it.

The `InAMQPPartException` exception class is proposed in order to manage problems when communicating with the RabbitMQ server.
Recall that, in the document that describes the software architecture of the case study, the lobby room is called using the AMQP protocol.
This cannot be changed, i.e. there is no technological choice here.
