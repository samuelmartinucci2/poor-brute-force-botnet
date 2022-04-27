package com.samuel.distributed.brute.force.service;

import com.samuel.distributed.brute.force.dto.JobChunkStatusDto;
import com.samuel.distributed.brute.force.model.ChunkStatus;
import com.samuel.distributed.brute.force.model.JobChunk;
import com.samuel.distributed.brute.force.repository.JobChunkRepository;
import com.samuel.distributed.brute.force.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChunkService {
    private final JobChunkRepository repository;
    private final JobRepository jobRepository;

    @Transactional
    public JobChunk getNextJob() {
        final String runId = UUID.randomUUID().toString();
        final int count = repository.reserve(runId);
        if (count == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return Optional.ofNullable(repository.findByRunId(runId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void reportStatus(final JobChunkStatusDto payload) {
        final JobChunk chunk = repository.findById(payload.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        chunk.setStatus(ChunkStatus.COMPLETED);
        if (payload.getStatus() == JobChunkStatusDto.ChunkStatus.SUCCESS) {
            chunk.getJob().setResolvedPassword(payload.getResolvedPassword());
            repository.deleteChunks(payload.getId());
        }
    }
}
