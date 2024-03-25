package olechka.lab6.server;

import olechka.lab6.State;
import olechka.lab6.commands.Command;
import olechka.lab6.commands.SaveCommand;
import olechka.lab6.protocol.ProtocolMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;

public class Server implements AutoCloseable {
    public static final int PORT = 2226;

    private static final Logger logger = LogManager.getLogger(Server.class);

    private final DatagramSocket socket;
    private final StateManager stateManager;

    public Server() throws SocketException {
        socket = new DatagramSocket(PORT);
        stateManager = new StateManager();
    }

    public void run() throws IOException {
        byte[] bytes = new byte[10_000];
//        следующие три строчки добавлены вместо строки ниже одной
//        DatagramSocket socket = new DatagramSocket(null);
//        InetSocketAddress address = new InetSocketAddress("0.0.0.0", 2222);
//        socket.bind(address);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
//        из сокета можем читать данные, читаем запрос, который придет
//        socket.bind(new InetSocketAddress("0.0.0.0", 2666));
        logger.atInfo().log("Сервер успешно запущен. Друзья, можете подключаться по адресу {}", socket.getLocalSocketAddress());
        while (true) {
//                в пакэт сокет запишет данные
            socket.receive(packet);
//                теперь из пакета получили данные
            try {
                byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                ProtocolMessage<Command> message = new ProtocolMessage<>();
                message.decode(data);
                processRequest(message, packet.getSocketAddress());
            } catch (Exception e) {
                logger.atWarn().withThrowable(e).log("случилась СТРАШНАЯ ошибка жестб (клиент {})", packet.getSocketAddress());
            }
        }
    }

    //обработка запроса включает отправку ответа
    private void processRequest(ProtocolMessage<Command> message, SocketAddress socketAddress) throws IOException {
        State state = stateManager.getClientState(message.getClientId());
        Command.Result result = message.getObject().execute(state);
//        добавили в историю
        state.getCommandManager().addHistoryCommand(message.getObject());
        ProtocolMessage<Command.Result> messageResult = new ProtocolMessage<>(result, message.getClientId(), state.isExitRequested());
        sendResponse(messageResult, socketAddress);
    }

    //отправляем ответ
    private void sendResponse(ProtocolMessage<Command.Result> message, SocketAddress socketAddress) throws IOException {
        byte[] data = message.encode();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, socketAddress);
        socket.send(datagramPacket);
    }

    //это пришлось сделать так как перестали использовать try with resourses, а наш сокет надо закрыть
    @Override
    public void close() throws Exception {
        socket.close();
    }

    public void save() {
        Command command = new SaveCommand();
        stateManager.executeOnAllStates(command);
        logger.atInfo().log("Все состояния сохранены");
    }
}
