package com.territory.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Log4j2
@JsonInclude(JsonInclude.Include.NON_NULL)
public class District extends AbstractTerritory {
    private UUID uuid;
    private String code;
    private String type;
    private OfficeLocation officeLocation;
    private UUID currentEmployeeUuid;
    private List<Staff> staffs;

    private String employeeID;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String email;

    public District createFromTerritory(Territory territory) {
        OfficeLocation officeLocation = OfficeLocation.builder()
                .mailingAddressLine1(territory.getMailingAddressLine1())
                .mailingAddressLine2(territory.getMailingAddressLine2())
                .mailingAddressLine3(territory.getMailingAddressLine3())
                .city(territory.getCity())
                .stateCodeFlexdata(territory.getStateCodeFlexdata())
                .zipCode(territory.getZipCode())
                .zipCodeExtension(territory.getZipCodeExtension())
                .primaryPhone(territory.getPrimaryPhone())
                .build();
        return District.builder()
                .uuid(territory.getUuid())
                .code(territory.getCode())
                .type(territory.getType())
                .type(territory.getType())
                .currentEmployeeUuid(
                        !territory.getCurrentEmployeeUuid().isEmpty()
                                ? UUID.fromString(territory.getCurrentEmployeeUuid())
                                :null
                )
                .employeeID(territory.getEmployeeID())
                .firstName(territory.getFirstName())
                .middleInitial(territory.getMiddleInitial())
                .lastName(territory.getLastName())
                .email(territory.getEmail())
                .officeLocation(officeLocation)
                .build();
    }
}