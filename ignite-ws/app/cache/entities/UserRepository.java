package cache.entities;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;

@RepositoryConfig(cacheName = "userCache")
public interface UserRepository extends IgniteRepository<User, String> {
}
