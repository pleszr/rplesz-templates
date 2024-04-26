package com.territory.boundary;

import com.territory.boundary.dto.BuildStructureByWorkdayIdRequest;
import com.territory.boundary.dto.BuildStructureByWorkdayIdResponse;
import com.territory.boundary.dto.RefreshCacheByCompanyUuidRequest;
import com.territory.entity.Employee;
import com.territory.control.MongoDBCache;
import com.territory.control.TerritoryStructureOrchestratorService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/cps/territory")
@Log4j2
public class TerritoryController {
    private final TerritoryStructureOrchestratorService territoryStructureOrchestratorService;
    private final MongoDBCache mongoDBCache;
    public TerritoryController(TerritoryStructureOrchestratorService territoryStructureOrchestratorService, MongoDBCache mongoDBCache) {
        this.territoryStructureOrchestratorService = territoryStructureOrchestratorService;
        this.mongoDBCache = mongoDBCache;
    }

    @PostMapping("/buildStructureByWorkdayId")
    public ResponseEntity<BuildStructureByWorkdayIdResponse> createEmployeeByPayrollId(@Valid @RequestBody BuildStructureByWorkdayIdRequest request){
        Employee employee = territoryStructureOrchestratorService.buildStructureByWorkdayId(request.workdayId());
        BuildStructureByWorkdayIdResponse response = new BuildStructureByWorkdayIdResponse(employee);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthChecker(){
        log.trace("healthcheck successful");
        return ResponseEntity.ok("healthcheck successful");
    }
    @PostMapping("/refreshCacheAll")
    public ResponseEntity<String> refreshCacheAll(){
        log.trace("Cache refresh started");
        mongoDBCache.refreshAllTerritoryByCompanyUuidCache();
        log.trace("Cache refresh completed");
        return ResponseEntity.ok("Refresh finished");
    }

    @PostMapping("/refreshCacheForCompany")
    public ResponseEntity<String> refreshCacheForCompany(@Valid @RequestBody RefreshCacheByCompanyUuidRequest request){
        log.info("Cache refresh for {} refresh started", request.companyUuid());
        mongoDBCache.refreshCacheByCompanyUuid(request.companyUuid());
        log.info("Cache refresh for {} refresh finished", request.companyUuid());
        return ResponseEntity.ok("Refresh finished");
    }
}
