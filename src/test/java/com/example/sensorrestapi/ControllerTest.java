package com.example.sensorrestapi;

import com.example.sensorrestapi.controller.Controller;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class ControllerTest {

    @Autowired
    private Controller controller;

    @Autowired SensorRepository sensorRepository;

    @Test
    @Transactional
    void sensorCreation() {
        // Params
        int id = 1;
        String city = "london";
        String country = "england";
        Map<String, String> params = new HashMap();
        // Create sensor from params
        params.put("id", Integer.toString(id));
        params.put("city", city);
        params.put("country", country);
        Sensor sensor = controller.createSensor(params);
        try {
            // Test that sensor parameters match given values
            assertThat(sensor.getId()).isEqualTo(id);
            assertThat(sensor.getCity()).isEqualTo(city);
            assertThat(sensor.getCountry()).isEqualTo(country);
        } finally {
            sensorRepository.delete(sensor);
        }
    }

    @Test
    @Transactional
    void sensorQuerying() throws JSONException {
        // Create sensors for testing
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("city", "london");
        params.put("country", "england");
        Sensor sensor1 = controller.createSensor(params);

        params = new HashMap<>();
        params.put("id", "2");
        params.put("city", "dublin");
        params.put("country", "ireland");
        Sensor sensor2 = controller.createSensor(params);

        // Define params for query
        params = new HashMap<>();
        params.put("ids", "1,2");
        params.put("metrics", "temp,humidity");
        params.put("startdate", "1/3/2021");
        params.put("enddate", "30/3/2021");
        try {
            JSONObject output = new JSONObject(controller.querySensors(params));
            assertThat(output.has("1"));
            assertThat(output.has("2"));
            JSONObject output1 = (JSONObject) output.get("1");
            JSONObject output2 = (JSONObject) output.get("2");
            assertThat(output1.has("temp") && output1.has("humidity"));
            assertThat(output2.has("temp") && output2.has("humidity"));
        } finally {
            sensorRepository.delete(sensor1);
            sensorRepository.delete(sensor2);
        }
    }
    // TODO: Unit tests for edge cases such as date outside of range, future dates, invalid ids etc.
}
