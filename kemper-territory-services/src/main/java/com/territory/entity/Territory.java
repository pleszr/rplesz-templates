package com.territory.entity;


import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@NoArgsConstructor
public class Territory {


    private UUID uuid;
    private String code;
    private String type; //Territory.TerritoryService.TerritoryType
    private String companyUuid;
    private String currentEmployeeUuid;
    private String regionUuid;
    private String districtUuid;
    private String staffUuid;
    private OfficeLocation officeLocation;

    private String mailingAddressLine1; //Territory.MailingAddress.MailingAddressLine1
    private String mailingAddressLine2; //Territory.MailingAddress.MailingAddressLine2
    private String mailingAddressLine3; //Territory.MailingAddress.MailingAddressLine3
    private String city; //Territory.MailingAddress.City
    private String stateCodeFlexdata; //Territory.MailingAddress.StateCodeFlexdata
    private String zipCode; //Territory.MailingAddress.ZipCode
    private String zipCodeExtension; //Territory.MailingAddress.ZipCodeExtension
    private String primaryPhone; //Territory.Primary.PrimaryPhone

    private String employeeID; //Employee.EmployeeInformation.ID
    private String writingNumber; //Employee.EmployeeInformation.PayrollEmpNumber
    private String firstName; //Employee.EmployeeInformation.FirstName
    private String middleInitial; //Employee.EmployeeInformation.MiddleInitial
    private String lastName; //Employee.EmployeeInformation.LastName
    private String email; //Employee.EmployeeInformation.Email

    public String getUuidBasedOnType(String type) {
        return switch (type) {
            case "Company" -> companyUuid;
            case "Region" -> regionUuid;
            case "District" -> districtUuid;
            case "Staff" -> staffUuid;
            default -> null;
        };
    }
}
