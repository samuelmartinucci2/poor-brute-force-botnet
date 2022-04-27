package com.samuel.distributed.brute.force.controller;

import com.samuel.distributed.brute.force.dto.JobDto;
import com.samuel.distributed.brute.force.model.Job;
import com.samuel.distributed.brute.force.service.JobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {
    private final JobService service;
    private ModelMapper mapper = new ModelMapper();

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final JobDto job) {
        service.create(mapper.map(job, Job.class));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Iterable<JobDto>> list() {
        return ResponseEntity.ok(service.list().stream()
                .map(job -> mapper.map(job, JobDto.class)).collect(Collectors.toList()));
    }
}
