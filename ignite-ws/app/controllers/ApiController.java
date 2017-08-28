package controllers;

import cache.CacheService;
import cache.entities.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ApiController extends Controller {
    private final CacheService cacheService;

    @Inject
    public ApiController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public Result getCellUsers(String cellId) {
//        SqlQuery<String, User> sql = new SqlQuery<>(User.class, "cellId = ?");
//        sql.setArgs(cellId);
//
//        List<User> cellUsers;
//        try (QueryCursor<Entry<String, User>> queryCursor = cache.query(sql)) {
//            cellUsers = queryCursor.getAll().stream().map(Entry::getValue).collect(Collectors.toList());
//        }
        List<User> cellUsers = new ArrayList<>();
        cacheService.getUsersByCellId().forEach(cellUsers::add);

        Object response = new Object() {
            int total = cellUsers.size();
            List<User> results = cellUsers;
        };

        return ok(Json.toJson(response));
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
