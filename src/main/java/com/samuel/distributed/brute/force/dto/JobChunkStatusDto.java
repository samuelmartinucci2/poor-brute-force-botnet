package com.samuel.distributed.brute.force.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Builder
@Data
public class JobChunkStatusDto implements Serializable {
    private final String id;
    private final ChunkStatus status;
    private final String resolvedPassword;

    public enum ChunkStatus {
        SUCCESS, FAILURE
    }
}
