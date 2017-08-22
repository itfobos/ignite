package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Arrays;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
//TODO: Remove
public class HomeController extends Controller {
    public Result index() {
        List<String> res = Arrays.asList("first", "second", "third");
        Object sampleObj = new Object() {
            public String name = "Slim Shady";
        };
        return ok(Json.toJson(res));
    }

}
