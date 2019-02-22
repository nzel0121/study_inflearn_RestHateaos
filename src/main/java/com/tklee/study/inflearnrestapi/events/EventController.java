package com.tklee.study.inflearnrestapi.events;

import com.tklee.study.inflearnrestapi.common.ErrorResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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
            return badRequest(errors);
        }

        eventValidator.validate(eventDto,errors);

        if(errors.hasErrors()){
            return badRequest(errors);
        }
        Event event = modelMapper.map(eventDto,Event.class);
        event.update();
        Event newEvent = eventRepository.save(event);

        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI toUri = selfLinkBuilder.toUri();

        EventResource eventResource = new EventResource(newEvent);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(new Link("/docs/index.html#resource-create-event").withRel("profile"));
        return ResponseEntity.created(toUri).body(eventResource);
    }

    private ResponseEntity<ErrorResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedResources<Resource<Event>> pagedResources = assembler.toResource(page , event -> new EventResource(event));
        pagedResources.add(new Link("/docs/index.html#resource-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(!optionalEvent.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#resource-get-an-event").withRel("profile"));
        return ResponseEntity.ok(eventResource);

    }
}
