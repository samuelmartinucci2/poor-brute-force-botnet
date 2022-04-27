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
public class ExpireChunkLockScheduler {

    private final JobChunkRepository repository;

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void enqueuePasswordChunk() {
        repository.unlockExpiredChunks();
    }

}