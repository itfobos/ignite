package controllers;

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

import javax.cache.Cache;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

public class TestApp {
    public static void main(String[] args) {
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("ignite-config.xml")) {

            CacheConfiguration<String, User> userCacheConfig = new CacheConfiguration<String, User>("userCache")
                    .setCacheMode(CacheMode.PARTITIONED)
                    .setQueryEntities(Collections.singleton(createUserQueryEntity()));

            IgniteCache<String, User> cache = ignite.getOrCreateCache(userCacheConfig);

            User user = new User("John", "john@russinpost.ru", "+79231112233");
            user.cellId = "cellId";
            cache.put(user.ctn, user);

            user = new User("Alice", "alice@russinpost.ru", "+79234445588");
            user.cellId = "cellId";
            cache.put(user.ctn, user);

            IgniteCache<String, BinaryObject> binaryCache = cache.withKeepBinary();

            SqlQuery<String, BinaryObject> query = new SqlQuery<>(User.class, "cellId = ?");
            QueryCursor<Cache.Entry<String, BinaryObject>> users = binaryCache.query(query.setArgs("cellId"));

            for (Cache.Entry<String, BinaryObject> e : users.getAll()) {
                System.out.println(">>>     " + e.getKey() + "     " + e.getValue().deserialize());
            }
        }
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

        userEntity.setFields(fields);

        userEntity.setIndexes(Arrays.asList(
                //TODO: Check the requirements
                new QueryIndex("ctn"), new QueryIndex("cellId")
        ));

        return userEntity;
    }
}
