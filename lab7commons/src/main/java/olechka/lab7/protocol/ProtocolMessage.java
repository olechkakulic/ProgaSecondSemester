package olechka.lab7.protocol;

import lombok.Getter;

import java.io.*;

// МОДУЛЬ ЧТЕНИЯ ЗАПРОСА !!DTO!! - ВИД удобный для передачи
//для любого запроса надо знать айди клиента чтобы его обработать.
//создание параметризированного класса.
//временный объект который существует только для обмена данных, а в стейте у нас хранится состояние клиента
public class ProtocolMessage<T> {
    @Getter
    private T object;
    @Getter
    private int requestOrder;
//идентификатор чего я хочу. в нем много байт. он может гарантировать уникальность
//    для того, чтобы отличать клиентов друг от друга.
//    могли бы опираться на порты и айпи адреса, но это ненадежно, потому что айпи например
//    может поменяться. например клиент может переключиться с моб инета на вай фай. для udp это важно
//   udp - датаграмный = пакет. udp statless - не устанавливают соединения. нет понятия разованного соединения.
//    просто перестал отвечать хост.
//    tcp работает с буфером отправки и буфера принятия.
//    вторая причина. если клиент отключился, а потом заново подключился, то мы сможем его recognised.

    //   идентификатор 16-ти байт.  тут 16 байт лол. генерируется рандомно.
    @Getter
    private String login;
    @Getter
    private String password;
    @Getter
    private boolean isExitRequested;

    public ProtocolMessage() {

    }

    public ProtocolMessage(T object, String login, String password, boolean isExitRequested, int requestOrder) {
        this.object = object;
        this.login = login;
        this.password = password;
        this.isExitRequested = isExitRequested;
        this.requestOrder = requestOrder;
    }

    //    декодировать
    @SuppressWarnings("unchecked")
    public void decode(byte[] bytes) throws IOException, ClassNotFoundException {
//        сериализация. с помощью входящего потока, который принимает массив байтов.
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        login = objectInputStream.readUTF();
        password = objectInputStream.readUTF();
//        читаем декодированный объект и записываем резы
        object = (T) objectInputStream.readObject();
        isExitRequested = objectInputStream.readBoolean();
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
            objectOutputStream.writeUTF(login);
            objectOutputStream.writeUTF(password);
            objectOutputStream.writeObject(object);
            objectOutputStream.writeBoolean(isExitRequested);
            objectOutputStream.writeInt(requestOrder);
        }
//        вернется закодированный массив байтов
        return byteArrayOutputStream.toByteArray();
    }

}
