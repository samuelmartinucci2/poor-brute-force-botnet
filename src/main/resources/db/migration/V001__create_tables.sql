CREATE TABLE job (
    id VARCHAR(36) NOT NULL,
    username VARCHAR(100),
    resolved_password VARCHAR(100),
    min_length INTEGER,
    max_length INTEGER,
    characters VARCHAR(200),
    pattern VARCHAR(200),
    start_marker VARCHAR(200),
    status VARCHAR(20) DEFAULT 'CREATED',
    PRIMARY KEY (id)
);

CREATE TABLE job_chunk (
    id VARCHAR(36) NOT NULL,
    run_id VARCHAR(36),
    status VARCHAR(30) DEFAULT 'SCHEDULED',
    mdate TIMESTAMP,
    password_list CHARACTER VARYING,
    job_id VARCHAR(36),
    PRIMARY KEY (id)

);

ALTER TABLE job_chunk
ADD CONSTRAINT job_fk FOREIGN KEY (job_id) REFERENCES job;


CREATE TABLE shedlock(
    name VARCHAR(64) NOT NULL,
    lock_until TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);