package eu.luminis.amsterdam.rest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @Secured("ROLE_USER")
    @RequestMapping("/hello-again")
    public String helloAgain() {
        return "hello again";
    }
}
