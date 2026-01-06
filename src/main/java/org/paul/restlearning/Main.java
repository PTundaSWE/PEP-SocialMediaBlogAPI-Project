package org.paul.restlearning;

import io.javalin.Javalin;
import org.paul.restlearning.controller.SocialMediaController;

public class Main {
    public static void main(String[] args) {
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);

    }
}