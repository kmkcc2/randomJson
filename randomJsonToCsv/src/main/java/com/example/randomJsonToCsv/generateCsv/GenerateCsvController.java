package com.example.randomJsonToCsv.generateCsv;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/csv")
public class GenerateCsvController {
    @Autowired
    GenerateCsvService generateCsvService;

    @GetMapping("/")
    public void getCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=utf-8");
        List<String> listOfJsons = generateCsvService.getListOfJsons(5);
        String header = "type,_id,name,type,latitude,longitude";
        StringBuilder data = new StringBuilder();
        JSONObject jsonObject = new JSONObject(listOfJsons);
        for (int i = 0; i < listOfJsons.size(); i++) {
            String jsonString = listOfJsons.get(i).toString();
            JSONObject json = new JSONObject(jsonString);
            Arrays.stream(header.split(",")).forEach(e -> {
                if (e.equals("latitude") || e.equals("longitude")){
                    data.append(json.getJSONObject("geo_position").get(e) + ",");
                }else data.append(json.get(e) + ",");
            });
            data.deleteCharAt(data.length() - 1);
            if(i < listOfJsons.size()) data.append("\n");
        }
        header += "\n";
        response.getWriter().print(header+data);
    }

    @GetMapping("/specific/")
    public void getSpecyficCsv(HttpServletResponse response, @RequestParam String headers) throws IOException {
        List<String> listOfJsons = generateCsvService.getListOfJsons(5);
        List<String> columns = Arrays.asList(headers.split(",")).stream().map(String::trim).toList();
        StringBuilder header = new StringBuilder();
        columns.stream().forEach(e -> header.append(e + ","));
        header.deleteCharAt(header.length() - 1);
        header.append("\n");
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < listOfJsons.size(); i++) {
            String jsonString = listOfJsons.get(i).toString();
            JSONObject json = new JSONObject(jsonString);
            Iterator<String> iter = columns.iterator();
            while(iter.hasNext()) {
                String nextColumn = iter.next();
                if (json.has(nextColumn)){
                    data.append(json.get(nextColumn));
                    if (iter.hasNext()) data.append(",");
                } else if (json.getJSONObject("geo_position").has(nextColumn)){
                    data.append(json.getJSONObject("geo_position").get(nextColumn));
                    if (iter.hasNext()) data.append(",");
                } else {
                    response.setStatus(400);
                }
            }
            if(i < listOfJsons.size()) data.append("\n");
        }
        response.setContentType("text/csv; charset=utf-8");
        if (response.getStatus() == 400) {
            response.getWriter().print("Invalid columns specified");
        }else{
            response.getWriter().print(header.toString()+data);
        }
    }
}
