package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;

public interface Command {
    //    статический вложенный класс, который не будет связан с объектами внешних классов после создания
    static class Result {
        private final boolean isSuccess;
        private final String message;

        public Result(boolean isSuccess, String message) {
            this.isSuccess = isSuccess;
            this.message = message;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public String getMessage() {
            return message;
        }


        public static Result success(String message) {
            return new Result(true, message);
        }

        public static Result error(String message) {
            return new Result(false, message);
        }
    }

    Result execute(State state);

    void parse(Console console);

}
