package olechka.lab6;


import olechka.lab6.commands.Command;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


// Класс CommandManager в основном необходим для создания и хранения объектов различных команд.

public class CommandManager {

    private final List<String> history = new LinkedList<>();

    //    здесь хранятся скрипты которые щас выполняются
    private final Set<String> scriptSet = new HashSet<>();
    //теперь тут будет создаваться commandFactory
    private final CommandFactory commandFactory = new CommandFactory();


    public void addHistoryCommand(Command command) {
//и для добавления названия команды в историю, будем использовать метод getCommandName
//        получаем имя команды через объект класса Class.
        String commandName = commandFactory.getCommandName(command.getClass());
        history.add(commandName);
        if (history.size() >= 15) {
            history.remove(0);
        }
    }


    public boolean pushScriptExecution(String str) {
        return scriptSet.add(str);
    }

    public void popScriptExecution(String str) {
        scriptSet.remove(str);
    }

    /**
     * Функция-getter для получения объекта history типа List<String>
     *
     * @return history
     */
    public List<String> getHistory() {
        return history;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }
}
