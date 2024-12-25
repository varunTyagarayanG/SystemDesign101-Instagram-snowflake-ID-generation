# Instagram's snowflake ID generation

This project demonstrates a unique ID generation system similar to Instagram's snowflake ID generation, implemented in MySQL with a Java client application. The system ensures distributed unique IDs for database entries using a combination of timestamps, shard IDs, and sequence numbers.

## How Instagram Handles This Problem

Instagram generates unique IDs using a technique similar to Twitter's Snowflake. This method combines:

1. **Timestamp (41 bits)**: Represents the number of milliseconds since a custom epoch (e.g., 1314220021721). Ensures IDs are roughly sortable by creation time.
2. **Shard ID (13 bits)**: Helps in partitioning the data across multiple database instances, supporting up to 8192 shards.
3. **Sequence Number (10 bits)**: Prevents collision within the same shard and timestamp, allowing up to 1024 unique IDs per shard per millisecond.

### Performance

- Instagram's approach can generate up to **1024 IDs per millisecond per shard**, using a 10-bit sequence number.
- With 8192 shards (13 bits for shard ID), this system can generate over **8 million IDs per millisecond**.
## Project Structure

### SQL Scripts

1. **`script_1.sql`**: 
   - Drops and recreates the database `virtual_db1`.

2. **`script_2.sql`**: 
   - Creates the `seq_tracker` table to maintain sequence counters.
   - Inserts an initial shard entry for shard ID `1`.

3. **`script_3.sql`**: 
   - Creates the `seq_counter` table to store generated IDs and associated user data.

4. **`script_4.sql`**: 
   - Defines the stored procedure `generate_id_virtual_db1` to generate unique IDs based on timestamp, shard ID, and sequence number.

5. **`script_5.sql`**: 
   - Creates a trigger `before_insert_virtual_db1` to generate a unique ID for each new `seq_counter` entry.

### Java Code

#### `InstagramIDGenerator`
This Java program provides functionality to:
1. Insert a user into the database and generate a unique ID.
2. Fetch and display the contents of the `seq_counter` table.
3. Clear the `seq_counter` table.

#### `InstagramIDGeneratorConcurrentConncetions`
This Java program demonstrates concurrent user insertion using a thread pool.

### Key Features
- **Database-Locked ID Generation**: Ensures atomic updates to the sequence number using MySQL locks.
- **Epoch-Based Unique ID**: Combines a custom epoch timestamp, shard ID, and sequence number to ensure uniqueness.
- **Concurrency Handling**: Supports concurrent insertions with guaranteed unique ID generation.

## Setup Instructions

### Prerequisites
- MySQL 8.0 or higher.
- Java 8 or higher.
- JDBC Driver for MySQL.

### Steps

1. **Database Setup**:
   - Run the SQL scripts in the following order:

2. **Java Compilation and Execution**:
   - Compile the Java files:
   - Run the Java programs:

3. **Observe Output**:
   - Monitor the generated unique IDs and database contents via the program outputs.

## Database Tables

### `seq_tracker`
| Column     | Type    | Description                                      |
|------------|---------|--------------------------------------------------|
| `shard_id` | INT     | Unique ID of the shard.                         |
| `seq_id`   | BIGINT  | Sequence number for the shard.                  |

### `seq_counter`
| Column         | Type         | Description                                |
|----------------|--------------|--------------------------------------------|
| `shard_id`     | INT          | Shard ID of the entry.                     |
| `seq_id`       | BIGINT       | Unique ID generated for the entry.         |
| `userInsertion`| VARCHAR(255) | User ID associated with the generated ID.  |

## Key Concepts

### Snowflake ID Generation
- **Timestamp**: Encodes the current time relative to a custom epoch.
- **Shard ID**: Differentiates entries across shards.
- **Sequence Number**: Ensures uniqueness within the same timestamp and shard.

### MySQL Features Used
- **Triggers**: Automates ID generation before row insertion.
- **Stored Procedures**: Encapsulates the logic for ID generation.
- **Locks**: Ensures atomic updates to sequence counters.

### Java Features
- **Concurrency**: Demonstrates concurrent database operations using a thread pool.
- **JDBC**: Handles database connections and SQL execution.

## Future Enhancements
- Add support for dynamic shard IDs.
- Implement distributed systems integration for ID generation.
- Add logging and exception handling improvements.

## Authors
- [Varun Tyagarayan G](https://varuntyagarayanme.netlify.app/)


## License
This project is licensed under the MIT License.
