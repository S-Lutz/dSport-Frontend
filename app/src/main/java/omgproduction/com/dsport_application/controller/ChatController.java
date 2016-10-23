package omgproduction.com.dsport_application.controller;

/**
 * Created by Florian on 21.10.2016.
 */
public class ChatController {
    private static ChatController instance;
    private ChatController () {}
    public static synchronized ChatController getInstance () {
        if (ChatController.instance == null) {
            ChatController.instance = new ChatController ();
        } return ChatController.instance;
    }
}
