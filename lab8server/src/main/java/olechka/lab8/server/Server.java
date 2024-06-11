package olechka.lab8.server;

import olechka.lab8.commands.Command;
import olechka.lab8.exceptions.CommandExecutionException;
import olechka.lab8.exceptions.CommandFailureType;
import olechka.lab8.protocol.ProtocolMessage;
import olechka.lab8.protocol.ProtocolMessageType;
import olechka.lab8.server.models.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//чтобы закрыть сервер имлеентим интерфейс, иначе бы закрывали сервер в мэйн

public class Server implements AutoCloseable {
    // закрепляем за сервером порт 2226
    public static final int PORT = 2226;
    //закрепляем за сервером логгер. В моем случае используем Log4.2
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    //к нашему серверному приложению привязан сокет. Сокет слушает порт. При создании
    // сервера, соответсвенному к нему привязан один сокет, который будет слушать порт, закрепленный
    // за нашим приложением
    private final DatagramSocket socket;
    private final DatabaseState state;
    private final UserManager userManager;
    private final SessionManager sessionManager;
    private final ExecutorService executorService;

    public Server(SessionFactory sessionFactory) throws IOException {
        // на низком уровне, когда по сетевой карте к ядру придет пакет с написанным на нем портом,
        // ядро посмотрит закреплен ли за таким портом какой-нибудь сокет.
        // если есть сокет с таким портом, то ядро положит в буфер сокета данные (этот пакет как раз).
        // программы вызывает из этого буфера сокет.ресив и эти данные получает
        // если в буфере данных нет, то программа начинает ждать, когда эти данные появятся
        // то есть тут одновременно работает и ядро - записывая данные в буфер, и программа, которая забирает данные из буфера.
        // когда мы наоборот пытаемся что-то отправить по сети, то программа записывает данные в буфер отправки.
        socket = new DatagramSocket(PORT);
        state = new DatabaseState(sessionFactory);
        userManager = new UserManager(sessionFactory);
        sessionManager = new SessionManager();
        // добавили в коллекцию все элементы с бд

//        создание фиксированного числа потоков, а конкретно в количество 4х штук.
        executorService = Executors.newFixedThreadPool(4);
    }


    //модуль приема подключений
    public void run() throws IOException {
        // это чтобы когда приложение считало с буфера обмена у сокета данные оно могло куда-то то их положить
        byte[] bytes = new byte[10_000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        // из сокета можем читать данные, читаем запрос, который придет
        // socket.bind(new InetSocketAddress("0.0.0.0", 2666));
        logger.info("Сервер успешно запущен. Друзья, можете подключаться по адресу {}", socket.getLocalSocketAddress());
        for (int i = 0; i < 4; i++) {
//            вызов FixedPool для чтения запроса
            executorService.submit(() -> {
                while (true) {
                    // на этом этапе получаем из буфера обмена данные и кладем их в наш пакет
                    socket.receive(packet);
                    // теперь из пакета получили данные
                    try {
                        byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                        ProtocolMessage message = new ProtocolMessage();
                        message.decode(data);
                        // так как у нас UDP, у нас нет соединения. мы тут получаем у нашего пакета айпи отправителя и порт отправители
                        processRequest(message, packet.getSocketAddress());
                    } catch (Exception e) {
                        logger.warn("случилась СТРАШНАЯ ошибка жестб (клиент {})", packet.getSocketAddress(), e);
                    }
                }
            });
        }
    }

    public void waitForever() throws InterruptedException {
        executorService.awaitTermination(10000L, TimeUnit.DAYS);
    }

    //модуль обработки команд
    //обработка запроса включает отправку ответа
    private void processRequest(ProtocolMessage message, SocketAddress socketAddress) {
        RemoteClient remoteClient = sessionManager.getClientSession(socketAddress);
        boolean result = remoteClient.canExecuteCommandOfOrder(message.getRequestOrder());
        if (result) {
            switch (message.getType()) {
                case LOGIN -> {
                    User user = userManager.checkUser(message.getLogin(), message.getPassword());
                    if (user != null) {
                        sendResponse(ProtocolMessageType.SERVER_STATE, state.getMemoryState(), remoteClient);
                    } else {
                        sendResponse(ProtocolMessageType.SERVER_ERROR, CommandFailureType.USER_LOGIN_FAILED, remoteClient);
                    }
                }

                case REGISTER -> {
                    User user = userManager.register(message.getLogin(), message.getPassword());
                    if (user != null) {
                        sendResponse(ProtocolMessageType.SERVER_STATE, state.getMemoryState(), remoteClient);
                    } else {
                        sendResponse(ProtocolMessageType.SERVER_ERROR, CommandFailureType.USER_REGISTER_FAILED, remoteClient);
                    }
                }

                case EXECUTE_COMMAND -> {
                    User user = userManager.checkUser(message.getLogin(), message.getPassword());
                    if (user != null) {
                        Command command = message.getCommand();
                        command.setUserId(user.getId());
                        try {
                            command.execute(state);
                            Collection<RemoteClient> remoteClients = sessionManager.getClients();
                            for (RemoteClient client : remoteClients) {
                                sendResponse(ProtocolMessageType.EXECUTE_COMMAND, command, client);
                            }
                        } catch (CommandExecutionException e) {
                            sendResponse(ProtocolMessageType.SERVER_ERROR, e.getCommandFailureType(), remoteClient);
                        }
                    }
                }
            }

        }
    }

    private void sendResponse(ProtocolMessageType type, Object object, RemoteClient remoteClient) {
        ProtocolMessage message = new ProtocolMessage(type, object, remoteClient.getLastRequestOrder());
        sendResponse(message, remoteClient.getAddress());
    }

    // создание потока для отправки ответа
    private void sendResponse(ProtocolMessage message, SocketAddress socketAddress) {
        try {
            byte[] data = message.encode();
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, socketAddress);
            socket.send(datagramPacket);
        } catch (IOException e) {
            logger.warn("Не удалось отправить ответ", e);
        }
    }

    //это пришлось сделать так как перестали использовать try with resourses, а наш сокет надо закрыть
    @Override
    public void close() throws Exception {
        socket.close();
    }

    //ненужный больше код так как после добавления объекта будем сразу добавлять его в бд и не нужно ничег
    // сохранять
    // public void save() {
    // Command command = new SaveCommand();
    // stateManager.executeOnAllStates(command);
    // logger.info("Все состояния сохранены");
    // }
}
