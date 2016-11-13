package ru.mail.park.test;

import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.mail.park.Application;
import ru.mail.park.WebSocketConfig;
import ru.mail.park.websocket.GameSocketHandler;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Import(Application.class)
public class LocalTestApplicationH2DB {
	public static final long IDLE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(1);
	public static final int BUFFER_SIZE_BYTES = 8192;

	public static void main(String[] args) {
		SpringApplication.run(new Object[]{WebSocketConfig.class,LocalTestApplicationH2DB.class}, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

	@Bean
	public DefaultHandshakeHandler handshakeHandler() {
		final WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
		policy.setInputBufferSize(BUFFER_SIZE_BYTES);
		policy.setIdleTimeout(IDLE_TIMEOUT_MS);

		return new DefaultHandshakeHandler(
				new JettyRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
	}

	@Bean
	public WebSocketHandler gameWebSocketHandler() {
		return new PerConnectionWebSocketHandler(GameSocketHandler.class);
	}
}
