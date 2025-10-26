package com.mycompany.sistemaweb_gestion_ventas_licoreria_donpolo.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/ping")
    public String ping() {
        return "ping Spring Boot";
    }
}
