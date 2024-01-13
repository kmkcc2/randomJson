package com.example.randomJsonToCsv.generateCsv;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/csv")
public class GenerateCsvController {
    @Autowired
    GenerateCsvService generateCsvService;

    @GetMapping("/")
    public ResponseEntity getCsv(@RequestParam(required = false) Optional<Integer> size) throws IOException {
        int listSize = size.orElse(5);
        List<String> listOfJsons = generateCsvService.getListOfJsons(listSize);
        if (listOfJsons == null){
            return ResponseEntity.internalServerError().contentType(MediaType.valueOf("text/plain")).body("External server failure");
        }
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
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/csv")).body(header+data);
    }

    @GetMapping("/specific/")
    public ResponseEntity getSpecyficCsv(@RequestParam String headers, @RequestParam(required = false) Optional<Integer> size) throws IOException {
        int listSize = size.orElse(5);
        List<String> listOfJsons = generateCsvService.getListOfJsons(listSize);
        if (listOfJsons == null){
            return ResponseEntity.internalServerError().contentType(MediaType.valueOf("text/plain")).body("External server failure");
        }
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
                    return ResponseEntity.badRequest().contentType(MediaType.valueOf("text/plain")).body("Invalid columns specified");
                }
            }
            if(i < listOfJsons.size()) data.append("\n");
        }
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/csv")).body(header.toString()+data);
    }
}
