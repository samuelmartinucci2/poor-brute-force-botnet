package com.samuel.distributed.brute.force.service;

import com.samuel.distributed.brute.force.model.Job;
import com.samuel.distributed.brute.force.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {
    private final JobRepository repository;

    public void create(final Job job) {
        repository.save(job);
    }

    public List<Job> list() {
        return repository.findAll();
    }
}
