package controllers;

import cache.CacheProvider;
import cache.entities.User;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.cache.Cache.Entry;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ApiController extends Controller {
    private final IgniteCache<String, User> cache;

    @Inject
    public ApiController(CacheProvider cacheProvider) {
        cache = cacheProvider.getCache();
    }

    public Result getCellUsers(String cellId) {
        SqlQuery<String, User> sql = new SqlQuery<>(User.class, "cellId = ?");
        sql.setArgs(cellId);

        List<User> cellUsers;
        try (QueryCursor<Entry<String, User>> queryCursor = cache.query(sql)) {
            cellUsers = queryCursor.getAll().stream().map(Entry::getValue).collect(Collectors.toList());
        }

        Object response = new Object() {
            int total = cellUsers.size();
            List<User> results = cellUsers;
        };
        return ok(Json.toJson(response));
    }

    public Result populateTestData() {
        User user = new User("John", "john@russinpost.ru", "+79231112233");
        user.cellId = "123qwe";
        cache.put(user.ctn, user);

        user = new User("Alice", "alice@russinpost.ru", "+79234445588");
        user.cellId = "123qwe";
        cache.put(user.ctn, user);


        user = new User("Mark", "mark@pechkin.ru", "+79521112233");
        user.cellId = "anotherCellId";
        cache.put(user.ctn, user);

        user = new User("Jane", "jane@pechkin.ru", "+79524445588");
        user.cellId = "anotherCellId";
        cache.put(user.ctn, user);

        return ok(String.format("Created %d objects", cache.size(CachePeekMode.ALL)));
    }
}
