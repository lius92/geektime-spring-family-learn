package geektime.spring.springbucks.repository;

import geektime.spring.springbucks.model.Tea;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeaRepository extends MongoRepository<Tea, Long> {
    List<Tea> findByName(String name);
}
