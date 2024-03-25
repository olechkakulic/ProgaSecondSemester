package olechka.lab6.server;

import olechka.lab6.State;
import olechka.lab6.commands.Command;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//сервер будет управлять пользовательскими состояниями
public class StateManager {
    private final Map<UUID, State> clientStates = new ConcurrentHashMap<>();

    public State getClientState(UUID clientId) {
//        вычислить если отсутствует. принимает коллекцию, смотрит отсуствует ли значение по ключу, и если да, то создает новый стейт
        return clientStates.computeIfAbsent(clientId, (id) -> {
            return new State("client-" + id + ".json");
        });
    }

    public void executeOnAllStates(Command command) {
        for (State state :
                clientStates.values()) {
            command.execute(state);
        }
    }
}
