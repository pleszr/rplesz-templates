package com.territory.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Agency extends AbstractTerritory {
    private UUID uuid;
    private String code;
    private String type;
    private UUID currentEmployeeUuid;

    private String employeeID;
    private String writingNumber;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String email;
    public Agency createFromTerritory(Territory territory) {
        return Agency.builder()
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