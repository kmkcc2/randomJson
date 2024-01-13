package com.example.randomJsonToCsv.generateCsv;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class GenerateCsvService {

    public List<String> getListOfJsons(int size)
    {
        final String uri = "http://localhost:8080/generate/json/"+size;

        RestTemplate restTemplate = new RestTemplate();
        String result;
        try{
            result = restTemplate.getForObject(uri, String.class);
            List<String> resultList = new ArrayList<>();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory factory = objectMapper.getFactory();
            JsonParser parser = factory.createParser(result);
            JsonNode jsonNode = objectMapper.readTree(parser);

            if (jsonNode.isArray()) {
                Iterator<JsonNode> elements = jsonNode.elements();

                while (elements.hasNext()) {
                    JsonNode element = elements.next();
                    resultList.add(element.toString());
                }
            }
            return resultList;
        } catch (ResourceAccessException | IOException e) {
            return null;
        }

    }
}
