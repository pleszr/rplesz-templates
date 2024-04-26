package com.territory.control;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.territory.entity.CompanyUuid;
import com.territory.entity.Territory;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
@Log4j2
@Service
public class MongoDBCache {
    public static Cache<String, List<Territory>> getAllTerritoryByCompanyUuidCache;
    private final MongoDBClient mongoDBClient;
    @Value("${cache.schedule.expireCacheAfterXMilliseconds}")
    private long expireCacheAfterXMilliseconds;
    public MongoDBCache(MongoDBClient mongoDBClient) {
        this.mongoDBClient = mongoDBClient;
    }
    @PostConstruct
    public void initializeCaches () {
        getAllTerritoryByCompanyUuidCache = Caffeine.newBuilder()
                .expireAfterWrite(expireCacheAfterXMilliseconds, TimeUnit.MILLISECONDS)
                .maximumSize(100)
                .build();
    }
    public void refreshAllTerritoryByCompanyUuidCache() {
        List<CompanyUuid> companyUuidList =  mongoDBClient.getAllCompanyUuid();
        for (CompanyUuid companyUuid : companyUuidList) {
            refreshCacheByCompanyUuid(companyUuid);
        }
    }

    public void refreshCacheByCompanyUuid(CompanyUuid companyUuid) {
        List<Territory> territories = mongoDBClient.getAllTerritoryByCompanyUuid(companyUuid.getUuid().toString());
        if (territories != null) {
            getAllTerritoryByCompanyUuidCache.put(companyUuid.getUuid().toString(),territories);
        }
    }
    public void refreshCacheByCompanyUuid(String companyUuidString) {
        CompanyUuid companyUuid = new CompanyUuid(UUID.fromString(companyUuidString));
        refreshCacheByCompanyUuid(companyUuid);
    }
}
