package it.gdhi;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackageClasses = {GdhiServiceApplication.class})
@EnableAsync
@EnableSwagger2
public class GdhiServiceApplication {

	@Value("${db.url}")
	String dbUrl;
	@Value("${db.username}")
	String dbUsername;
	@Value("${db.password}")
	String dbPassword;
	@Value("${db.driverClassName}")
	String dbDriverClassName;
	@Value("${spring.jpa.showSql}")
	String showSql;
	@Value("${spring.jpa.formatSql}")
	String formatSql;
	@Value("${spring.jpa.hibernate.ddlAuto}")
	String ddlAuto;
	@Value("${spring.jpa.hibernate.namingStrategy}")
	String namingStrategy;
	@Value("${spring.jpa.hibernate.physicalNamingStrategy}")
	String physicalNamingStrategy;
	@Value("${spring.jpa.hibernate.dialect}")
	String dialect;

	@Autowired
	public ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(GdhiServiceApplication.class, args);
	}

	@Bean
	public Properties jpaProperties() {
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", dialect);
		jpaProperties.put("spring.jpa.hibernate.ddl-auto", ddlAuto);
		jpaProperties.put("hibernate.implicit_naming_strategy", namingStrategy);
		jpaProperties.put("hibernate.physical_naming_strategy", physicalNamingStrategy);
		jpaProperties.put("hibernate.show_sql", showSql);
		jpaProperties.put("hibernate.format_sql", formatSql);
		jpaProperties.put("hibernate.event.merge.entity_copy_observer", "allow");
		return jpaProperties;
	}
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("it.gdhi")).build();
	}
	@Bean
	public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
																		 ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
																		 EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
																		 WebEndpointProperties webEndpointProperties, Environment environment) {
		List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
		Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
		allEndpoints.addAll(webEndpoints);
		allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
		allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
		String basePath = webEndpointProperties.getBasePath();
		EndpointMapping endpointMapping = new EndpointMapping(basePath);
		boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment,
				basePath);
		return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
				corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
				shouldRegisterLinksMapping, null);
	}

	private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment,
											   String basePath) {
		return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
				|| ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
	}
	@Bean
	public EntityManagerFactory entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setJpaDialect(new HibernateJpaDialect());
		factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factoryBean.setPersistenceUnitName("persistenceUnit");
		factoryBean.setPackagesToScan("it.gdhi.model", "it.gdhi.internationalization.model");
		factoryBean.setJpaProperties(jpaProperties());
		factoryBean.afterPropertiesSet();

		return factoryBean.getObject();
	}
	@Bean
	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(dbDriverClassName);
		dataSource.setJdbcUrl(dbUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(dbPassword);
		return dataSource;
	}
	@Transactional
	public PlatformTransactionManager transactionManager() {					//will be used during runtime

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}
}
