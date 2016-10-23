package omgproduction.com.dsport_application.controller;

/**
 * Created by Florian on 21.10.2016.
 */
public class EventController {
    private static EventController instance;
    private EventController () {}
    public static synchronized EventController getInstance () {
        if (EventController.instance == null) {
            EventController.instance = new EventController ();
        } return EventController.instance;
    }
}
