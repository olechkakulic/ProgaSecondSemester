package olechka.lab8.client;

import olechka.lab8.protocol.ProtocolMessage;
import olechka.lab8.protocol.ProtocolMessageType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class NetworkClient implements Runnable {
    private final DatagramChannel channel;
    private final Selector selector;
    private final Consumer<ProtocolMessage> messageConsumer;
    private String login;
    private String password;
    private InetSocketAddress serverAddress;
    private int requestOrder;

    public NetworkClient(Consumer<ProtocolMessage> messageConsumer) throws IOException {
        this.messageConsumer = messageConsumer;
        channel = DatagramChannel.open();
        selector = Selector.open();
        serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), 2226);
    }

    public void setLoginSettings(String login, String password, InetSocketAddress serverAddress) {
        this.login = login;
        this.password = password;
        this.serverAddress = serverAddress;
    }

    public void sendMessage(ProtocolMessageType type, Object object) throws IOException {
        ProtocolMessage message = new ProtocolMessage(type, object, requestOrder, login, password);
        byte[] data = message.encode();
        channel.send(ByteBuffer.wrap(data), serverAddress);
    }

    public void run() {
        try {
            channel.socket().bind(null);
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);

            ByteBuffer buffer = ByteBuffer.allocate(10000);

            while (!Thread.interrupted()) {
                SocketAddress address = null;
                //            цикл будет выполняться до тех пор пока у нас не совпадают адреса
                while (!Objects.equals(serverAddress, address)) {
                    buffer.clear();
                    selector.select(5000);
                    address = channel.receive(buffer);
                }
                // декодинг данных.
                byte[] data = Arrays.copyOf(buffer.array(), buffer.position());
                ProtocolMessage message = new ProtocolMessage();
                message.decode(data);
                // в этом классе мы не будем заниматься обработкой пакетов. он только для сетевого взаимодействия.
                messageConsumer.accept(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
