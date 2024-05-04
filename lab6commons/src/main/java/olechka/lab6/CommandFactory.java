package olechka.lab6;

import olechka.lab6.commands.*;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandFactory {
    private final Map<String, Class<? extends Command>> commands = new HashMap<>();

    //поменялась часть, отвечающая за создание и хранение объектов команд. Теперь в качестве значения будем передавать
//    объект класса Class.
//    в целом поменялся принцип организации команды help.  сделали через аннотации.
    public CommandFactory() {
        commands.put("add", AddElementCommand.class);
        commands.put("update", UpdateElementCommand.class);
        commands.put("clear", ClearCommand.class);
        commands.put("info", InfoCommand.class);
        commands.put("remove", RemoveElementCommand.class);
        commands.put("show", ShowCommand.class);
//        commands.put("save", SaveCommand.class);
        commands.put("filter_by_students_count", FilterByStudentsCount.class);
        commands.put("print_descending", PrintDescendingCommand.class);
        commands.put("exit", ExitCommand.class);
        commands.put("history", HistoryCommand.class);
        commands.put("execute_script", ExecuteScriptCommand.class);
        commands.put("remove_lower", RemoveLowerCommand.class);
        commands.put("add_if_min", AddIfMinCommand.class);
        commands.put("print_field_descending_students_count", PrintFieldDescendingCommand.class);
        commands.put("help", HelpCommand.class);
    }

    //таким образом поменялся принцип создания команды. По ключу получаем значение мапы.
//    на объекте класса Class получаем конструктор. На конструкторе как раз создаем объект команды.
    public Command createCommand(String name) {
        try {
            Class<? extends Command> commandClass = commands.get(name);
            if (commandClass == null) {
                return null;
            }
            Constructor<? extends Command> constructor = commandClass.getConstructor();
            Command command = constructor.newInstance();
            return command;
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
    }

    //функция получения коллекции строк, в ней мы получаем ключи, которые записываем в коллекцию.
    public Collection<String> getCommandNames() {
        return commands.keySet();
    }

    //функция получения описания команд с помощью аннотаций
    public String getCommandDescription(String commandsName) {
//        получаем по ключу объекты класса класс.
        Class<? extends Command> command = commands.get(commandsName);
//        если над объектом класса класс есть аннотация, то получаем ее
        if (command.isAnnotationPresent(CommandDescription.class)) {
            CommandDescription commandDescription = command.getAnnotation(CommandDescription.class);
//            достаем из нее значение.
            return commandDescription.value();
        }
//        иначе возвращаем просто нихуя)
        return "";
    }

    //функция получения названия команды (ключа) по значению. то есть наоборот!!
    public String getCommandName(Class<? extends Command> commandClass) {
//        делаем из map-ы entrySet потому что на мэпе нельзя использовать стримы
        Set<Map.Entry<String, Class<? extends Command>>> listPara = commands.entrySet();
        String commandName = listPara.stream().filter(c -> {
                    return c.getValue() == commandClass;
                })
                .findAny().get().getKey();
        return commandName;
    }
}
