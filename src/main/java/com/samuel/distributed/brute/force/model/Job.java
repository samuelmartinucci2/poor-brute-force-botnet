package com.samuel.distributed.brute.force.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column
    private String username;

    @Column
    private String resolvedPassword;

    @Column
    private Integer minLength;

    @Column
    private Integer maxLength;

    @Column
    private String characters;

    @Column
    private String pattern;

    @Column
    private String startMarker;

    @Column
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.CREATED;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobChunk> chunks = new HashSet<>();

    public void updateSchedulingStatus(final String startMarker, final boolean hasMoreEntries) {
        if (hasMoreEntries) {
            setStartMarker(startMarker);
            return;
        }

        setStatus(JobStatus.SCHEDULED);
    }

}
