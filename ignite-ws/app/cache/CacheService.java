package cache;

import cache.entities.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import play.inject.ApplicationLifecycle;

import javax.cache.Cache;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class CacheService {

    private final IgniteCache<String, User> cache;
    private IgniteCache<String, BinaryObject> binaryCache;

    private final Ignite ignite;

    @Inject
    private CacheService(ApplicationLifecycle lifecycle) {
        Ignition.setClientMode(true);

        ignite = Ignition.start("ignite-config.xml");

        CacheConfiguration<String, User> userCacheConfig = new CacheConfiguration<String, User>("userCache")
                .setCacheMode(CacheMode.PARTITIONED)
                .setQueryEntities(Collections.singleton(createUserQueryEntity()));

        cache = ignite.getOrCreateCache(userCacheConfig);
        binaryCache = cache.withKeepBinary();

        lifecycle.addStopHook(() -> {
            ignite.close();
            return CompletableFuture.completedFuture(null);
        });
    }

    public List<User> getUsersByCellId(String cellId) {
        SqlQuery<String, BinaryObject> query = new SqlQuery<>(User.class, "cellId = ?");
        QueryCursor<Cache.Entry<String, BinaryObject>> users = binaryCache.query(query.setArgs(cellId));

        return StreamSupport.stream(users.spliterator(), false)
                .map(Cache.Entry::getValue)
                .map(CacheService::makeUserFromBinaryObject)
                .collect(Collectors.toList());
    }

    private static User makeUserFromBinaryObject(BinaryObject binaryObject) {
        User result = new User(
                binaryObject.field("name"),
                binaryObject.field("email"),
                binaryObject.field("ctn"));

        result.cellId = binaryObject.field("cellId");
        result.activationDate = binaryObject.field("activationDate");

        return result;
    }

    public void addUser(User user) {
        cache.put(user.ctn, user);
    }


    /**
     * Create cache type metadata for {@link User}.
     *
     * @return Cache type metadata.
     */
    @SuppressWarnings("Duplicates")
    private static QueryEntity createUserQueryEntity() {
        QueryEntity userEntity = new QueryEntity();

        userEntity.setValueType(User.class.getName());
        userEntity.setKeyType(String.class.getName());

        LinkedHashMap<String, String> fields = new LinkedHashMap<>();

        fields.put("name", String.class.getName());
        fields.put("email", String.class.getName());
        fields.put("ctn", String.class.getName());
        fields.put("cellId", String.class.getName());
        fields.put("activationDate", Date.class.getName());

        userEntity.setFields(fields);

        userEntity.setIndexes(Arrays.asList(new QueryIndex("ctn"), new QueryIndex("cellId")));

        return userEntity;
    }

}
