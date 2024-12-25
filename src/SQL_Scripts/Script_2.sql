CREATE TABLE IF NOT EXISTS seq_tracker (
    shard_id INT PRIMARY KEY,
    seq_id BIGINT NOT NULL DEFAULT 0
);

-- Insert the shard entry for shard ID 2
INSERT INTO seq_tracker (shard_id, seq_id) VALUES (1, 0)
ON DUPLICATE KEY UPDATE seq_id = seq_id;
