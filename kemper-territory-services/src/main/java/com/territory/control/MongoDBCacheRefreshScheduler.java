package com.territory.control;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
@Configuration
@EnableScheduling
public class MongoDBCacheRefreshScheduler {
    private final MongoDBCache mongoDBCache;
    MongoDBCacheRefreshScheduler(MongoDBCache mongoDBCache) {
        this.mongoDBCache = mongoDBCache;
    }
    @Scheduled(fixedRateString = "${cache.schedule.refreshCacheEveryXMilliseconds}")
    private void refreshCache() {
        log.info("Cache refresh started");
        mongoDBCache.refreshAllTerritoryByCompanyUuidCache();
        log.info("Cache refresh completed");
    }

}
