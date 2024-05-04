package olechka.lab6.client;

import olechka.lab6.CommandFactory;
import olechka.lab6.commands.Command;
import olechka.lab6.interaction.ArgumentException;
import olechka.lab6.interaction.Console;
import olechka.lab6.interaction.InterationClosedException;
import olechka.lab6.protocol.ProtocolMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.UUID;

public class Client implements AutoCloseable {
    private final DatagramChannel channel;
    private final UUID clientId;
    private InetSocketAddress serverAddress;
    private final Selector selector;
    private int requestOrder;
    public Client(String ip) throws IOException {
//        создаем канал
        channel = DatagramChannel.open();
        clientId = new UUID(0, 1);
//        клиенту чтобы отправлять данные нужно знать точный адрес. а вот для сервера мы сказали, что мо
        serverAddress = new InetSocketAddress(ip, 2226);
//        создаст селектор конкретно для моей ОС
        selector = Selector.open();
    }

    public void run() throws IOException, InterationClosedException, ClassNotFoundException {
//        принимает буфер - массив байт, читает данные. но возвращает адрес, с которого эти данные пришли.
//        у него нет публичного конструктора поэтому создаем через метод. капасити - вместимость
//        также есть указатель на определенный байт и лимит.
//        привязали канал к случайному порту
        channel.socket().bind(null);
//        выключает блокирующий режим и переключает в неблокирующий
        channel.configureBlocking(false);
//        региструем данный  канал для селектора
        channel.register(selector, SelectionKey.OP_READ);
        ByteBuffer buffer = ByteBuffer.allocate(10_000);
//        у буфера есть позиция. в начале она равна 0. при добавлении байта она смещается.
//        buffer.put((byte) 1);
//        System.out.println(buffer.position());
//        channel.receive()
        System.out.println("Здравствуйте! Вы находитесь в программе Управление коллекцией.");
        Console console = new Console();
        CommandFactory commandFactory = new CommandFactory();
//            цикл будет работать пока exitrequested false
        while (!console.isInputClosed()) {
            String commandName = console.next();
            Command command = commandFactory.createCommand(commandName);
            if (command == null) {
                System.out.println("Введена несуществующая команда/аргумент " + commandName);
                continue;
            }
            try {
//                после выполнения каждой команды будет увеличиваться номер следующего запроса.
                command.parse(console);
            } catch (ArgumentException exception) {
                System.out.println("Данная команда требует корректный аргумент. Подробнее смотри, используя, команду 'help'");
                continue;
            }

            sendCommand(command);

            SocketAddress address = null;
//            цикл будет выполняться до тех пор пока у нас не совпадают адреса
            while (!serverAddress.equals(address)) {
                buffer.clear();
//                сейчас блокирующий режим, то есть recieve будет ждать если очередь пуста
//                с помощью кода ниже оптимизировали неблокирующий режим
                int selectedCount = selector.select(5000);
                if (selectedCount <= 0) {
                    System.out.println("Кажется, сервер не отвечает :( Вы можете подождать еще немного или прервать выполнение клиентской программы.");
                    sendCommand(command);
                }
                address = channel.receive(buffer);
            }
//декодинг данных.
            byte[] data = Arrays.copyOf(buffer.array(), buffer.position());
            ProtocolMessage<Command.Result> message = new ProtocolMessage<>();
            message.decode(data);

            System.out.println(message.getObject().getMessage());
            if (message.isExitRequested()) {
                break;
            }

//                Command.Result result = command.execute(state);
//                System.out.println(result.getMessage());
            requestOrder++;
        }
        System.out.println("Спасибо за использование. Обязательно подпишитесь на мой тгк https://t.me/krinzh_umer");
    }

    private void sendCommand(Command command) throws IOException {
//        потому что мы с сервера только отправляем это сообщение но не обрабатываем
        ProtocolMessage<Command> message = new ProtocolMessage<>(command, clientId, false, requestOrder);
        byte[] data = message.encode();
        channel.send(ByteBuffer.wrap(data), serverAddress);
    }
//    byte[] data = message.encode();
//    DatagramPacket datagramPacket = new DatagramPacket(data, data.length, socketAddress);
//        socket.send(datagramPacket);

    @Override
    public void close() throws Exception {
        channel.close();
    }
}
