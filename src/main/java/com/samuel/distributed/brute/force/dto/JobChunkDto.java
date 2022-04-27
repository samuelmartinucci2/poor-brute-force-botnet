package com.samuel.distributed.brute.force.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
public class JobChunkDto implements Serializable {
    private final String id;
    private final List<String> chunk;
    private final String username;
}
