package com.samuel.distributed.brute.force.command;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Command<T extends CommandArguments> {
    boolean test();

    List<String> execute(T args) throws IOException, ExecutionException, InterruptedException;
}
