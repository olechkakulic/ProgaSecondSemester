package olechka.lab5;

import olechka.lab5.commands.Command;
import olechka.lab5.interaction.ArgumentException;
import olechka.lab5.interaction.Console;
import olechka.lab5.interaction.InterationClosedException;
import olechka.lab5.models.StudyGroup;
import olechka.lab5.parsing.JsonObjectParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws ReflectiveOperationException {
        if (args.length == 0) {
            System.err.println("не введен путь к файлу");
            System.exit(1);
        }

        String filename = args[0];
        State state = new State(filename);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            if (bufferedReader.ready()) {
//                Через JSONTokener мы считываем из reader-а объект JSONArray
                JSONTokener tokener = new JSONTokener(bufferedReader);
                JSONArray jsonArray = new JSONArray(tokener);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    StudyGroup studyGroup = JsonObjectParser.createFromJsonObject(StudyGroup.class, jsonObject);
                    state.addElement(studyGroup);
                }
            } else {
                System.out.println("В качестве аргумента был передан пустой файл");
            }
        } catch (IOException e) {
            System.err.println("Файл не найден или нет доступа ");
            System.exit(1);
        } catch (JSONException e) {
            System.err.println("Файл поврежден. Неверная JSON-структура. ");
            //System.exit(1);
        } catch (Exception e) {
            System.err.println("Файл поврежден");
            //System.exit(1);
        }
        System.out.println("Здравствуйте! Вы находитесь в программе Управление коллекцией.");
        Console console = new Console();
//        ExecuteScriptCommand initialCommand = new ExecuteScriptCommand();
//        initialCommand.setFileName(args[0]);
//        Command.Result initianalResult = initialCommand.execute(state);
//        if (!initianalResult.isSuccess()){
//            System.err.println("Файл со скриптом не найден или скрипт поврежден: " + initianalResult.getMessage());
//            System.exit(1);
//        }
        try {
//            цикл будет работать пока exitrequested false
            while (!console.isInputClosed() && !state.isExitRequested()) {
                String commandName = console.next();
                Command command = state.getCommandManager().createCommand(commandName);
                if (command == null) {
                    System.out.println("Введена несуществующая команда/аргумент " + commandName);
                    continue;
                }
                try {
                    command.parse(console);
                } catch (ArgumentException exception) {
                    System.out.println("Данная команда требует корректный аргумент. Подробнее смотри, используя, команду 'help'");
                    continue;
                }
                Command.Result result = command.execute(state);
                System.out.println(result.getMessage());

            }
            System.out.println("Спасибо за использование. Обязательно подпишитесь на мой тгк https://t.me/krinzh_umer");
        } catch (InterationClosedException e) {
            if (System.getProperty("os.name", "").startsWith("Windows")) {
                System.out.println("Была нажата комбинация клавиш ctrl+c. Совершен выход из программы.");
            } else {
                System.out.println("Была нажата комбинация клавиш ctrl+d. Совершен выход из программы.");
            }

//
//            SaveCommand saveCommand = new SaveCommand();
//            Command.Result result = saveCommand.execute(state);
//            if (result.isSuccess()) {
//                System.out.println("я сохранил тебе файл все пока");
//            }
        }
    }
}