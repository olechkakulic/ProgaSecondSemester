package olechka.lab7.server;

import olechka.lab7.commands.Command;
import olechka.lab7.commands.RegisterCommand;
import olechka.lab7.protocol.ProtocolMessage;
import olechka.lab7.server.models.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Arrays;
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
            executorService.submit(() -> {
                while (true) {
                    // на этом этапе получаем из буфера обмена данные и кладем их в наш пакет
                    socket.receive(packet);
                    // теперь из пакета получили данные
                    try {
                        // фактически могли получить меньше байт, поэтому обрезаем до нужного кол-ва
                        // вытаскиваем массив с помощью getData
                        // эщкереееееееееееееееееееееееееееееееееееееееееееееееееееееееееееееееееее
                        byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                        ProtocolMessage<Command> message = new ProtocolMessage<>();
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
    private void processRequest(ProtocolMessage<Command> message, SocketAddress socketAddress) {
        // запись результата выполнения команды. тут соотвественно заканчивается
        // выполнение. получаем result.

        // boolean result1 = sessionManager.getClientSession().canExecuteCommandOfOrder(message.getRequestOrder());
        Thread thread = new Thread(() -> {
            boolean result = sessionManager.getClientSession(socketAddress).canExecuteCommandOfOrder(message.getRequestOrder());
            if (result) {
                Command command = message.getObject();
                Command.Result commandResult;
                if (command instanceof RegisterCommand) {
                    userManager.register(message.getLogin(), message.getPassword());
                }
                User user = userManager.checkUser(message.getLogin(), message.getPassword());
                if (user != null) {
                    synchronized (state) {
                        state.setCurrentUserId(user.getId());
                        // исполнение команды
                        commandResult = command.execute(state);
                        // добавили в историю
                        state.getCommandManager().addHistoryCommand(message.getObject());
                    }
                } else {
                    commandResult = Command.Result.error("У вас неправильный логин или пароль.");
                }

                // статус того, был ли совершен выход из программы
                ProtocolMessage<Command.Result> messageResult = new ProtocolMessage<>(commandResult, message.getLogin(), message.getPassword(), state.isExitRequested(), message.getRequestOrder());
                sendResponse(messageResult, socketAddress);
            }

        });
        thread.start();
    }

    private void sendResponse(ProtocolMessage<Command.Result> message, SocketAddress socketAddress) {
        Thread thread = new Thread(() -> {
            try {
                byte[] data = message.encode();
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length, socketAddress);
                socket.send(datagramPacket);
            } catch (IOException e) {
                logger.warn("Не удалось отправить ответ", e);
            }
        });
        thread.start();
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
