package com.example.randomJsonToCsv.generateCsv;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/csv")
public class GenerateCsvController {
    @Autowired
    GenerateCsvService generateCsvService;

    @GetMapping("/")
    public List<String> getJsons() {
        List<String> listOfJsons = generateCsvService.getListOfJsons(5);
        System.out.println(listOfJsons);
        JSONObject jsonObject = new JSONObject(listOfJsons);
        for (int i = 0; i < listOfJsons.size(); i++) {
            String jsonString = listOfJsons.get(i).toString();
            JSONObject json = new JSONObject(jsonString);
            System.out.println(json.getString("type"));
        }
        return listOfJsons;
    }
}
