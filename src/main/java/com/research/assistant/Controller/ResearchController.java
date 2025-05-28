package com.research.assistant.Controller;

import com.research.assistant.dto.ResearchRequest;
import com.research.assistant.Service.ResearchService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*")
public class ResearchController {

    private final ResearchService researchService;
    public ResearchController(ResearchService researchService) {
        this.researchService = researchService;
    }

    @PostMapping(value = "/process", produces = "text/plain; charset=UTF-8")

    public ResponseEntity<String> processContent(@RequestBody ResearchRequest researchRequest){
            String result = researchService.processContent(researchRequest);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .body(result);
        }
    }


