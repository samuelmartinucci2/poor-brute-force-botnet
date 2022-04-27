package com.samuel.distributed.brute.force.controller;

import com.samuel.distributed.brute.force.dto.JobChunkDto;
import com.samuel.distributed.brute.force.dto.JobChunkStatusDto;
import com.samuel.distributed.brute.force.model.JobChunk;
import com.samuel.distributed.brute.force.service.ChunkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chunk")
public class ChunkController {
    private final ChunkService service;

    @PostMapping("/assign")
    public ResponseEntity<JobChunkDto> assign() {
        final JobChunk chunk = service.getNextJob();
        return ResponseEntity.ok(JobChunkDto.builder()
                .chunk(chunk.getPasswordList())
                .username(chunk.getJob().getUsername())
                .id(chunk.getId())
                .build());
    }

    @PostMapping("/report")
    public ResponseEntity<Void> report(@RequestBody final JobChunkStatusDto payload) {
        service.reportStatus(payload);
        return ResponseEntity.accepted().build();
    }
}
