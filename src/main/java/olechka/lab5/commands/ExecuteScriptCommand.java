
package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.ArgumentException;
import olechka.lab5.interaction.Console;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ExecuteScriptCommand implements Command {
    private String fileName;

    @Override
    public Result execute(State state) {
        boolean isPushed = state.getCommandManager().pushScriptExecution(fileName);
        if (!isPushed) {
            return Result.error("Скрипт " + fileName + " уже выполняется");
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName));) {
            Scanner scanner = new Scanner(inputStreamReader);
            String out = "";
            while (scanner.hasNext()) {
                Console console = new Console(scanner, Console.getNullStream());
                String commandName = console.next();
                Command command = state.getCommandManager().createCommand(commandName);
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
