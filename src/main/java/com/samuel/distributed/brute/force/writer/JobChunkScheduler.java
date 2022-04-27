package com.samuel.distributed.brute.force.writer;

import com.google.common.collect.Iterables;
import com.samuel.distributed.brute.force.command.crunch.CrunchCommand;
import com.samuel.distributed.brute.force.command.crunch.CrunchCommandArguments;
import com.samuel.distributed.brute.force.model.Job;
import com.samuel.distributed.brute.force.model.JobChunk;
import com.samuel.distributed.brute.force.model.JobStatus;
import com.samuel.distributed.brute.force.repository.JobChunkRepository;
import com.samuel.distributed.brute.force.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobChunkScheduler {

    private static final int CRUNCH_BUFFER_SIZE = 100_000;
    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;
    private final JobChunkRepository chunkRepository;
    private final CrunchCommand command;

    @Scheduled(cron = "0 */1 * * * *")
    @SchedulerLock(name = "JobChunkScheduler_enqueuePasswordChunk", lockAtLeastFor = "1m")
    @Transactional
    public void enqueuePasswordChunk() {
        try {
            final Job job = jobRepository.findFirstByStatus(JobStatus.CREATED);
            if (job == null) return;

            final CrunchCommandArguments args = CrunchCommandArguments.builder()
                    .offset(job.getStartMarker())
                    .count(CRUNCH_BUFFER_SIZE)
                    .maxLength(job.getMaxLength())
                    .minLength(job.getMinLength())
                    .characters(job.getCharacters())
                    .pattern(job.getPattern())
                    .build();
            final List<String> passwordList = command.execute(args);
            final Iterator<List<String>> it = Iterables.partition(passwordList, CHUNK_SIZE).iterator();
            while (it.hasNext()) {
                List<String> chunk = it.next();
                if (!it.hasNext()) {
                    final boolean hasMoreEntries = chunk.size() >= CHUNK_SIZE;
                    chunk = new ArrayList<>(chunk);
                    final String lastMarker = hasMoreEntries ? chunk.remove(chunk.size() - 1) : null;
                    job.updateSchedulingStatus(lastMarker, hasMoreEntries);
                }

                chunkRepository.save(JobChunk.builder()
                        .job(job)
                        .mdate(new Date())
                        .passwordList(chunk)
                        .build());
            }

            jobRepository.save(job);
        } catch (final IOException | ExecutionException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error("Unable to process password list", ex);
        }

    }

}