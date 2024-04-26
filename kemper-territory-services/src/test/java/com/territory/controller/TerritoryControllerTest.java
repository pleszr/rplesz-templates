//package com.territory.controller;
//
//import com.jayway.jsonpath.JsonPath;
//import com.territory.Api2QueryException;
//import com.territory.ErrorCode;
//import com.territory.service.CompanyService;
//import control.TerritoryStructureOrchestratorService;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class TerritoryControllerTest {
//    @WebMvcTest(TerritoryController.class)
//    @Nested
//    class findCompanyIdByWorkDayIdTests {
//        @Autowired
//        private MockMvc mockMvc;
//
//        @MockBean
//        private TerritoryStructureOrchestratorService territoryStructureOrchestratorService;
//
//        @MockBean
//        private CompanyService companyService;
//
//        @Test
//        void companyUuid_is_sent_back_if_workdayId_Exists() throws Exception {
//            //given
//            String expectedUuid = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
//            when(companyService.findCompanyUuidByWorkDayId("17182")).thenReturn(expectedUuid);
//
//            //when
//            String responseJson = mockMvc.perform(MockMvcRequestBuilders.post("/apis/cps/territory/findCompanyIdByWorkdayId")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content("{\"workdayId\":17182}"))
//                    .andExpect(status().isOk())
//                    .andReturn()
//                    .getResponse()
//                    .getContentAsString();
//
//            //then
//            String uuidFromJson = JsonPath.read(responseJson, "$.companyUuid");
//            assertEquals(expectedUuid, uuidFromJson, "Expected to get aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee in $.companyUuid but got " + uuidFromJson);
//            verify(companyService, times(1)).findCompanyUuidByWorkDayId("17182");
//        }
//
//        @Test
//        void if_no_doc_with_workdayId_is_found_gives_back_404() throws Exception {
//            //given
//            when(companyService.findCompanyUuidByWorkDayId("44444")).thenThrow(new Api2QueryException(ErrorCode.NOT_FOUND, "Employee not found","Search found no document"));
//
//            //when
//            String responseJson = mockMvc.perform(MockMvcRequestBuilders.post("/apis/cps/territory/findCompanyIdByWorkdayId")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content("{\"workdayId\":44444}"))
//                    .andExpect(status().isNotFound())
//                    .andReturn()
//                    .getResponse()
//                    .getContentAsString();
//
//            //then
//            String detailFromJson = JsonPath.read(responseJson, "$.detail");
//            assertEquals("Employee not found", detailFromJson, "Expected to get 'Employee not found' in $.detail but got " + detailFromJson);
//            verify(companyService, times(1)).findCompanyUuidByWorkDayId("44444");
//        }
//    }
//}
