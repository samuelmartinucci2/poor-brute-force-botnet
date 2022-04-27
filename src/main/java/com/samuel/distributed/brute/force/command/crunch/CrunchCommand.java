package com.samuel.distributed.brute.force.command.crunch;

import com.samuel.distributed.brute.force.command.Command;
import com.samuel.distributed.brute.force.util.ConsoleUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class CrunchCommand implements Command<CrunchCommandArguments> {
    static final String COMMAND = "crunch";

    @Override
    public boolean test() {
        try {
            final String testCrunch = ConsoleUtil.executeSync(
                    1, TimeUnit.SECONDS, ConsoleUtil.OutputStream.ERROR, COMMAND);
            return StringUtils.containsIgnoreCase(testCrunch, COMMAND);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> execute(final CrunchCommandArguments args) throws IOException, ExecutionException, InterruptedException {
        final List<String> commands = args.command();
        String response = ConsoleUtil.executeSync(0, TimeUnit.SECONDS,
                ConsoleUtil.OutputStream.OUT, commands.toArray(new String[0]));
        if (response == null || response.isEmpty())
            throw new IOException(String.format("Could not run %s", String.join(" ", commands)));

        return new ArrayList<>(Arrays.asList(response.split(System.lineSeparator())));
    }
}
