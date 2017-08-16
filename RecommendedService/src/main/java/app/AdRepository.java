package app;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdRepository extends MongoRepository<Ad, String>{
}
