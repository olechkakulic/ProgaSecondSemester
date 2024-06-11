package olechka.lab8.protocol;

import lombok.Getter;
import olechka.lab8.State;
import olechka.lab8.commands.Command;
import olechka.lab8.exceptions.CommandFailureType;

import java.io.*;

// МОДУЛЬ ЧТЕНИЯ ЗАПРОСА !!DTO!! - ВИД удобный для передачи
//для любого запроса надо знать айди клиента чтобы его обработать.
//создание параметризированного класса.
//временный объект который существует только для обмена данных, а в стейте у нас хранится состояние клиента
public class ProtocolMessage {
    @Getter
    private ProtocolMessageType type;
    @Getter
    private Object object;
    @Getter
    private int requestOrder;
    @Getter
    private String login;
    @Getter
    private String password;

    public ProtocolMessage() {

    }

    public ProtocolMessage(ProtocolMessageType type, Object object, int requestOrder) {
        this.type = type;
        this.object = object;
        this.requestOrder = requestOrder;
        login = "";
        password = "";
    }

    public ProtocolMessage(ProtocolMessageType type, Object object, int requestOrder, String login, String password) {
        this(type, object, requestOrder);
        this.login = login;
        this.password = password;
    }

    //    декодировать
    public void decode(byte[] bytes) throws IOException, ClassNotFoundException {
//        сериализация. с помощью входящего потока, который принимает массив байтов.
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        type = ProtocolMessageType.values()[objectInputStream.readInt()];
        login = objectInputStream.readUTF();
        password = objectInputStream.readUTF();
//        читаем декодированный объект и записываем резы
        object = objectInputStream.readObject();
        requestOrder = objectInputStream.readInt();
    }

    //        закодировать
    public byte[] encode() throws IOException {
//        записываем все в байт потоки, потому что именно это требует от нас udppppppppppppppppppp
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            //            если бы сделали так, то записали бы сильно больше байт
//            objectOutputStream.writeObject(clientId);
//            записали по 8 байт
            objectOutputStream.writeInt(type.ordinal());
            objectOutputStream.writeUTF(login);
            objectOutputStream.writeUTF(password);
            objectOutputStream.writeObject(object);
            objectOutputStream.writeInt(requestOrder);
        }
//        вернется закодированный массив байтов
        return byteArrayOutputStream.toByteArray();
    }

    public Command getCommand() {
        if (object instanceof Command command) {
            return command;
        }
        // if (object instanceof Command) {
        //     return (Command) object;
        // }
        return null;
    }

    public State getState() {
        if (object instanceof State state) {
            return state;
        }
        return null;
    }

    public CommandFailureType getFailureType() {
        if (object instanceof CommandFailureType type) {
            return type;
        }
        return null;
    }

}
