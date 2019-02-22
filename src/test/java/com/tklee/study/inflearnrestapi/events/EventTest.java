package com.tklee.study.inflearnrestapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        //Given
        Event event = new Event();
        String name = "name";
        String description = "description";

        //When
        event.setName(name);
        event.setDescription(description);

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void testFree(){
        //Given
        Event event = Event.builder()
                        .basePrice(0)
                        .maxPrice(0)
                        .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isTrue();

        event = Event.builder()
                    .basePrice(100)
                    .maxPrice(0)
                    .build();

        event.update();

        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline(){
        Event event = Event.builder()
                        .location("가양역 투썸")
                        .build();

        event.update();

        assertThat(event.isOffline()).isTrue();

        event = Event.builder()
                    .build();

        assertThat(event.isOffline()).isFalse();
    }

}