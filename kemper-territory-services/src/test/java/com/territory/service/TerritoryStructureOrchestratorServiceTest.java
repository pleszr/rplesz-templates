//package com.territory.service;
//
//import com.territory.TestUtil;
//import entity.Employee;
//import com.territory.Api2QueryException;
//import com.territory.ErrorCode;
//import control.MongoDBClient;
//import org.bson.Document;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//public class TerritoryStructureOrchestratorServiceTest {
//    @Nested
//    @SpringBootTest
//    class findCompanyIdByWorkDayIdTest {
//
//        @MockBean
//        private MongoDBClient mongoDBClient;
//
//        @Autowired
//        private TerritoryStructureOrchestratorService territoryStructureOrchestratorService;
//        @Test
//        void companyUuid_is_returned_if_workdayId_exists() {
//            //given
//            Document mockDocument = TestUtil.createDocumentFromJson("findCompanyIdByWorkDayIdTestSuccess.json");
//
//            //when
//            when(mongoDBClient.getCompanyUuidByWorkdayId("1030330")).thenReturn(Optional.ofNullable(mockDocument));
//
//            //then
//            assertEquals("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee", territoryStructureOrchestratorService.findCompanyUuidByWorkDayId("1030330"),"Expected companyUuid to be aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee but it is not");
//        }
//
//        @Test
//        void companyUuid_not_returned_if_workdayId_does_not_exist() {
//            //when
//            when(mongoDBClient.getCompanyUuidByWorkdayId("1030330")).thenReturn(Optional.empty());
//
//            Api2QueryException api2QueryException = assertThrows(
//                    Api2QueryException.class,
//                    () -> territoryStructureOrchestratorService.findCompanyUuidByWorkDayId("non_existing_workdayId"),
//                    "Expected findCompanyIdByWorkDayId to throw Api2QueryException, but it didn't"
//            );
//
//            assertEquals(
//                    ErrorCode.NOT_FOUND.toString(),
//                    api2QueryException.getErrorCode(),
//                    "Expected error code to be NOT_FOUND");
//            assertTrue(
//                    api2QueryException.getMessage().equalsIgnoreCase("Employee not found"),
//                    "Expected message to equal ignoring case 'Employee not found' but it did not");
//        }
//
//        @Test
//        void companyUuid_is_not_returned_if_workday_exists_but_companyUuid_is_not_present_in_doc() {
//            Document mockDocument = TestUtil.createDocumentFromJson("findCompanyIdByWorkDayIdTestSuccess.json");
//            TestUtil.renameDocument(mockDocument, "TerritoryService", "TerritoryServiceChangedOnPurpose");
//            when(mongoDBClient.getCompanyUuidByWorkdayId("non_existing_workdayId")).thenReturn(Optional.of(mockDocument));
//
//            Api2QueryException api2QueryException = assertThrows(
//                    Api2QueryException.class,
//                    () -> territoryStructureOrchestratorService.findCompanyUuidByWorkDayId("non_existing_workdayId"),
//                    "Expected findCompanyIdByWorkDayId to throw Api2QueryException, but it did not"
//            );
//
//            assertEquals(
//                    ErrorCode.NOT_FOUND.toString(),
//                    api2QueryException.getErrorCode(),
//                    "Expected error code to be NOT_FOUND");
//            assertTrue(
//                    api2QueryException.getMessage().equalsIgnoreCase("Employee not found"),
//                    "Expected message to equal ignoring case 'Employee not found' but it did not");
//        }
//    }
//
//    @Nested
//    @SpringBootTest
//    class getEmployeeByPayrollIdTest {
//        @MockBean
//        private MongoDBClient mongoDBClient;
//
//        @Autowired
//        private TerritoryStructureOrchestratorService territoryStructureOrchestratorService;
//
//        @Test
//        void employee_is_created_if_workdayId_exists() {
//            //given
//            Document mockDocument = TestUtil.createDocumentFromJson("employee-sample.json");
//            when(mongoDBClient.getEmployeeByWorkdayId("foundable_workdayId")).thenReturn(Optional.ofNullable(mockDocument));
//            //when
//            Employee employee = territoryStructureOrchestratorService.createEmployeeByWorkdayId("foundable_workdayId");
//
//            //then
//            assertAll(
//                    "Successful Employee mapping",
//                    () -> assertEquals("111055", employee.getID(), "Expected ID to be 111055 but it is not"),
//                    () -> assertEquals("9763D", employee.getPayrollEmpNumber(), "Expected PayrollEmpNumber to be 9763D but it is not"),
//                    () -> assertEquals("01", employee.getEmtpStatus(), "Expected EmtpStatus to be 01 but it is not"),
//                    () -> assertEquals("Agent", employee.getJobClass(), "Expected JobClass to be Agent but it is not"),
//                    () -> assertEquals("CARRIE", employee.getFirstName(), "Expected FirstName to be CARRIE but it is not"),
//                    () -> assertEquals("L", employee.getMiddleInitial(), "Expected MiddleInitial to be L but it is not"),
//                    () -> assertEquals("YOUNG", employee.getLastName(), "Expected LastName to be YOUNG but it is not"),
//                    () -> assertEquals("", employee.getEmail(), "Expected Email to be '' but it is not"),
//                    () -> assertEquals("7695280", employee.getPhoneNumber(), "Expected PhoneNumber to be 7695280 but it is not"),
//                    () -> assertEquals("", employee.getNPN(), "Expected NPN to be '' but it is not"),
//                    () -> assertEquals("AL", employee.getState(), "Expected State to be AL but it is not")
//            );
//        }
//        @Test
//        void employee_is_not_created_if_workdayId_doesnt_exist() {
//            //given
//            when(mongoDBClient.getEmployeeByWorkdayId("non_existing_workdayId")).thenReturn(Optional.empty());
//            //then
//            Api2QueryException api2QueryException = assertThrows(
//                    Api2QueryException.class,
//                    () -> territoryStructureOrchestratorService.createEmployeeByWorkdayId("non_existing_workdayId"),
//                    "Expected getEmployeeByPayrollIdTest to throw Api2QueryException, but it didn't"
//            );
//
//            assertEquals(
//                    ErrorCode.NOT_FOUND.toString(),
//                    api2QueryException.getErrorCode(),
//                    "Expected error code to be NOT_FOUND");
//            assertTrue(
//                    api2QueryException.getMessage().equalsIgnoreCase("Employee not found"),
//                    "Expected message to equal ignoring case 'Employee not found' but it did not");
//        }
//
//        @Test
//        void employee_is_not_created_if_EmployeeInformation_is_not_present_in_document() {
//            //given
//            Document mockDocument = TestUtil.createDocumentFromJson("employee-sample.json");
//            TestUtil.renameDocument(mockDocument, "EmployeeInformation", "EmployeeInformation_ChangedOnPurpose");
//            when(mongoDBClient.getEmployeeByWorkdayId("existing_workdayId")).thenReturn(Optional.of(mockDocument));
//            //then
//            Api2QueryException api2QueryException = assertThrows(
//                    Api2QueryException.class,
//                    () -> territoryStructureOrchestratorService.createEmployeeByWorkdayId("existing_workdayId"),
//                    "Expected getEmployeeByPayrollIdTest to throw Api2QueryException, but it didn't"
//            );
//
//            assertEquals(
//                    ErrorCode.NOT_FOUND.toString(),
//                    api2QueryException.getErrorCode(),
//                    "Expected error code to be NOT_FOUND");
//            assertTrue(
//                    api2QueryException.getMessage().equalsIgnoreCase("Employee not found"),
//                    "Expected message to equal ignoring case 'Employee not found' but it did not");
//        }
//        @Test
//        void employee_is_not_created_if_EmployeeJobClass_is_not_present_in_document() {
//            //given
//            Document mockDocument = TestUtil.createDocumentFromJson("employee-sample.json");
//            TestUtil.renameDocument(mockDocument, "EmployeeJobClass", "EmployeeJobClass_ChangedOnPurpose");
//            when(mongoDBClient.getEmployeeByWorkdayId("existing_workdayId")).thenReturn(Optional.of(mockDocument));
//            //then
//            Api2QueryException api2QueryException = assertThrows(
//                    Api2QueryException.class,
//                    () -> territoryStructureOrchestratorService.createEmployeeByWorkdayId("existing_workdayId"),
//                    "Expected getEmployeeByPayrollIdTest to throw Api2QueryException, but it didn't"
//            );
//
//            assertEquals(
//                    ErrorCode.NOT_FOUND.toString(),
//                    api2QueryException.getErrorCode(),
//                    "Expected error code to be NOT_FOUND");
//            assertTrue(
//                    api2QueryException.getMessage().equalsIgnoreCase("Employee not found"),
//                    "Expected message to equal ignoring case 'Employee not found' but it did not");
//        }
//        @Test
//        void employee_is_not_created_if_EmployeeStatus_is_not_present_in_document() {
//            //given
//            Document mockDocument = TestUtil.createDocumentFromJson("employee-sample.json");
//            TestUtil.renameDocument(mockDocument, "EmployeeStatus", "EmployeeStatus_ChangedOnPurpose");
//            when(mongoDBClient.getEmployeeByWorkdayId("existing_workdayId")).thenReturn(Optional.of(mockDocument));
//            //then
//            Api2QueryException api2QueryException = assertThrows(
//                    Api2QueryException.class,
//                    () -> territoryStructureOrchestratorService.createEmployeeByWorkdayId("existing_workdayId"),
//                    "Expected getEmployeeByPayrollIdTest to throw Api2QueryException, but it didn't"
//            );
//
//            assertEquals(
//                    ErrorCode.NOT_FOUND.toString(),
//                    api2QueryException.getErrorCode(),
//                    "Expected error code to be NOT_FOUND");
//            assertTrue(
//                    api2QueryException.getMessage().equalsIgnoreCase("Employee not found"),
//                    "Expected message to equal ignoring case 'Employee not found' but it did not");
//        }
//
//
//    }
//
//}
