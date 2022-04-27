package com.samuel.distributed.brute.force.config;

import com.samuel.distributed.brute.force.command.crunch.CrunchCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Configuration
@Slf4j
public class DependencyConfiguration {

    @Autowired
    public DependencyConfiguration(final CrunchCommand command) throws IOException, InterruptedException, ExecutionException {
        if (command.test()) {
            log.info("Crunch Found");
            return;
        }

        throw new RuntimeException("Crunch was not found on this machine!");
    }
}