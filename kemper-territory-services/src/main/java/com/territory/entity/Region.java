package com.territory.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Region extends AbstractTerritory {
    UUID uuid;
    private String code;
    private String type;
    private String employeeId;
    private String writingNumber;
    private UUID currentEmployeeUuid;
    private List<District> districts;

    private String employeeID;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String email;
    public Region createFromTerritory(Territory territory) {
        return Region.builder()
                .uuid(territory.getUuid())
                .code(territory.getCode())
                .type(territory.getType())
                .type(territory.getType())
                .currentEmployeeUuid(!territory.getCurrentEmployeeUuid().isEmpty() ? UUID.fromString(territory.getCurrentEmployeeUuid()):null)
                .employeeID(territory.getEmployeeID())
                .firstName(territory.getFirstName())
                .middleInitial(territory.getMiddleInitial())
                .lastName(territory.getLastName())
                .email(territory.getEmail())
                .build();
    }

}