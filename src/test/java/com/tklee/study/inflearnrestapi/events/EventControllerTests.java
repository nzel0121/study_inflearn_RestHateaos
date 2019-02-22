package com.tklee.study.inflearnrestapi.events;


import com.tklee.study.inflearnrestapi.common.BaseControllerTest;
import com.tklee.study.inflearnrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;


    @Test
    @TestDescription("정상적으로 이벤트 생성을 호출한 테스트")
    public void createEvent() throws Exception {
        //Given
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Develoment with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,02,21,14,00))
                .endEnrollmentDateTime(LocalDateTime.of(2019,02,21,14,00))
                .beginEventDateTime(LocalDateTime.of(2019,02,21,14,00))
                .endEventDateTime(LocalDateTime.of(2019,02,21,14,00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("가양역 우림비즈나인")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-event").description("link to update-event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("endEnrollmentDateTime").description("endEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("response Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("endEnrollmentDateTime").description("endEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("it tells if this event is free"),
                                fieldWithPath("offline").description("it tells if this event is offline"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("this link is self"),
                                fieldWithPath("_links.query-events.href").description("this link is query-events"),
                                fieldWithPath("_links.update-event.href").description("this link is update-event"),
                                fieldWithPath("_links.profile.href").description("this link is profile")

                        )
                ))
        ;

    }

    @Test
    @TestDescription("들어오면 안되는 케이스에 대해 BadRequest를 되돌려주는지에 대한 테스트")
    public void createEvent_BadRequest() throws Exception {
        //Given
        Event event = Event.builder()
                .id(10)
                .name("Spring")
                .description("REST API Develoment with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,02,21,14,00))
                .endEnrollmentDateTime(LocalDateTime.of(2019,02,21,14,00))
                .beginEventDateTime(LocalDateTime.of(2019,02,21,14,00))
                .endEventDateTime(LocalDateTime.of(2019,02,21,14,00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("가양역 우림비즈나인")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void createEvent_BadRequest_Invalid_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEvent_BadRequest_Wrong_Input() throws Exception {
        //Given
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Develoment with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,02,21,14,00))
                .endEnrollmentDateTime(LocalDateTime.of(2019,02,21,14,00))
                .beginEventDateTime(LocalDateTime.of(2019,02,21,14,00))
                .endEventDateTime(LocalDateTime.of(2019,02,20,14,00))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("가양역 우림비즈나인")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        IntStream.range(0,30).forEach(this::generateEvent);

        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size" ,"10")
                    .param("sort","name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"
                ))
        ;
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                        .name("event " + index)
                        .description("REST API Develoment with Spring")
                        .beginEnrollmentDateTime(LocalDateTime.of(2019,02,21,14,00))
                        .endEnrollmentDateTime(LocalDateTime.of(2019,02,21,14,00))
                        .beginEventDateTime(LocalDateTime.of(2019,02,21,14,00))
                        .endEventDateTime(LocalDateTime.of(2019,02,21,14,00))
                        .basePrice(100)
                        .maxPrice(200)
                        .limitOfEnrollment(100)
                        .location("가양역 우림비즈나인")
                        .free(false)
                        .offline(true)
                        .eventStatus(EventStatus.DRAFT)
                        .build();

        return this.eventRepository.save(event);
    }

    @Test
    @TestDescription("기존이벤트 1개 조회하기")
    public void getEvent() throws Exception {
        Event event = this.generateEvent(100);

        this.mockMvc.perform(get("/api/events/{id}",event.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("name").exists())
                    .andExpect(jsonPath("id").exists())
                    .andExpect(jsonPath("_links.self").exists())
                    .andExpect(jsonPath("_links.profile").exists())
                    .andDo(document("get-an-event"))
        ;

    }

    @Test
    @TestDescription("없는 이벤트를 조회시 404 에러")
    public void getEvent404() throws Exception {
        this.mockMvc.perform(get("/api/events/1183"))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("이벤트를 수정하기")
    public void updateEvent() throws Exception {
        Event event = this.generateEvent(100);
        EventDto eventDto = modelMapper.map(event,EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는경우 경우에 수정오류")
    public void updateEvent400_Empty() throws Exception {
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }
    @Test
    @TestDescription("입력값이 잘못되어있는 경우 경우에 수정오류")
    public void updateEvent400_Wrong() throws Exception {
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(200);
        eventDto.setMaxPrice(100);

        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("존재하지않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        this.mockMvc.perform(put("/api/events/1234",event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }
}


