package olechka.lab8.server;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Map<SocketAddress, RemoteClient> sessions = new ConcurrentHashMap<>();

    public RemoteClient getClientSession(SocketAddress socketAddress) {
//        вычислить если отсутствует. принимает коллекцию, смотрит отсуствует ли значение по ключу, и если да, то создает новый стейт
        return sessions.computeIfAbsent(socketAddress, (address) -> {
            return new RemoteClient(address);
        });
    }

    public Collection<RemoteClient> getClients() {
        return sessions.values();
    }
}
