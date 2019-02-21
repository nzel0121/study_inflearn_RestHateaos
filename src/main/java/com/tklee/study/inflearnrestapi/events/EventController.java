package com.tklee.study.inflearnrestapi.events;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
public class EventController {

    @PostMapping("/api/events/")
    public ResponseEntity createEvent(){
        URI toUri = linkTo(methodOn(EventController.class).createEvent()).slash("{ids}").toUri();
        return ResponseEntity.created(toUri).build();
    }
}
