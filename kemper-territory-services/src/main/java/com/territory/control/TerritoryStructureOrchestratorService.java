package com.territory.control;

import com.territory.ErrorCode;
import com.territory.Api2QueryException;
import com.territory.entity.*;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class TerritoryStructureOrchestratorService {
    private final MongoDBClient mongoDBClient;
    TerritoryStructureOrchestratorService(MongoDBClient mongoDBClient) {
        this.mongoDBClient = mongoDBClient;
    }
    
    public Employee buildStructureByWorkdayId(String workdayId) {
        Employee employee = getEmployeeByWorkdayId(workdayId);
        employee.setCompany(
                getCompanyByTerritoryUuid(
                        employee.getTerritoryRef())
        );
        List<Territory> allTerritories = loadAllTerritoryByCompanyUuid(
                employee.getCompany().getUuid()
        );
        addRegionsToCompany(
                allTerritories,
                employee.getCompany()
        );
        addDistrictsToRegions(
                allTerritories,
                employee.getCompany().getRegions()
        );
        List<District> districts = getAllDistricts(
                employee.getCompany().getRegions()
        );
        addStaffsToDistricts(
                allTerritories, districts
        );
        List<Staff> staffs = getAllStaffs(districts);
        addAgenciesToStaff(
                allTerritories,
                staffs
        );
        return employee;
    }

    private Employee getEmployeeByWorkdayId(String workdayId) {
        Employee employee = mongoDBClient.getEmployeeByWorkdayId(workdayId)
                .orElseThrow(() -> {
                    log.error("No employee found with workdayId: {}",workdayId);
                    return new Api2QueryException(ErrorCode.NOT_FOUND,"Employee not found");
                });
        log.debug("Employee found by workdayId: {}. Employee created: {}",workdayId, employee);
        return employee;
    }
    private Company getCompanyByTerritoryUuid(String territoryRef) {
        UUID territoryUuid = convertRefToUuid(territoryRef);
        Document companyDocument = mongoDBClient.getCompanyUuidByTerritoryUuid(territoryUuid)
                .orElseThrow(() -> {
                        log.error("Did not find {} in territory collection", territoryUuid.toString());
                        return new Api2QueryException(ErrorCode.NOT_FOUND, "Company not found");
                });
        UUID companyUuid =  getCompanyUuidAsStringFromDocument(companyDocument);
        Company company = mongoDBClient.getCompanyByUuid(companyUuid)
                .orElseThrow(() -> {
                    log.error("Did not find {} in territory collection",companyUuid);
                    return new Api2QueryException(ErrorCode.NOT_FOUND, "Company not found");
                });
        log.info("Company found by territoryUuid: " + territoryUuid + ". Company created: " + company);
        return company;
    }

    private UUID getCompanyUuidAsStringFromDocument(Document companyDocument) {
        String companyUuidString;
        try {
            companyUuidString = companyDocument.get("companyUuid").toString();
        } catch (Exception e) {
            throw new Api2QueryException(
                    ErrorCode.NOT_FOUND,
                    String.format(
                            "Could not extract companyUuid from territory document: %s",
                            companyDocument.toJson()
                    )
            );
        }
        return UUID.fromString(companyUuidString);
    }
    private List<Territory> loadAllTerritoryByCompanyUuid(UUID companyUuid) {
        List<Territory> allTerritories;
        List<Territory> cachedTerritories =
                MongoDBCache.getAllTerritoryByCompanyUuidCache.getIfPresent(companyUuid.toString());
        if (cachedTerritories == null) {
            allTerritories = new ArrayList<>(mongoDBClient.getAllTerritoryByCompanyUuid(companyUuid.toString()));
            MongoDBCache.getAllTerritoryByCompanyUuidCache.put(companyUuid.toString(), allTerritories);
            log.debug("Uuid {} was not found in cache. Loaded all territories from DB. allTerritories size: {}",
                    companyUuid.toString(),
                    allTerritories.size());
        } else {
            allTerritories = cachedTerritories;
        }
        List<Territory> copyList = new ArrayList<>(allTerritories); //return a copy as we will be removing elements from it
        log.info("loadAllTerritoryByCompanyUuid finished with companyUuid: {}, allTerritories size: {}",
                companyUuid,
                allTerritories.size());
        return copyList;
    }
    private void addRegionsToCompany(List<Territory> allTerritories, Company company) {
        company.setRegions(
                getListOfChildrenBasedOnParentUuid(
                        allTerritories,
                        company.getUuid().toString(),
                        "Company",
                        "Region",
                        Region.builder().build()
                )
        );
    }
    private void addDistrictsToRegions(List<Territory> allTerritories, List<Region> regions) {
        for (Region region : regions) {
            region.setDistricts(
                    getListOfChildrenBasedOnParentUuid(
                            allTerritories,
                            region.getUuid().toString(),
                            "Region",
                            "District",
                            District.builder().build()
                    )
            );
        }
        log.info("addDistrictsToRegions finished");
    }
    private List<District> getAllDistricts(List<Region> regions) {
        List<District> allDistricts = new ArrayList<>();
        for (Region region : regions) {
            allDistricts.addAll(region.getDistricts());
        }
        log.info("getAllDistricts finished. allDistricts size: " + allDistricts.size());
        return allDistricts;
    }


    private void addStaffsToDistricts(List<Territory> allTerritories, List<District> districts) {
        for (District district : districts) {
            district.setStaffs(
                    getListOfChildrenBasedOnParentUuid(
                            allTerritories,
                            district.getUuid().toString(),
                            "District",
                            "Staff",
                            Staff.builder().build()
                    )
            );
        }
        log.info("addStaffsToDistricts finished");
    }
    private List<Staff> getAllStaffs(List<District> districts) {
        List<Staff> allStaffs = new ArrayList<>();
        for (District district : districts) {
            try {
                allStaffs.addAll(district.getStaffs());
            } catch (NullPointerException nullPointerException) {
                log.info("No staffs found for district: " + district.getUuid().toString());
            }
        }
        log.info("getAllStaffs: allStaffs size: " + allStaffs.size());
        return allStaffs;
    }
    private void addAgenciesToStaff(List<Territory> allTerritories, List<Staff> staffs) {
        for (Staff staff : staffs) {
            staff.setAgencies(
                    getListOfChildrenBasedOnParentUuid(
                            allTerritories,
                            staff.getUuid().toString(),
                            "Staff",
                            "Agency",
                            Agency.builder().build()
                    )
            );
        }
        log.info("addAgenciesToStaff finished");
    }

    private static UUID convertRefToUuid(String ref) {
        log.info("Converting ref to UUID: " + ref);
        String uuidString = ref.substring(0, ref.indexOf(";"));
        return UUID.fromString(uuidString);
    }
    private <GenericTerritory extends AbstractTerritory> List<GenericTerritory> getListOfChildrenBasedOnParentUuid(
            List<Territory> allTerritories,
            String uuid,
            String parentType,
            String childType,
            GenericTerritory instance) {
        List<GenericTerritory> genericTerritories = new ArrayList<>();
        log.debug("getListOfChildrenBasedOnParentUuid initiated with uuid: "
                + uuid + ", parentType: " + parentType + ", childType: " + childType
                + ". allTerritories size: " + allTerritories.size());
        Iterator<Territory> iterator = allTerritories.iterator();
        while (iterator.hasNext()) {
            Territory territory = iterator.next();
            if (
                    territory.getType().equals(childType)
                            && (territory.getUuidBasedOnType(parentType).equals(uuid))
            ) {
                @SuppressWarnings("unchecked")
                GenericTerritory territoryInstance = (GenericTerritory) instance.createFromTerritory(territory);
                genericTerritories.add(territoryInstance);
                iterator.remove();
            }
        }
        log.trace("getListOfChildrenBasedOnParentUuid finished. allTerritories size: " + allTerritories.size());
        return genericTerritories;
    }
}

