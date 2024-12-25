USE virtual_db1 ;
DELIMITER $$

CREATE PROCEDURE `generate_id_virtual_db1` (IN user_id VARCHAR(255), OUT result BIGINT)
BEGIN
    DECLARE our_epoch BIGINT DEFAULT 1314220021721;
    DECLARE now_millis BIGINT;
    DECLARE local_shard_id INT DEFAULT 1;  -- Fixed shard ID for virtual_db1
    DECLARE new_seq_id BIGINT;

    -- Fetch and increment the seq_id atomically (trigger locks this row)
    UPDATE seq_tracker
    SET seq_id = LAST_INSERT_ID(seq_id + 1)
    WHERE shard_id = local_shard_id;

    -- Fetch the updated sequence ID
    SET new_seq_id = LAST_INSERT_ID();

    -- Get the current timestamp in milliseconds
    SET now_millis = UNIX_TIMESTAMP(NOW(3)) * 1000;

    -- Generate the ID using the Instagram-like approach
    SET result = (now_millis - our_epoch) << 23;
    SET result = result | (local_shard_id << 10);
    SET result = result | new_seq_id;
END$$

DELIMITER ;
