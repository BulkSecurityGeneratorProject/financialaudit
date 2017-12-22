package com.bkavramlari.financialaudit.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yildizib on 1.12.2017.
 */
@Configuration
@EnableCaching
public class CacheConfig {
    /**
     *
     */
    public interface ICacheNames {
        String HOURLY = "HOURLY";
        String DAILY = "DAILY";
        String WEEKLY = "WEEKLY";
    }

}
