package com.tklee.study.inflearnrestapi.events;

import com.tklee.study.inflearnrestapi.accounts.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime endEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // Optional if no this value then online
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @ManyToOne
    private Account manager;

    public void update() {
        if(this.basePrice == 0 && this.maxPrice == 0){
            this.free = true;
        }else{
            this.free = false;
        }

        if(this.location == null || this.location.isEmpty()){
            this.offline = false;
        }else{
            this.offline = true;
        }
    }
}
