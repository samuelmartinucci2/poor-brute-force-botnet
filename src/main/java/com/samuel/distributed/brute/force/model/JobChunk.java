package com.samuel.distributed.brute.force.model;

import com.samuel.distributed.brute.force.model.helpers.StringListConverter;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobChunk {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column
    private String runId;

    @Column
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ChunkStatus status = ChunkStatus.SCHEDULED;

    @Column
    @Builder.Default
    private Date mdate = new Date();

    @Convert(converter = StringListConverter.class)
    @Column
    private List<String> passwordList;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

}
