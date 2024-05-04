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
//чтобы закрыть сервер имлеентим интерфейс, иначе бы закрывали сервер в мэйн

public class Server implements AutoCloseable {
    //    закрепляем за сервером порт 2226
    public static final int PORT = 2226;
    //закрепляем за сервером логгер. В моем случае используем Log4.2
    private static final Logger logger = LogManager.getLogger(Server.class);
    //к нашему серверному приложению привязан сокет. Сокет слушает порт. При создании
//    сервера, соответсвенному к нему привязан один сокет, который будет слушать порт, закрепленный
//    за нашим приложением
    private final DatagramSocket socket;
    private final StateManager stateManager;
    private final SessionManager sessionManager;

    public Server() throws SocketException {
//        на низком уровне, когда по сетевой карте к ядру придет пакет с написанным на нем портом,
//        ядро посмотрит закреплен ли за таким портом какой-нибудь сокет.
//        если есть сокет с таким портом, то ядро положит в буфер сокета данные (этот пакет как раз).
//        программы вызывает из этого буфера сокет.ресив и эти данные получает
//        если в буфере данных нет, то программа начинает ждать, когда эти данные появятся
//        то есть тут одновременно работает и ядро - записывая данные в буфер, и программа, которая забирает данные из буфера.
//        когда мы наоборот пытаемся что-то отправить по сети, то программа записывает данные в буфер отправки.
        socket = new DatagramSocket(PORT);
        stateManager = new StateManager();
        sessionManager = new SessionManager();
    }

    //модуль приема подключений
    public void run() throws IOException {
//        это чтобы когда приложение считало с буфера обмена у сокета данные оно могло куда-то то их положить
        byte[] bytes = new byte[10_000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
//        из сокета можем читать данные, читаем запрос, который придет
//        socket.bind(new InetSocketAddress("0.0.0.0", 2666));
        logger.atInfo().log("Сервер успешно запущен. Друзья, можете подключаться по адресу {}", socket.getLocalSocketAddress());
        while (true) {
//                на этом этапе получаем из буфера обмена данные и кладем их в наш пакет
            socket.receive(packet);
//                теперь из пакета получили данные
            try {
//                фактически могли получить меньше байт, поэтому обрезаем до нужного кол-ва
//                вытаскиваем массив с помощью getData
//                эщкереееееееееееееееееееееееееееееееееееееееееееееееееееееееееееееееееее
                byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                ProtocolMessage<Command> message = new ProtocolMessage<>();
                message.decode(data);
//                так как у нас UDP, у нас нет соединения. мы тут получаем у нашего пакета айпи отправителя и порт отправители
                processRequest(message, packet.getSocketAddress());
            } catch (Exception e) {
                logger.atWarn().withThrowable(e).log("случилась СТРАШНАЯ ошибка жестб (клиент {})", packet.getSocketAddress());
            }
        }
    }

    //модуль обработки команд
//обработка запроса включает отправку ответа
    private void processRequest(ProtocolMessage<Command> message, SocketAddress socketAddress) throws IOException {
        State state = stateManager.getClientState(message.getClientId());
//        запись результата выполнения команды. тут соотвественно заканчивается
//        выполнение. получаем result.

//        boolean result1 = sessionManager.getClientSession().canExecuteCommandOfOrder(message.getRequestOrder());
        boolean result1 = sessionManager.getClientSession(socketAddress).canExecuteCommandOfOrder(message.getRequestOrder());
        if (result1) {
//            исполнение команды
            Command.Result result = message.getObject().execute(state);
            //        добавили в историю
            state.getCommandManager().addHistoryCommand(message.getObject());
//        статус того, был ли совершен выход из программы
            ProtocolMessage<Command.Result> messageResult = new ProtocolMessage<>(result, message.getClientId(), state.isExitRequested(), message.getRequestOrder());
            sendResponse(messageResult, socketAddress);
        }
    }

    //открыты два клиента, мы начинаем
//    редактировать в одном клиенте имеющийся элемент.
//    в другом клиенте удаляем этот элемент либо очищаем коллекцию
//    выходим
//    в первом клиенте пытаемся продолжить редактирование
//    сервер упал.
    //отправляем ответ
//
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

    //убираем команду save с клиентской части программы (в моем случае с общей части программы), при выполнении выхода с клиента
//    с помощью команды exit будут сохранены все состояния. так же при завершении работы сервера будут сохранены все состояния.
    public void save() {
        Command command = new SaveCommand();
        stateManager.executeOnAllStates(command);
        logger.atInfo().log("Все состояния сохранены");
    }
}
