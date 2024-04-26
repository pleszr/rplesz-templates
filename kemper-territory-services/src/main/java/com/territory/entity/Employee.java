package com.territory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
    UUID uuid;
    Integer employeeId;
    String writingNumber;
    String firstName;
    String middleInitial;
    String lastName;
    String emailAddress;
    String phone;
    String nationalProducerNumber;
    String residentState;
    String employmentStatus;
    String role;
    @JsonIgnore
    String territoryRef;
    Company company;
}






