package com.example.sensorrestapi.controller;

import com.example.sensorrestapi.Sensor;
import com.example.sensorrestapi.SensorRepository;
import hirondelle.date4j.DateTime;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.Map;
import java.util.TimeZone;

@RestController
public class Controller {

    @Autowired
    SensorRepository sensorRepository;

    @PostMapping("/addsensor")
    public Sensor createSensor(@RequestParam Map<String, String> params) {
        if (!params.containsKey("id") || !params.containsKey("city") || !params.containsKey("country"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id, city and country parameters must be provided.");
        int id;
        // Verify id is an integer and unique
        try {
            id = Integer.parseInt(params.get("id"));
            if (sensorRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id must take the form of a unique integer");
            }
        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id unable to be parsed correctly");
        }
            String country = params.get("country");
            String city = params.get("city");
            return sensorRepository.save(new Sensor(id, country, city));
    }

    @GetMapping("/query")
    public String querySensors(@RequestParam Map<String, String> params) {
        if (!params.containsKey("ids") || !params.containsKey("metrics"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ids, metrics, startdate and enddate parameters must be provided.");
        try {
            // Get Parameters from request
            String[] ids = params.get("ids").split(",");
            String[] metrics = params.get("metrics").split(",");
            String startDate;
            String endDate;

            // Check dates are within acceptable range
            // Query each sensor for each metric
            JSONObject output = new JSONObject();
            for (String idString : ids) {
                int id = Integer.parseInt(idString);
                if (!sensorRepository.existsById(id))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A queried sensor does not exist.");
                JSONObject innerObject;
                // Handle provided dates correctly
                if(!params.containsKey("startdate")) {
                    innerObject = sensorRepository.getById(id).query(metrics);
                }
                else if(!params.containsKey("enddate")) {
                    innerObject = sensorRepository.getById(id).queryBetweenDates(metrics,
                            params.get("startdate"), DateTime.now(TimeZone.getDefault()).toString());
                }
                else {
                    Sensor sensor = sensorRepository.getById(id);
                    innerObject = sensor.queryBetweenDates(metrics, params.get("startdate"), params.get("enddate"));
                }
                output.put(Integer.toString(id), innerObject);
            }
            return output.toString();

        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ids parameter must take the form of a comma separated list of integers");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
