package tech.hongjian.sharding_sphere_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@EnableTransactionManagement
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class
})
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
//	@Bean
//	public ServletRegistrationBean<Servlet> druidStatView() {
//		ServletRegistrationBean<Servlet> registBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
//		Map<String, String> params = new HashMap<>();
//		params.put("resetEnable", "false");
////		params.put("loginUsername", "druid");
////		params.put("loginPassword", "druid");
//		registBean.setInitParameters(params);
//		return registBean;
//	}
}
