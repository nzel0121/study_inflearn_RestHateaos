package com.tklee.study.inflearnrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {


    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository,ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto){
        /* if not use modelMapper then do like that
        Event event = Event.builder()
                .name(eventDto.getName())
                .description(eventDto.getDescription())
                .beginEnrollmentDateTime(eventDto.getBeginEnrollmentDateTime())
                .endEnrollmentDateTime(eventDto.getEndEnrollmentDateTime())
                .beginEventDateTime(eventDto.getBeginEventDateTime())
                .endEventDateTime(eventDto.getEndEventDateTime())
                .location(eventDto.getLocation())
                .basePrice(eventDto.getBasePrice())
                .maxPrice(eventDto.getMaxPrice())
                .limitOfEnrollment(eventDto.getLimitOfEnrollment())
                .build();
        */
        Event event = modelMapper.map(eventDto,Event.class);
        Event newEvent = eventRepository.save(event);

        URI toUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(toUri).body(newEvent);
    }
}
