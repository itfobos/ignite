package controllers;

import cache.entities.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;

public class TestApp {
    public static void main(String[] args) {
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("example-default.xml")) {

            CacheConfiguration<String, User> userCacheConfig = new CacheConfiguration<String, User>("userCache")
                    .setCacheMode(CacheMode.PARTITIONED)
                    .setIndexedTypes(String.class, User.class);

            IgniteCache<String, User> cache = ignite.getOrCreateCache(userCacheConfig);

            User user = new User("John", "john@russinpost.ru", "+79231112233");
            cache.put(user.ctn, user);

            user = new User("Alice", "alice@russinpost.ru", "+79234445588");
            cache.put(user.ctn, user);

            System.out.println("From cache");
            System.out.println(cache.get("+79231112233"));
            System.out.println(cache.get("+79234445588"));

        }
    }
}
