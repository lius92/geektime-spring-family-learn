package geektime.spring.data.mongodemo.repository;

import geektime.spring.data.mongodemo.model.Coffee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CoffeeRepository extends MongoRepository<Coffee, Long> {

    List<Coffee> findByName(String name);

}
