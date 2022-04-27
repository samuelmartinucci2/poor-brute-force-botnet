package com.samuel.distributed.brute.force.repository;

import com.samuel.distributed.brute.force.model.JobChunk;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JobChunkRepository extends CrudRepository<JobChunk, String> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query(value = "UPDATE job_chunk SET mdate = CURRENT_TIMESTAMP, run_id = :runId, status = 'IN_PROGRESS' " +
            "WHERE id = (SELECT id from job_chunk WHERE status = 'SCHEDULED' LIMIT 1)", nativeQuery = true)
    int reserve(@Param("runId") String runId);

    @Query(value = "SELECT * from job_chunk WHERE status = 'IN_PROGRESS'", nativeQuery = true)
    List<JobChunk> findScheduled();

    JobChunk findByRunId(String runId);

    @Modifying
    @Query(value = "UPDATE job_chunk SET status = 'SCHEDULED' WHERE status = 'IN_PROGRESS' AND mdate >= CURRENT_TIMESTAMP - INTERVAL 5 MINUTE", nativeQuery = true)
    void unlockExpiredChunks();

    @Modifying
    @Query(value = "DELETE FROM job_chunk WHERE job_id = (SELECT job_id from job_chunk WHERE id = :id)", nativeQuery = true)
    void deleteChunks(@Param("id") String id);
}
