package co.edu.udistrital.mdp.eventos.exceptions;

public class ErrorMessage {
    public static final String EVENT_NOT_FOUND = "EVENT_NOT_FOUND";
    public static final String ASSISTANT_NOT_FOUND = "The assistant with the given id was not found";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";


    private ErrorMessage() {
        throw new IllegalStateException("Utility class");
    }
}
