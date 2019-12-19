package geektime.spring.springbucks;

import com.mongodb.client.result.UpdateResult;
import geektime.spring.springbucks.converter.MoneyReadConverter;
import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.model.CoffeeOrder;
import geektime.spring.springbucks.model.OrderState;
import geektime.spring.springbucks.model.Tea;
import geektime.spring.springbucks.repository.CoffeeRepository;
import geektime.spring.springbucks.repository.TeaRepository;
import geektime.spring.springbucks.service.CoffeeOrderService;
import geektime.spring.springbucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
public class SpringBucksApplication implements ApplicationRunner {
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	private CoffeeRepository coffeeRepository;
	@Autowired
	private TeaRepository teaRepository;
	@Autowired
	private CoffeeService coffeeService;
	@Autowired
	private CoffeeOrderService orderService;
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	public static void main(String[] args) {
		SpringApplication.run(SpringBucksApplication.class, args);
	}

	@Bean
	public MongoCustomConversions mongoCustomConversions() {
		return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
	}

	@Bean
	@ConfigurationProperties("redis")
	public JedisPoolConfig jedisPoolConfig() {
		return new JedisPoolConfig();
	}

	@Bean(destroyMethod = "close")
	public JedisPool jedisPool(@Value("${redis.host}") String host) {
		return new JedisPool(jedisPoolConfig(), host);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//jpaDemo();
		//mongodbDemo();
		jedisDomo();
	}

	private void jpaDemo() {
		log.info("All Coffee: {}", coffeeRepository.findAll());
		Optional<Coffee> latte = coffeeService.findOneCoffee("Latte");
		if (latte.isPresent()) {
			CoffeeOrder order = orderService.createOrder("Li Lei", latte.get());
			log.info("Update INIT to PAID: {}", orderService.updateState(order, OrderState.PAID));
			log.info("Update PAID to INIT: {}", orderService.updateState(order, OrderState.INIT));
		}
	}

	private void mongodbDemo() throws InterruptedException {
//		Tea greenTea = Tea.builder()
//				.name("greenTea")
//				.price(Money.of(CurrencyUnit.of("CNY"), 20.0))
//				.createTime(new Date())
//				.updateTime(new Date())
//				.build();
//		Tea saved = mongoTemplate.save(greenTea);
//		log.info("tea: {}", saved);
//
//		List<Tea> list = mongoTemplate.find(
//				Query.query(Criteria.where("name").is("greenTea")), Tea.class);
//		log.info("find {} tea:", list.size());
//		list.forEach(c -> log.info("tea {}", c));
//
//		Thread.sleep(1000);
//
//		// 更新
//		UpdateResult updateResult = mongoTemplate.updateFirst(
//				Query.query(Criteria.where("name").is("greenTea")),
//				new Update().set("price", Money.ofMajor(CurrencyUnit.of("CNY"), 30))
//						.currentDate("updateTime"),
//				Tea.class);
//
//		// 打印受影响的数量
//		log.info("Update Result: {}", updateResult.getModifiedCount());
//
//		Tea updateOne = mongoTemplate.findById(saved.getId(), Tea.class);
//		log.info("Update result: {}", updateOne);
//
//		mongoTemplate.remove(updateOne);


		Tea greenTea = Tea.builder()
				.name("greenTea")
				.price(Money.of(CurrencyUnit.of("CNY"), 20.0))
				.createTime(new Date())
				.updateTime(new Date())
				.build();
		Tea blackTea = Tea.builder()
				.name("blackTea")
				.price(Money.of(CurrencyUnit.of("CNY"), 30.0))
				.createTime(new Date())
				.updateTime(new Date())
				.build();

		Thread.sleep(1000);

		teaRepository.insert(Arrays.asList(greenTea, blackTea));
		teaRepository.findAll(Sort.by("name"))
				.forEach(c -> log.info("Saved tea: {}", c));

		greenTea.setPrice(Money.of(CurrencyUnit.of("CNY"), 35));
		greenTea.setUpdateTime(new Date());

		teaRepository.save(greenTea);
		teaRepository.findByName("greenTea").forEach(c -> log.info("tea {}", c));

		teaRepository.deleteAll();
	}

	private void jedisDomo()
	{
		log.info(jedisPoolConfig.toString());
		try (Jedis jedis = jedisPool.getResource()) {
			coffeeService.findAllCoffee().forEach(c -> {
				jedis.hset(
						"springbucks-menu",
						c.getName(),
						Long.toString(c.getPrice().getAmountMajorLong())
				);
			});

			Map<String, String> menu = jedis.hgetAll("springbucks-menu");
			log.info("Menu: {}", menu);

			String price = jedis.hget("springbucks-menu", "espresso");
			log.info("espresso = {}", Money.ofMinor(CurrencyUnit.of("CNY"), Long.parseLong(price)));
		}
	}
}

