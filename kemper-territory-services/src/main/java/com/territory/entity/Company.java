package com.territory.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {
    UUID uuid;
    String code; //Territory.TerritoryInformation.TerritoryNumber
    String policycompanyCode; //policycompanyCode_notInSkyeYet
    String writingNumber; //Employee.EmployeeInformation.PayrollEmpNumber
    String type; //Territory.TerritoryService.TerritoryType
    String name; //Territory.TerritoryInformation.TerritoryName
    String abbreviation; //abbreviation_notInSkyeYet
    UUID currentEmployeeUuid;
    List<Region> regions;
}
