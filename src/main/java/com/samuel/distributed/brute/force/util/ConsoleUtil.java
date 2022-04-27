package com.samuel.distributed.brute.force.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.*;

@UtilityClass
public class ConsoleUtil {

    public String executeSync(final long timeout,
                              final TimeUnit timeUnit,
                              final OutputStream outputStream,
                              final String... commands) throws IOException, InterruptedException, ExecutionException {
        final ProcessBuilder pb = new ProcessBuilder(commands);
        final Process process = pb.start();
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        final Future<String> output = outputStream == OutputStream.ERROR ?
                pool.submit(() -> IOUtils.toString(process.getErrorStream(), Charset.defaultCharset())) :
                pool.submit(() -> IOUtils.toString(process.getInputStream(), Charset.defaultCharset()));

        pool.shutdown();

        if (timeout != 0 && timeUnit != null)
            if (!process.waitFor(timeout, timeUnit))
                process.destroy();
            else
                process.waitFor();

        return output.get();
    }

    public Process executeAsync(final String... commands) throws IOException {
        final ProcessBuilder pb = new ProcessBuilder(commands);
        return pb.start();
    }

    public enum OutputStream {
        ERROR, OUT
    }
}
