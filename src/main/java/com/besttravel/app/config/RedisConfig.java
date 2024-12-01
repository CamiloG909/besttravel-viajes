package com.besttravel.app.config;

import com.besttravel.app.util.constants.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class RedisConfig {
	@Value(value = "${cache.redis.address}")
	private String serverAddress;
	@Value(value = "${cache.redis.password}")
	private String serverPassword;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress(this.serverAddress)
			.setPassword(this.serverPassword);
		return Redisson.create(config);
	}

	@Bean
	@Autowired
	public CacheManager cacheManager(RedissonClient redissonClient) {
		Map<String, CacheConfig> configs = Map.of(
			CacheConstants.FLY_CACHE_NAME, new CacheConfig(),
			CacheConstants.HOTEL_CACHE_NAME, new CacheConfig(),
			CacheConstants.RESERVATION_CACHE_NAME, new CacheConfig()
		);
		return new RedissonSpringCacheManager(redissonClient, configs);
	}

	@CacheEvict(cacheNames = {
		CacheConstants.HOTEL_CACHE_NAME,
		CacheConstants.FLY_CACHE_NAME,
		CacheConstants.RESERVATION_CACHE_NAME,
	}, allEntries = true)
	@Scheduled(cron = CacheConstants.SCHEDULED_RESET_CACHE)
	@Async
	public void deleteCache() {
		log.info("Clean cache");
	}
}
