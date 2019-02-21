package com.tklee.study.inflearnrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {


    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository,ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
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
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto,errors);

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        Event event = modelMapper.map(eventDto,Event.class);
        Event newEvent = eventRepository.save(event);

        URI toUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(toUri).body(newEvent);
    }
}
