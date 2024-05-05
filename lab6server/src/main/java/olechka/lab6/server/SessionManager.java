package olechka.lab6.server;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Map<SocketAddress, Session> clientStates = new ConcurrentHashMap<>();

    //функция получения состояния клиента по его айди.
    public Session getClientSession(SocketAddress socketAddress) {
//        вычислить если отсутствует. принимает коллекцию, смотрит отсуствует ли значение по ключу, и если да, то создает новый стейт
        return clientStates.computeIfAbsent(socketAddress, (id) -> {
            return new Session();
        });
    }
    //метод для того, чтобы исполнить команду на всех состояниях.
}
