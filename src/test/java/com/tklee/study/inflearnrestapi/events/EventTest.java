package com.tklee.study.inflearnrestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
    @Parameters({
            "0,0,true",
            "100,0,false",
            "0,100,false"
    })
    public void testFree(int basePrice,int maxPrice,boolean isFree){
        //Given
        Event event = Event.builder()
                        .basePrice(basePrice)
                        .maxPrice(maxPrice)
                        .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    @Test
    @Parameters
    public void testOffline(String location,boolean isOffline){
        Event event = Event.builder()
                        .location(location)
                        .build();

        event.update();

        assertThat(event.isOffline()).isEqualTo(isOffline);
    }
    private Object[] parametersForTestOffline(){
        return new Object[]{
                new Object[]{"강남",true},
                new Object[]{null, false}
        };
    }

}