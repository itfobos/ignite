package controllers;

import cache.CacheService;
import cache.entities.User;
import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ApiController extends Controller {

    private final CacheService cacheService;
    private final FormFactory formFactory;

    @Inject
    public ApiController(CacheService cacheService, FormFactory formFactory) {
        this.cacheService = cacheService;
        this.formFactory = formFactory;
    }

    public Result getCellUsers(String cellId) {
        List<User> cellUsers = cacheService.getUsersByCellId(cellId);

        Object response = new Object() {
            public int total = cellUsers.size();

            public List<UserDTO> results = cellUsers.stream().map(UserDTO::new).collect(Collectors.toList());
        };

        return ok(Json.toJson(response));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addUserToCell() {
        JsonNode requestJson = request().body().asJson();

        Result result;
        try {
            boolean operationResult = cacheService.addUserToCell(requestJson.get("ctn").asText(), requestJson.get("cellId").asText());
            Logger.debug("operationResult: {}", operationResult);

            result = ok(operationResult ? "Updated" : "Concurrent modification");
        } catch (Exception e) {
            Logger.debug(e.getMessage());
            result = notFound(e.getMessage());
        }

        return result;
    }


    public Result populateTestData() {
        User user = new User("John", "john@russinpost.ru", "+79231112233");
        user.cellId = "123qwe";
        cacheService.addUser(user);

        user = new User("Alice", "alice@russinpost.ru", "+79234445588");
        user.cellId = "123qwe";
        cacheService.addUser(user);
        ;

        user = new User("Mark", "mark@pechkin.ru", "+79521112233");
        user.cellId = "anotherCellId";
        user.activationDate = new Date();
        cacheService.addUser(user);

        user = new User("Jane", "jane@pechkin.ru", "+79524445588");
        user.cellId = "anotherCellId";
        user.activationDate = new Date();
        cacheService.addUser(user);


        return ok(String.format("Created %d objects", 4));
    }
}
