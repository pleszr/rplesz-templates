package com.territory.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfficeLocation {
    private String mailingAddressLine1; //Territory.MailingAddress.MailingAddressLine1
    private String mailingAddressLine2; //Territory.MailingAddress.MailingAddressLine2
    private String mailingAddressLine3; //Territory.MailingAddress.MailingAddressLine3
    private String city; //Territory.MailingAddress.City
    private String stateCodeFlexdata; //Territory.MailingAddress.StateCodeFlexdata
    private String zipCode; //Territory.MailingAddress.ZipCode
    private String zipCodeExtension; //Territory.MailingAddress.ZipCodeExtension
    private String primaryPhone; //Territory.Primary.PrimaryPhone
}