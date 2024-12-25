USE virtual_db1 ;
DELIMITER $$

DROP TRIGGER IF EXISTS before_insert_virtual_db1;
CREATE TRIGGER before_insert_virtual_db1
BEFORE INSERT ON seq_counter
FOR EACH ROW
BEGIN
    DECLARE new_seq_id BIGINT;

    -- Access the user_id from session variable
    DECLARE user_id VARCHAR(255);
    SET user_id = @user_id;  -- Retrieve user_id from session variable

    -- Validate if @user_id is not NULL
    IF user_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User ID session variable is not set.';
    END IF;

    -- Call the procedure to generate a new seq_id
    CALL generate_id_virtual_db1(user_id, new_seq_id);

    -- Assign the generated seq_id to the new row
    SET NEW.shard_id = 1;  -- Fixed shard ID for virtual_db1
    SET NEW.seq_id = new_seq_id;
END$$

DELIMITER ;
