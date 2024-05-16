package olechka.lab7;


import lombok.Getter;
import olechka.lab7.commands.Command;

import java.util.*;


// Класс CommandManager в основном необходим для создания и хранения объектов различных команд.

public class CommandManager {

    /**
     * -- GETTER --
     * Функция-getter для получения объекта history типа List<String>
     *
     * @return history
     */
    @Getter
    private final List<String> history = new LinkedList<>();

    //    здесь хранятся скрипты которые щас выполняются
    private final Set<String> scriptSet = new HashSet<>();
    //теперь тут будет создаваться commandFactory
    @Getter
    private final CommandFactory commandFactory = new CommandFactory();


    public void addHistoryCommand(Command command) {
//и для добавления названия команды в историю, будем использовать метод getCommandName
//        получаем имя команды через объект класса Class.
        Optional<String> commandName = commandFactory.getCommandName(command.getClass());
        if (commandName.isPresent()) {
            history.add(commandName.get());
            if (history.size() >= 15) {
                history.remove(0);
            }
        }

    }


    public boolean pushScriptExecution(String str) {
        return scriptSet.add(str);
    }

    public void popScriptExecution(String str) {
        scriptSet.remove(str);
    }

}
