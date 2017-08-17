package controllers;

import play.libs.Json;
import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    public Result index() {
        Object sampleObj = new Object() {
            public String name = "Slim Shady";
        };
        return ok(Json.toJson(sampleObj));
    }

}
