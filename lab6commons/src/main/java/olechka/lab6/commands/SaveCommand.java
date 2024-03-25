package olechka.lab6.commands;

import olechka.lab6.State;
import olechka.lab6.interaction.Console;
import olechka.lab6.models.StudyGroup;
import olechka.lab6.parsing.JsonObjectParser;
import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

@CommandDescription("сохранить коллекцию в файл")
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
