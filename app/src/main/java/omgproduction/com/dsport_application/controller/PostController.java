package omgproduction.com.dsport_application.controller;

/**
 * Created by Florian on 21.10.2016.
 */
public class PostController {
    private static PostController instance;
    private PostController () {}
    public static synchronized PostController getInstance () {
        if (PostController.instance == null) {
            PostController.instance = new PostController ();
        } return PostController.instance;
    }
}
