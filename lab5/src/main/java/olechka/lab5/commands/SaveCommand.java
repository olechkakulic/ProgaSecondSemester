package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;
import olechka.lab5.models.StudyGroup;
import olechka.lab5.parsing.JsonObjectParser;
import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class SaveCommand implements Command {


    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        String filename = state.getSaveFileName();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            JSONArray array = new JSONArray();
            for (StudyGroup s :
                    collection) {
                array.put(JsonObjectParser.jsonParser(s));
            }
            bufferedWriter.write(array.toString(2));
        } catch (IOException e) {
            return Result.error("Не удалось записать в файл");
        }
        return Result.success("Вы сохранили значения в файл: " + filename);
    }

    @Override
    public void parse(Console console) {
    }
}
