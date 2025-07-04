package co.edu.udistrital.mdp.eventos.exceptions;

public class ErrorMessage {
    public static final String EVENT_NOT_FOUND = "EVENT_NOT_FOUND";
    public static final String ASSISTANT_NOT_FOUND = "The assistant with the given id was not found";
    public static final String ORGANIZER_NOT_FOUND = "The organizer with the given id was not found";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String LOCATION_NOT_FOUND = "LOCATION_NOT_FOUND";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String TICKET_NOT_FOUND = "TICKET_NOT_FOUND";

    public static final String PREFERENCE_NOT_FOUND = "The preference with the given id was not found";
    public static final String PREFERENCE_NOT_ASSOCIATED = "The preference is not associated to the assistant";

    public static final String BOOKING_NOT_FOUND = "The booking with the given id was not found";
    public static final String BOOKING_NOT_ASSOCIATED = "The booking is not associated to the assistant";

    public static final String NOTIFICATION_NOT_FOUND = "The notification with the given id was not found";
    public static final String NOTIFICATION_NOT_ASSOCIATED = "The notification is not associated to the assistant";

    public static final String PURCHASE_NOT_FOUND = "The purchase with the given id was not found";
    public static final String PURCHASE_NOT_ASSOCIATED = "The purchase is not associated to the assistant";

    public static final String REFUND_NOT_FOUND = "The refund with the given id was not found";


    public static final String PROMO_NOT_FOUND = "The promo with the given id was not found";


    private ErrorMessage() {
        throw new IllegalStateException("Utility class");
    }
}
