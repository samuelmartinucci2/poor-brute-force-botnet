package com.samuel.distributed.brute.force.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobDto implements Serializable {
    private String id;
    private String username;
    private Integer minLength;
    private Integer maxLength;
    private String characters;
    private String pattern;
    private String resolvedPassword;
}
