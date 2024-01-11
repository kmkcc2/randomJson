package com.example.randomJson.randomObject;

import com.example.randomJson.randomGeoPosition.RandomGeoPosition;
import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/generate/json")
public class RandomObjectController {

    @GetMapping("/{size}")
    public List<RandomObject> getRandomObjects(@PathVariable int size) {
        Faker faker = new Faker();
        List randomObjectList = new ArrayList();
        for(int i = 0; i < size; i++) {
            RandomObject ro = new RandomObject(
                    faker.pokemon().name(),
                    faker.idNumber().hashCode(),
                    faker.random().hex().toLowerCase(Locale.ROOT),
                    faker.name().firstName(),
                    faker.name().fullName(),
                    faker.code().asin(),
                    faker.pokemon().name(),
                    faker.country().name(),
                    new RandomGeoPosition(
                            faker.number().randomDouble(10,0,100),
                            faker.number().randomDouble(10,0,100)
                            ),
                    faker.number().randomDigit(),
                    faker.bool().bool(),
                    faker.country().countryCode2(),
                    faker.bool().bool(),
                    faker.number().randomDouble(2,100,1000)
                    );
            randomObjectList.add(ro);
        }

        return randomObjectList;
    }

}
