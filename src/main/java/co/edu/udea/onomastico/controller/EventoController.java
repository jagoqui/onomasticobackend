package co.edu.udea.onomastico.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class EventoController {


    @RequestMapping("/")
    public String home(){
        return "Hello World!";
    }
}
