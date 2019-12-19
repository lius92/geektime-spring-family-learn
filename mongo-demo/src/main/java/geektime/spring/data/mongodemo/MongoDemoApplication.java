package geektime.spring.data.mongodemo;

import com.mongodb.client.result.UpdateResult;
import geektime.spring.data.mongodemo.converter.MoneyReadConverter;
import geektime.spring.data.mongodemo.model.Coffee;
import geektime.spring.data.mongodemo.repository.CoffeeRepository;
import jdk.nashorn.internal.objects.annotations.Where;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableMongoRepositories
public class MongoDemoApplication implements ApplicationRunner {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CoffeeRepository coffeeRepository;

	public static void main(String[] args) {
		SpringApplication.run(MongoDemoApplication.class, args);
	}

	@Bean
	public MongoCustomConversions mongoCustomConversions() {
		return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		Coffee espresso = Coffee.builder()
//				.name("espresso")
//				.price(Money.of(CurrencyUnit.of("CNY"), 20.0))
//				.createTime(new Date())
//				.updateTime(new Date())
//				.build();
//		Coffee saved = mongoTemplate.save(espresso);
//		log.info("coffee: {}", saved);
//
//		List<Coffee> list = mongoTemplate.find(
//				Query.query(Criteria.where("name").is("espresso")), Coffee.class);
//		log.info("find {} coffee:", list.size());
//		list.forEach(c -> log.info("coffee {}", c));
//
//		Thread.sleep(1000);
//
//		// 更新
//		UpdateResult updateResult = mongoTemplate.updateFirst(
//				Query.query(Criteria.where("name").is("espresso")),
//				new Update().set("price", Money.ofMajor(CurrencyUnit.of("CNY"), 30))
//						.currentDate("updateTime"),
//				Coffee.class);
//
//		// 打印受影响的数量
//		log.info("Update Result: {}", updateResult.getModifiedCount());
//
//		Coffee updateOne = mongoTemplate.findById(saved.getId(), Coffee.class);
//		log.info("Update result: {}", updateOne);
//
//		mongoTemplate.remove(updateOne);


		Coffee espresso = Coffee.builder()
				.name("espresso")
				.price(Money.of(CurrencyUnit.of("CNY"), 20.0))
				.createTime(new Date())
				.updateTime(new Date())
				.build();
		Coffee latte = Coffee.builder()
				.name("latte")
				.price(Money.of(CurrencyUnit.of("CNY"), 30.0))
				.createTime(new Date())
				.updateTime(new Date())
				.build();

		Thread.sleep(1000);

		coffeeRepository.insert(Arrays.asList(espresso, latte));
		coffeeRepository.findAll(Sort.by("name"))
				.forEach(c -> log.info("Saved coffee: {}", c));

		latte.setPrice(Money.of(CurrencyUnit.of("CNY"), 35));
		latte.setUpdateTime(new Date());

		coffeeRepository.save(latte);
		coffeeRepository.findByName("lattee").forEach(c -> log.info("Coffee {}", c));

		coffeeRepository.deleteAll();
	}
}
