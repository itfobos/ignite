package cache;

import cache.entities.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
public class CacheProvider {
    private final IgniteCache<String, User> cache;
    private final Ignite ignite;

    @Inject
    private CacheProvider(ApplicationLifecycle lifecycle) {
        Ignition.setClientMode(true);

        ignite = Ignition.start("example-default.xml");

        CacheConfiguration<String, User> userCacheConfig = new CacheConfiguration<String, User>("userCache")
                .setCacheMode(CacheMode.PARTITIONED)
                .setIndexedTypes(String.class, User.class);

        cache = ignite.getOrCreateCache(userCacheConfig);

        lifecycle.addStopHook(() -> {
            ignite.close();
            return CompletableFuture.completedFuture(null);
        });
    }

    public IgniteCache<String, User> getCache() {
        return cache;
    }

}
