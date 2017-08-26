package cache;

import cache.entities.User;
import cache.entities.UserRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

@Singleton
public class CacheService {

    private AnnotationConfigApplicationContext applicationContext;
    private UserRepository userRepo;

    @Inject
    private CacheService(ApplicationLifecycle lifecycle) {
        applicationContext = igniteSpringDataInit();
        // Getting a reference to PersonRepository.
        userRepo = applicationContext.getBean(UserRepository.class);

        lifecycle.addStopHook(() -> {
            applicationContext.destroy();
            return CompletableFuture.completedFuture(null);
        });
    }

    private static AnnotationConfigApplicationContext igniteSpringDataInit() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        // Explicitly registering Spring configuration.
        ctx.register(SpringCacheConfig.class);

        ctx.refresh();

        return ctx;
    }

    public Iterable<User> getAllUsers() {
        return this.userRepo.findAll();
    }

    public void addUser(TreeMap<String, User> map) {
        //TODO:
        userRepo.save(map);
    }

}
