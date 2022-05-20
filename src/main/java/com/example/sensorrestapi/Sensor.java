package com.example.sensorrestapi;

import hirondelle.date4j.DateTime;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Entity
public class Sensor {
    @Id
    private int id;
    private String country;
    private String city;

    public Sensor(int id, String country, String city) {
        this.id = id;
        this.country = country;
        this.city = city;
    }

    public Sensor() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public JSONObject queryBetweenDates(String[] metrics, String startDate, String endDate) throws ParseException {
        // Verify dates are appropriate
        //TODO: Support more date formats
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        long dateRange = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if(dateRange > 30 || dateRange < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date range must be between 1 and 30 days");

        // Query each metric from sensor
        JSONObject output = new JSONObject();
        Random rand = new Random();
        for(String metric : metrics){
            // TODO: Implement API Call to sensor
            output.put(metric, rand.nextDouble() * 100);
        }
        return output;
    }

    public JSONObject query(String[] metrics) {
        JSONObject output = new JSONObject();
        Random rand = new Random();
        for(String metric : metrics){
            // TODO: Implement API Call to sensor
            output.put(metric, rand.nextDouble() * 100);
        }
        return output;
    }
}
