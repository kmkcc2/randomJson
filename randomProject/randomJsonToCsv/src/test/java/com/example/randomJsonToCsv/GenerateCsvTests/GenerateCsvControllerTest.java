package com.example.randomJsonToCsv.GenerateCsvTests;

import com.example.randomJsonToCsv.generateCsv.GenerateCsvService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GenerateCsvControllerTest {

    private List defaultList;
    private List smallerList;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenerateCsvService generateCsvService;

    @BeforeEach
    public void setUp() {
        defaultList = new ArrayList();
        defaultList.add("{\"type\":\"\", \"_id\":\"\", \"name\":\"\", \"_type\":\"\", \"geo_position\": {\"latitude\":\"\", \"longitude\":\"\"}}");
        defaultList.add("{\"type\":\"\", \"_id\":\"\", \"name\":\"\", \"_type\":\"\", \"geo_position\": {\"latitude\":\"\", \"longitude\":\"\"}}");
        defaultList.add("{\"type\":\"\", \"_id\":\"\", \"name\":\"\", \"_type\":\"\", \"geo_position\": {\"latitude\":\"\", \"longitude\":\"\"}}");
        defaultList.add("{\"type\":\"\", \"_id\":\"\", \"name\":\"\", \"_type\":\"\", \"geo_position\": {\"latitude\":\"\", \"longitude\":\"\"}}");
        defaultList.add("{\"type\":\"\", \"_id\":\"\", \"name\":\"\", \"_type\":\"\", \"geo_position\": {\"latitude\":\"\", \"longitude\":\"\"}}");

        smallerList = new ArrayList();
        smallerList.add("{\"type\":\"\", \"_id\":\"\", \"name\":\"\", \"_type\":\"\", \"geo_position\": {\"latitude\":\"\", \"longitude\":\"\"}}");
        smallerList.add("{\"type\":\"\", \"_id\":\"\", \"name\":\"\", \"_type\":\"\", \"geo_position\": {\"latitude\":\"\", \"longitude\":\"\"}}");
    }

    @Test
    public void getCsvReturns200() throws Exception {
        given(generateCsvService.getListOfJsons(5)).willReturn(defaultList);
        MvcResult mvcResult = mvc.perform(get("/csv/")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/csv")))
                .andReturn();
        Assertions.assertEquals(6, mvcResult.getResponse().getContentAsString().split("\n").length);
    }

    @Test
    public void getCsvWithSpecifiedSizeReturnsValidCsv() throws Exception {
        given(generateCsvService.getListOfJsons(2)).willReturn(smallerList);
        MvcResult mvcResult = mvc.perform(get("/csv/?size=2")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/csv")))
                .andReturn();
        Assertions.assertEquals(3, mvcResult.getResponse().getContentAsString().split("\n").length);
    }

    @Test
    public void getSpecyficCsvReturnsValidCsv() throws Exception {
        given(generateCsvService.getListOfJsons(2)).willReturn(new ArrayList<>());
        MvcResult mvcResult = mvc.perform(get("/csv/specific/?headers=type")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/csv")))
                .andReturn();
        Assertions.assertEquals(1, mvcResult.getResponse().getContentAsString().split(",").length);
        Assertions.assertEquals("type", mvcResult.getResponse().getContentAsString().split(",")[0].trim());
    }

    @Test
    public void getCsvWhenServerIsNotRespondingReturns500() throws Exception {
        given(generateCsvService.getListOfJsons(5)).willReturn(null);
        MvcResult mvcResult = mvc.perform(get("/csv/specific/?headers=type")).andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.valueOf("text/plain")))
                .andReturn();
        Assertions.assertEquals("External server failure", mvcResult.getResponse().getContentAsString());
    }

}
