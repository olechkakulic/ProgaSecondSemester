package olechka.lab6.commands;

import olechka.lab6.CommandFactory;
import olechka.lab6.State;
import olechka.lab6.interaction.ArgumentException;
import olechka.lab6.interaction.Console;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

@CommandDescription("{file_name} считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.")
public class ExecuteScriptCommand implements Command {
    private String fileName;

    @Override
    public Result execute(State state) {
        boolean isPushed = state.getCommandManager().pushScriptExecution(fileName);
        if (!isPushed) {
            return Result.error("Скрипт " + fileName + " уже выполняется");
        }
        CommandFactory commandFactory = state.getCommandManager().getCommandFactory();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName));) {
            Scanner scanner = new Scanner(inputStreamReader);
            String out = "";
            while (scanner.hasNext()) {
                Console console = new Console(scanner, Console.getNullStream());
                String commandName = console.next();
                Command command = commandFactory.createCommand(commandName);
                if (command == null) {
                    out += "Неизвестная команда " + commandName;
                    return Result.error(out);
                }
                try {
                    command.parse(console);
                } catch (ArgumentException e) {
                    return Result.error(out + "\nКоманда " + commandName + " имеет некорректный аргумент");
                }
                Result result = command.execute(state);
                out += result.getMessage() + "\n";
                if (!result.isSuccess()) {
                    return Result.error(out);
                }
            }
            return Result.success(out);
        } catch (IOException e) {
            return Result.error("Не удалось прочитать файл");
        } finally {
            state.getCommandManager().popScriptExecution(fileName);
        }

    }

    @Override
    public void parse(Console console) {
        String fileName = console.nextLine().trim();
        if (fileName.isEmpty()) {
            throw new ArgumentException();
        }
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
