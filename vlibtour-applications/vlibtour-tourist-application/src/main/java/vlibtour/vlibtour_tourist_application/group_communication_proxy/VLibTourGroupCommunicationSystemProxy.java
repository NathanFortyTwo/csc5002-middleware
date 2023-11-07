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
package vlibtour.vlibtour_tourist_application.group_communication_proxy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

/**
 * The AMQP/RabbitMQ Proxy (for clients) of the VLibTour Group Communication
 * System.
 * 
 * @author Denis Conan
 */
public class VLibTourGroupCommunicationSystemProxy {
    private String topic;
    private Connection connection;
    private Channel channel;
    private String userRoutingKey;
    private Consumer consumer;
    private String queueName;

    public static String BROADCAST_POSITION = "all.position";

    public VLibTourGroupCommunicationSystemProxy(final String topic, final String userRoutingKey, String uri)
            throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException,
            InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
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
        channel.exchangeDeclare(topic, BuiltinExchangeType.TOPIC);
        this.topic = topic;
        this.userRoutingKey = userRoutingKey;
    }

    public void publish(final String message, final String specificRoutingKey) throws IOException {
        /**
         * specificRoutingKey could be either "all.position" or "userId.sms"
         */
        channel.basicPublish(topic, userRoutingKey + "." + specificRoutingKey, null, message.getBytes());
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    public void setConsumer(Consumer consumer) throws IOException {
        this.queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, topic, "*.all.#");
        channel.queueBind(queueName, topic, "*." + userRoutingKey + ".#"); // SMS for example
        this.consumer = consumer;
    }

    public void startConsuming() throws IOException {
        channel.basicConsume(queueName, true, consumer);
    }

    public Channel getChannel() {
        return channel;
    }
}
