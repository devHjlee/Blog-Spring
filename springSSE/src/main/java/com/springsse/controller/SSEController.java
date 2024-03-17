package com.springsse.controller;

import com.springsse.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SSEController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(path = "/v1/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE+ ";charset=UTF-8")
    public ResponseEntity<SseEmitter> subscribe(@RequestParam String userId) {
        SseEmitter emitter = sseEmitterService.subscribe(userId);
        return ResponseEntity.ok(emitter);
    }

    @GetMapping(path = "/v1/subscribe/send")
    public ResponseEntity<SseEmitter> test(@RequestParam String userId) {
        sseEmitterService.publish(userId);
        return ResponseEntity.ok().build();
    }
}

