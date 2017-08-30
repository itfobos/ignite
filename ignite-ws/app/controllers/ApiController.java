package controllers;

import cache.CacheService;
import cache.entities.User;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ApiController extends Controller {

    private final CacheService cacheService;

    private final Form<UserProfileDTO> profileForm;

    @Inject
    public ApiController(CacheService cacheService, FormFactory formFactory) {
        this.cacheService = cacheService;

        profileForm = formFactory.form(UserProfileDTO.class);
    }

    public Result getCellUsers(String cellId) {
        List<User> cellUsers = cacheService.getUsersByCellId(cellId);

        Object response = new Object() {
            public int total = cellUsers.size();

            public List<UserInfoDTO> results = cellUsers.stream().map(UserInfoDTO::new).collect(Collectors.toList());
        };

        return ok(Json.toJson(response));
    }

    public Result addUserProfile() {
        Form<UserProfileDTO> currentForm = profileForm.bindFromRequest();

        Result result;

        if (!currentForm.hasErrors()) {
            UserProfileDTO userProfileDTO = currentForm.get();
            if (!cacheService.isUserExist(userProfileDTO.getCtn())) {
                result = created();
                cacheService.addUser(new User(userProfileDTO.getName(), userProfileDTO.getEmail(), userProfileDTO.getCtn()));
            } else {
                result = status(CONFLICT, "User with CTN '" + userProfileDTO.getCtn() + "' already exist");
            }


        } else {
            List<FieldValidationErrorDTO> errorDTOs = currentForm.allErrors()
                    .stream()
                    .map(FieldValidationErrorDTO::new)
                    .collect(Collectors.toList());

            Object response = new Object() {
                public List<FieldValidationErrorDTO> errors = errorDTOs;
            };

            result = badRequest(Json.toJson(response));
        }

        return result;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addUserToCell() {
        JsonNode requestJson = request().body().asJson();

        Result result;
        try {
            boolean operationResult = cacheService.addUserToCell(requestJson.get("ctn").asText(), requestJson.get("cellId").asText());

            result = operationResult ? ok("Updated") : status(CONFLICT, "Concurrent modification");
        } catch (Exception e) {
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
        cacheService.addUser(user);

        user = new User("Jane", "jane@pechkin.ru", "+79524445588");
        user.cellId = "anotherCellId";
        cacheService.addUser(user);


        return ok(String.format("Created %d objects", 4));
    }
}
