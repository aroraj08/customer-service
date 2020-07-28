package com.example.springjpa.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("cache")
@Data
public class CacheConfig {

    private long timeoutSeconds;
    private int redisPort;
    private String redisHost;
    private Map<String, Long> cacheExpiration = new HashMap<>();

}
