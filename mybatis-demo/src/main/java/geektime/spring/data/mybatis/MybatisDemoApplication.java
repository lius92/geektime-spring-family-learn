package geektime.spring.data.mybatis;

import com.github.pagehelper.PageInfo;
import geektime.spring.data.mybatis.mapper.TeaMapper;
import geektime.spring.data.mybatis.model.Tea;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@MapperScan("geektime.spring.data.mybatis.mapper")
public class MybatisDemoApplication implements ApplicationRunner {
	//@Autowired
	//private CoffeeMapper coffeeMapper;
	@Autowired
	private TeaMapper teaMapper;

	public static void main(String[] args) {
		SpringApplication.run(MybatisDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//generateArtifacts();
		//playWithArtifacts();

		teaMapper.findAllWithRowBounds(new RowBounds(1, 3))
				.forEach(c -> log.info("Page(1) Coffee {}", c));

		teaMapper.findAllWithRowBounds(new RowBounds(2, 3))
				.forEach(c -> log.info("Page(2) Coffee {}", c));

		log.info("=============================");

		teaMapper.findAllWithRowBounds(new RowBounds(1, 0))
				.forEach(c -> log.info("Page(1) Coffee {}", c));

		log.info("=============================");

		teaMapper.findAllWithParam(1, 3)
				.forEach(c -> log.info("Page(1) Coffee {}", c));

		List<Tea> list = teaMapper.findAllWithParam(2, 3);
		PageInfo page = new PageInfo(list);
		log.info("PageInfo: {}", page);

	}

//	private void generateArtifacts() throws Exception {
//		List<String> warnings = new ArrayList<>();
//		ConfigurationParser cp = new ConfigurationParser(warnings);
//		Configuration config = cp.parseConfiguration(
//				this.getClass().getResourceAsStream("/generatorConfig.xml"));
//		DefaultShellCallback callback = new DefaultShellCallback(true);
//		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
//		myBatisGenerator.generate(null);
//	}
//
//	private void playWithArtifacts() {
//		Coffee espresso = new Coffee()
//				.withName("espresso")
//				.withPrice(Money.of(CurrencyUnit.of("CNY"), 20.0))
//				.withCreateTime(new Date())
//				.withUpdateTime(new Date());
//		coffeeMapper.insert(espresso);
//
//		Coffee latte = new Coffee()
//				.withName("latte")
//				.withPrice(Money.of(CurrencyUnit.of("CNY"), 30.0))
//				.withCreateTime(new Date())
//				.withUpdateTime(new Date());
//		coffeeMapper.insert(latte);
//
//		Coffee s = coffeeMapper.selectByPrimaryKey(1L);
//		log.info("Coffee {}", s);
//
////		CoffeeExample example = new CoffeeExample();
////		example.createCriteria().andNameEqualTo("latte");
////		List<Coffee> list = coffeeMapper.selectByExample(example);
////		list.forEach(e -> log.info("selectByExample: {}", e));
//
//		CoffeeExample example = new CoffeeExample();
//		example.createCriteria().andPriceBetween(Money.of(CurrencyUnit.of("CNY"), 5.0),
//				Money.of(CurrencyUnit.of("CNY"), 10.0));
//		List<Coffee> list = coffeeMapper.selectByExample(example);
//		list.forEach(e -> log.info("selectByExample: {}", e));
//	}
}

