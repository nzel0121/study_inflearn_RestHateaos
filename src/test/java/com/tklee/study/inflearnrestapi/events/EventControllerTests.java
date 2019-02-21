package com.tklee.study.inflearnrestapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
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
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }

    @Test
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
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEvent_BadRequest_Invalid_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }
}
