CREATE TABLE IF NOT EXISTS seq_counter (
    shard_id INT NOT NULL,
    seq_id BIGINT NOT NULL,
    userInsertion VARCHAR(255) NOT NULL,
    PRIMARY KEY (shard_id, seq_id)
);
