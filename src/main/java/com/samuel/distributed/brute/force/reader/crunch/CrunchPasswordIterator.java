package com.samuel.distributed.brute.force.reader.crunch;

import com.samuel.distributed.brute.force.command.crunch.CrunchCommand;
import com.samuel.distributed.brute.force.command.crunch.CrunchCommandArguments;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class CrunchPasswordIterator implements Iterator<List<String>> {

    private final CrunchCommand command;
    private final CrunchCommandArguments arguments;

    private String offset;
    private boolean hasMoreEntries = true;

    @Override
    public boolean hasNext() {
        return hasMoreEntries;
    }

    @Override
    public synchronized List<String> next() {
        try {
            final CrunchCommandArguments args = StringUtils.isBlank(offset) ? arguments : arguments.withOffset(offset);
            final List<String> res = command.execute(args);
            hasMoreEntries = res.size() >= arguments.getCount();
            offset = res.remove(res.size() - 1);
            return res;
        } catch (final Exception e) {
            throw new RuntimeException("Error loading next portion of the batch.");
        }
    }
}
