package com.samuel.distributed.brute.force.command.crunch;

import com.samuel.distributed.brute.force.command.CommandArguments;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@With
public class CrunchCommandArguments implements CommandArguments {
    @Builder.Default
    private final int count = 10_000;
    private final int minLength;
    private final int maxLength;
    @Builder.Default
    private String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789%$#@!";
    private final String offset;
    private final String pattern;

    @Override
    public List<String> command() {
        final List<String> commands = new ArrayList<>();
        commands.add(CrunchCommand.COMMAND);
        commands.add(String.valueOf(minLength));
        commands.add(String.valueOf(maxLength));
        commands.add(characters);
        commands.add("-c");
        commands.add(String.valueOf(count));
        if (StringUtils.isNotBlank(offset)) {
            commands.add("-s");
            commands.add(offset);
        }
        if (StringUtils.isNotBlank(pattern)) {
            commands.add("-t");
            commands.add(pattern);
        }
        return commands;
    }
}
