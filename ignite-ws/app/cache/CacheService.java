package cache;

import cache.entities.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.cache.Cache;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class CacheService {

    private static final String CONFIG_URL_PROPERTY = "ignite.config.url";

    private final IgniteCache<String, User> cache;
    private IgniteCache<String, BinaryObject> binaryCache;

    private final Ignite ignite;

    @Inject
    private CacheService(ApplicationLifecycle lifecycle) throws MalformedURLException {
        Ignition.setClientMode(true);

        String configUrlStr = System.getProperty(CONFIG_URL_PROPERTY);
        if (configUrlStr != null && !configUrlStr.isEmpty()) {
            this.ignite = Ignition.start(new URL(configUrlStr));
            Logger.info("Used URL {} for connection to Apache ignite", configUrlStr);
        } else {
            ignite = Ignition.start("ignite-config.xml");
            Logger.info("Used default configuration");
        }

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

    /**
     * @return <i>true</i> if user found and updated. <i>false</i> - user found but not updated
     * @throws Exception if user, with CTN, not found.
     */
    public boolean addUserToCell(String ctn, String cellId) throws Exception {
        BinaryObject userBinary = binaryCache.get(ctn);
        if (userBinary == null) {
            throw new Exception("User with CTN '" + ctn + "' not found");
        }

        Object currentCelIdValue = userBinary.field(User.CELL_ID);

        String sql = "UPDATE User set " + User.CELL_ID + " = ? " +
                "WHERE " + User.CTN + " = ? AND " + User.CELL_ID + " = ?";


        FieldsQueryCursor<List<?>> queryCursor = binaryCache.query(new SqlFieldsQuery(sql).setArgs(cellId, ctn, currentCelIdValue));
        Long updatedObjectsAmount = (Long) queryCursor.getAll().get(0).get(0);

        return updatedObjectsAmount > 0;
    }

    public List<User> getUsersByCellId(String cellId) {
        SqlQuery<String, BinaryObject> query = new SqlQuery<>(User.class, User.CELL_ID + " = ?");
        QueryCursor<Cache.Entry<String, BinaryObject>> users = binaryCache.query(query.setArgs(cellId));

        return StreamSupport.stream(users.spliterator(), false)
                .map(Cache.Entry::getValue)
                .map(CacheService::makeUserFromBinaryObject)
                .collect(Collectors.toList());
    }

    private static User makeUserFromBinaryObject(BinaryObject binaryObject) {
        User result = new User(
                binaryObject.field(User.NAME),
                binaryObject.field(User.EMAIL),
                binaryObject.field(User.CTN),
                binaryObject.field(User.ACTIVATION_DATE));

        result.cellId = binaryObject.field(User.CELL_ID);

        return result;
    }

    public void addUser(User user) {
        cache.put(user.ctn, user);
    }

    public boolean isUserExist(String ctn) {
        return cache.containsKey(ctn);
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

        fields.put(User.NAME, String.class.getName());
        fields.put(User.EMAIL, String.class.getName());
        fields.put(User.CTN, String.class.getName());
        fields.put(User.CELL_ID, String.class.getName());
        fields.put(User.ACTIVATION_DATE, Date.class.getName());

        userEntity.setFields(fields);

        userEntity.setIndexes(Arrays.asList(new QueryIndex(User.CTN), new QueryIndex(User.CELL_ID)));

        return userEntity;
    }

}
