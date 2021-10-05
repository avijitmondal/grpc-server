package com.avijitmondal.together.grpc.server.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadinessProbeController {

    @GetMapping("/")
    public String ready() {
        return "LIVE";
    }
}
