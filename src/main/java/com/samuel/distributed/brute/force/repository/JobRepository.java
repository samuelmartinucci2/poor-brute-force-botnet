package com.samuel.distributed.brute.force.repository;

import com.samuel.distributed.brute.force.model.Job;
import com.samuel.distributed.brute.force.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    Job findFirstByStatus(JobStatus status);

    @Modifying
    @Query(value = "UPDATE job set resolved_password = :resolvedPassword, status = COMPLETED WHERE id = :jobId", nativeQuery = true)
    void markAsCompleted(@Param("jobId") String jobId, @Param("resolvedPassword") String resolvedPassword);
}
