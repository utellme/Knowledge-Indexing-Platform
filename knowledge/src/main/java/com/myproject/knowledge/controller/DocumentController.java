package com.myproject.knowledge.controller;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.myproject.knowledge.context.SecurityContext;
import com.myproject.knowledge.model.DocumentRequest;
import com.myproject.knowledge.model.DocumentResponse;
import com.myproject.knowledge.service.DocumentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;


@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Long> ingest( @PathVariable String tenantId, 
        @Valid @RequestBody DocumentRequest doc) {
        if (!tenantId.equals(SecurityContext.getTenantId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Long id = service.save(tenantId, doc);
        return Map.of("documentId", id);
    }

     @GetMapping("/search")
    public List<DocumentResponse> search( @PathVariable String tenantId, 
        @RequestParam 
        @NotBlank(message = "query parameter 'q' must not be empty")
        String q, 
        @RequestParam(defaultValue = "10") 
        int limit,
         @RequestParam(defaultValue = "0") 
        int offset) {
        if (!tenantId.equals(SecurityContext.getTenantId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return service.search(tenantId, q, limit, offset);
    }
}
