package com.example.randomJson.randomObjectTests;

import com.example.randomJson.randomObject.RandomObject;
import com.example.randomJson.randomObject.RandomObjectController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RandomObjectTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RandomObjectController randomObjectController;


    @Test
    public void getRandomObjectsWithCertainSizeReturns200() throws Exception {
        int size = 5;
        List randomObjects = new ArrayList();
        for (int i = 0; i < size; i ++) randomObjects.add(new RandomObject());
        given(randomObjectController.getRandomObjects(size)).willReturn(randomObjects);
        mvc.perform(get("/generate/json/"+size)).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(5)));
    }
    @Test
    public void getRandomObjectsWithInvalidSizeReturns400() throws Exception {
        mvc.perform(get("/generate/json/1a")).andExpect(status().isBadRequest());
    }
}
