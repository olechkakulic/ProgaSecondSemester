package olechka.lab7.client;

import olechka.lab7.CommandFactory;
import olechka.lab7.commands.Command;
import olechka.lab7.commands.LoginCommand;
import olechka.lab7.commands.RegisterCommand;
import olechka.lab7.interaction.ArgumentException;
import olechka.lab7.interaction.Console;
import olechka.lab7.interaction.InterationClosedException;
import olechka.lab7.parsing.ObjectParser;
import olechka.lab7.protocol.ProtocolMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;

public class Client implements AutoCloseable {
    private final DatagramChannel channel;
    private final ByteBuffer buffer;
    private String login;
    private String password;
    private InetSocketAddress serverAddress;
    private final Selector selector;
    private int requestOrder;
    private boolean isExitRequested;

    public Client(String ip) throws IOException {
        //        создаем канал
        channel = DatagramChannel.open();
        //        клиенту чтобы отправлять данные нужно знать точный адрес. а вот для сервера мы сказали, что мо
        serverAddress = new InetSocketAddress(ip, 2226);
        //        создаст селектор конкретно для моей ОС
        selector = Selector.open();
        //        у буфера есть позиция. в начале она равна 0. при добавлении байта она смещается.
        //        buffer.put((byte) 1);
        //        System.out.println(buffer.position());
        //        channel.receive()
        buffer = ByteBuffer.allocate(10_000);
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

        System.out.println("Здравствуйте! Вы находитесь в программе Управление коллекцией.");
        Console console = new Console();
        askCredentials(console);
        CommandFactory commandFactory = new CommandFactory();
        //            цикл будет работать пока exitrequested false
        while (!console.isInputClosed() && !isExitRequested) {
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

            executeCommand(command);
        }
        System.out.println("Спасибо за использование. Обязательно подпишитесь на мой тгк https://t.me/krinzh_umer");
    }

    private void askCredentials(Console console) throws IOException, ClassNotFoundException {
        while (true) {
            try {
                console.getOut().println("Есть ли у вас учетная запись?");
                Boolean answer = ObjectParser.createInteractive(Boolean.class, console, false);
                Command.Result commandResult;
                if (answer != null && answer) {
                    console.getOut().println("Введите логин: ");
                    login = console.nextLine();
                    console.getOut().println("Введите пароль: ");
                    password = console.nextLine();
                    commandResult = executeCommand(new LoginCommand());
                    // кря кря кря кря кря кря кря крякря кря кря кря
                } else {
                    console.getOut().println("Придумайте логин: ");
                    login = console.nextLine();
                    console.getOut().println("Придумайте пароль: ");
                    password = console.nextLine();
                    commandResult = executeCommand(new RegisterCommand());
                }
                if (commandResult.isSuccess()) {
                    break;
                }

            } catch (IllegalArgumentException e) {
                console.getOut().println("Кажется, я вас не понял. Ответьте, пожалуйста, да или нет.");
            }
        }

    }

    private Command.Result executeCommand(Command command) throws IOException, ClassNotFoundException {
        sendCommand(command);

        SocketAddress address = null;
        //            цикл будет выполняться до тех пор пока у нас не совпадают адреса
        while (!serverAddress.equals(address)) {
            buffer.clear();
            long time = System.currentTimeMillis();
            //                сейчас блокирующий режим, то есть recieve будет ждать если очередь пуста
            //                с помощью кода ниже оптимизировали неблокирующий режим
            int selectedCount = selector.select(5000);
            if (selectedCount <= 0 && (System.currentTimeMillis() - time) >= 5000) {
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

        isExitRequested = message.isExitRequested();
        requestOrder++;
        return message.getObject();
    }

    private void sendCommand(Command command) throws IOException {
        //        потому что мы с сервера только отправляем это сообщение но не обрабатываем
        ProtocolMessage<Command> message = new ProtocolMessage<>(command, login, password, false, requestOrder);
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
