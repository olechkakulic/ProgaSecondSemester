package olechka.lab8.exceptions;


import lombok.Getter;

public class CommandExecutionException extends RuntimeException {
    @Getter
    private final CommandFailureType commandFailureType;

    public CommandExecutionException(CommandFailureType commandFailureType) {
        super(commandFailureType.name());
        this.commandFailureType = commandFailureType;
    }
}
