package com.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "eventCreatorId")
    private Integer eventCreatorId;

    @Column(name = "eventName")
    private String eventName;

    @Column(name = "eventDescription")
    private String eventDescription;

    @Column(name = "eventDate")
    private String eventDate;

    @Column(name = "invitedIds")
    private List<Integer> invitedIds;

    @CreationTimestamp
    private Instant createdOn;

    @Lob
    @Column(name = "eventPicture")
    private byte[] eventPicture;

    public void inviteUser (Integer id) {
        if(!invitedIds.contains(id)) {
            invitedIds.add(id);
        }
    }

}
