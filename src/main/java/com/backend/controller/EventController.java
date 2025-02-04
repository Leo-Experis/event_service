package com.backend.controller;

import com.backend.model.Event;
import com.backend.repository.EventRepository;
import com.backend.responses.ErrorResponse;
import com.backend.responses.EventListResponse;
import com.backend.responses.EventResponse;
import com.backend.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("events")
public class EventController {

    @Autowired
    private EventRepository repository;


    @GetMapping
    public ResponseEntity<Response<?>> getAll() {
        List<Event> events = ResponseEntity.ok(repository.findAll()).getBody();
        EventListResponse eventListResponse = new EventListResponse();
        eventListResponse.set(events);
        return ResponseEntity.ok(eventListResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getById(@PathVariable Integer id) {
        Event event = this.repository.findById(id).orElse(null);
        if(event == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Event not Found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        EventResponse eventResponse = new EventResponse();
        eventResponse.set(event);
        return ResponseEntity.ok(eventResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody Event event) {
        Event newEvent = repository.save(event);
        EventResponse eventResponse = new EventResponse();
        eventResponse.set(newEvent);
        return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> update(@PathVariable Integer id, @RequestBody Event updatedEvent) {
        Event oldEvent = this.repository.findById(id).orElse(null);
        if(oldEvent == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Event not found by given ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        oldEvent.setEventName(updatedEvent.getEventName());
        oldEvent.setEventCreatorId(updatedEvent.getEventCreatorId());
        oldEvent.setEventDescription(updatedEvent.getEventDescription());
        oldEvent.setEventDate(updatedEvent.getEventDate());
        oldEvent.setInvitedIds(updatedEvent.getInvitedIds());

        Event newEvent = repository.save(oldEvent);
        EventResponse eventResponse = new EventResponse();
        eventResponse.set(newEvent);

        return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "addImage/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Response<?>> addImageToEvent(@PathVariable Integer id, @RequestParam("file")MultipartFile file) {
        Event oldEvent = repository.findById(id).orElse(null);
        if(oldEvent == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Event not found with ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        try {
            oldEvent.setEventPicture(file.getBytes());
            Event updatedEvent = repository.save(oldEvent);

            EventResponse eventResponse = new EventResponse();
            eventResponse.set(updatedEvent);
            return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("{eventId}/invite/{userId}")
    public ResponseEntity<Response<?>> inviteUser(@PathVariable Integer eventId, @PathVariable Integer userId) {
        Event event = repository.findById(eventId).orElse(null);
        if(event == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Event not found by given ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        event.inviteUser(userId);

        Event newEvent = repository.save(event);
        EventResponse eventResponse = new EventResponse();
        eventResponse.set(newEvent);

        return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
    }
}
