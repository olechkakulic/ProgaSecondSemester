package olechka.lab6.server;

import olechka.lab6.State;
import olechka.lab6.commands.Command;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//класс для управления пользовательскими состояниями.
//сервер будет управлять пользовательскими состояниями
public class StateManager {
    //    при создании объекта StateManager-а (у меня он создается при создании State), будет создаваться наша мэпа
//    чтобы не сломалось ничего, потому что при нажатии сtrl+c на сервере, создается параллельный поток, который сохраняет данные.
//
    private final Map<UUID, State> clientStates = new ConcurrentHashMap<>();

    //функция получения состояния клиента по его айди.
    public State getClientState(UUID clientId) {
//        вычислить если отсутствует. принимает коллекцию, смотрит отсуствует ли значение по ключу, и если да, то создает новый стейт
        return clientStates.computeIfAbsent(clientId, (id) -> {
            return new State("client-" + id + ".json");
        });
    }

    //метод для того, чтобы исполнить команду на всех состояниях.
    public void executeOnAllStates(Command command) {
        for (State state :
                clientStates.values()) {
            command.execute(state);
        }
    }
}
