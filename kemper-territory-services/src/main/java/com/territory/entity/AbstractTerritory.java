package com.territory.entity;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class AbstractTerritory {
    private UUID uuid;
    private String code; //Territory.TerritoryInformation.TerritoryNumber
    private String type; //Territory.TerritoryService.TerritoryType
    private String employeeId; //Employee.EmployeeInformation.ID
    private String writingNumber; //Employee.EmployeeInformation.PayrollEmpNumber
    private String firstName; //Employee.EmployeeInformation.FirstName
    private String middleInitial; //Employee.EmployeeInformation.MiddleInitial
    private String lastName; //Employee.EmployeeInformation.LastName
    private String email; //Employee.EmployeeInformation.Email
    private UUID currentEmployeeUuid;

    public AbstractTerritory createFromTerritory(Territory territory) {
        throw new UnsupportedOperationException("This method should be overridden in child classes");
    }
}
