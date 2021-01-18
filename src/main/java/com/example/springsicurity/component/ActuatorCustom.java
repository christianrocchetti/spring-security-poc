package com.example.springsicurity.component;

import org.springframework.boot.actuate.endpoint.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Endpoint(id = "actuator-custom")
public class ActuatorCustom {

    private Map<String, Boolean> test = new ConcurrentHashMap<>();

    @ReadOperation
    public Map<String, Boolean> features() {
        return test;
    }

    @ReadOperation
    public Boolean feature(@Selector String name) {
        return test.get(name);
    }

    // .csrf().disable() for enable @WriteOperation
    @WriteOperation
    public void configureFeature(@Selector String name, Boolean feature) {
        test.put(name, feature);
    }

    // .csrf().disable() for enable @DeleteOperation
    @DeleteOperation
    public void deleteFeature(@Selector String name) {
        test.remove(name);
    }


}
