package it.gdhi.controller;

import it.gdhi.dto.AIRequest;
import it.gdhi.service.GdhmAiService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final GdhmAiService aiService;

    public AiController(GdhmAiService aiService) {
        this.aiService = aiService;
    }


    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@Valid @RequestBody AIRequest request) {

        return aiService.streamChat(request);
    }
}
