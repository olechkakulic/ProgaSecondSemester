package olechka.lab5;


import olechka.lab5.commands.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Olya
 * @version 1.0
 * Класс CommandManager в основном необходим для создания и хранения объектов различных команд.
 */
public class CommandManager {

    /**
     * Статическое поле класса, в виде HashMap, в котором в качестве ключа хранятся названия команд,
     * в качестве значения - объект функционального интерфейса.
     */
    private final static Map<String, Supplier<Command>> commands = new HashMap<>();
    /**
     * Поле в виде List, в котором хранится история команд. history заполняется в
     * методе createCommand(), таким образом, при создании команды, она сразу же будет запоминаться.
     */
    private final List<String> history = new LinkedList<>();
    /**
     * Поле в виде HashMap - helpMap, в котором в качестве ключа хранятся названия команд,
     * в качестве значения - описания к командам.
     */
    private final Map<String, String> helpMap = new HashMap<>();
    //    здесь хранятся скрипты которые щас выполняются
    private final Set<String> scriptSet = new HashSet<>();


    /**
     * Статический инициализатор - метод, предназначенный для инициализации поля класса commands.
     */
    static {
        commands.put("add", () -> new AddElementCommand());
        commands.put("update", () -> new UpdateElementCommand());
        commands.put("clear", () -> new ClearCommand());
        commands.put("info", () -> new InfoCommand());
        commands.put("remove", () -> new RemoveElementCommand());
        commands.put("show", () -> new ShowCommand());
        commands.put("save", () -> new SaveCommand());
        commands.put("filter_by_students_count", () -> new FilterByStudentsCount());
        commands.put("print_descending", () -> new PrintDescendingCommand());
        commands.put("exit", () -> new ExitCommand());
        commands.put("history", () -> new HistoryCommand());
        commands.put("execute_script", () -> new ExecuteScriptCommand());
        commands.put("remove_lower", () -> new RemoveLowerCommand());
        commands.put("add_if_min", () -> new AddIfMinCommand());
        commands.put("print_field_descending_students_count", () -> new PrintFieldDescendingCommand());
        commands.put("help", () -> new HelpCommand());
    }

    /**
     * инициализатор - метод, предназначенный для инициализации поля helpMap.
     */ {
        helpMap.put("add", "добавить новый элемент в коллекцию");
        helpMap.put("update", " {id (тип int)} {element} обновить значение элемента коллекции, id которого равен заданному");
        helpMap.put("clear", "очистить коллекцию");
        helpMap.put("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        helpMap.put("remove ", "{id (тип int)} удалить элемент из коллекции по его id");
        helpMap.put("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        helpMap.put("save", "сохранить коллекцию в файл");
        helpMap.put("filter_by_students_count", " {students_count (тип int)} вывести элементы, значение поля studentsCount которых равно заданному");
        helpMap.put("print_descending", "вывести элементы коллекции в порядке убывания");
        helpMap.put("exit", "завершить программу (без сохранения в файл)");
        helpMap.put("history", "вывести последние 14 команд (без их аргументов)");
        helpMap.put("execute_script", " {file_name} считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        helpMap.put("remove_lower", " {element} удалить из коллекции все элементы, меньшие, чем заданный");
        helpMap.put("add_if_min", " {element} добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        helpMap.put("print_field_descending_students_count", "вывести значения поля studentsCount все элементов в порядке убывания");
        helpMap.put("help", "вывести справку по доступным командам");
    }

    /**
     * Функция получения объекта класса команды по названию этой команды.
     *
     * @param name - название команды
     * @return supplier.get() возвращает объект класса различных команд, реализующих
     * интерфейс Command
     */
    public Command createCommand(String name) {
        Supplier<Command> supplier = commands.get(name.toLowerCase());
        if (supplier == null) {
            return null;
        }
        history.add(name);
        if (history.size() >= 15) {
            history.remove(0);
        }
        return supplier.get();
    }

    public boolean pushScriptExecution(String str) {
        return scriptSet.add(str);
    }

    public void popScriptExecution(String str) {
        scriptSet.remove(str);
    }

    /**
     * Функция-getter для получения объекта helpMap типа Map<String, String>
     *
     * @return helpMap
     */
    public Map<String, String> getHelpMap() {
        return helpMap;
    }

    /**
     * Функция-getter для получения объекта history типа List<String>
     *
     * @return history
     */
    public List<String> getHistory() {
        return history;
    }
}
