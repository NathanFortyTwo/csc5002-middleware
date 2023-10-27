/**
This file is part of the course CSC5002.

Copyright (C) 2017-2023 Télécom SudParis

This is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This software platform is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the muDEBS platform. If not, see <http://www.gnu.org/licenses/>.

Initial developer(s): Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_tourist_application.lobby_room_proxy;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.jsonrpc.JsonRpcClient;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;

import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;

/**
 * The AMQP/RabbitMQ Proxy (for clients) of the Lobby Room Server.
 * 
 * @author Denis Conan
 */
public final class VLibTourLobbyRoomProxy {
    /**
    * the connection to the RabbitMQ broker.
    */
    private Connection connection;
    /**
     * the channel for producing and consuming.
     */
    private Channel channel;
    /**
     * the RabbitMQ JSON RPC client.
     */
    private JsonRpcClient jsonRpcClient;

    /**
     * The service
     */
    public VLibTourLobbyService service;

    public VLibTourLobbyRoomProxy() throws IOException, JsonRpcException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        while (connection == null) {
            try {
                connection = factory.newConnection();
            } catch (IOException e) {
                System.out.println(" [x] Cannot connect to the AMQP broker");
                System.out.println(" [x] Retrying in 5 seconds");
                Thread.sleep(5000);
            }
        }
        connection = factory.newConnection();
        channel = connection.createChannel();
        jsonRpcClient = new JsonRpcClient(channel, VLibTourLobbyService.EXCHANGE_NAME,
                VLibTourLobbyService.BINDING_KEY);
        service = jsonRpcClient.createProxy(VLibTourLobbyService.class);
    }

    /**
    * closes the JSON RPC client, the channel and the connection with the broker.
    * 
    * @throws IOException
    *             communication problem.
    * @throws TimeoutException
    *             broker to long to communicate with.
    */
    public void close() throws IOException, TimeoutException {
        jsonRpcClient.close();
        if (channel != null) {
            channel.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
