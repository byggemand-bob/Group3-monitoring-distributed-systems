package com.group3.monitorClient;

import com.group3.monitorClient.messenger.queue.PersistentSQLQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class AbstractPersistentSQLQueueTest {
    public PersistentSQLQueue messageQueue;

    @BeforeAll
    static void setupDir() {
        File path = new File("src/main/resources/sqlite/db/");
        path.mkdirs();
    }

    @BeforeEach
    public void Setup () {
        messageQueue = new PersistentSQLQueue("src/main/resources/sqlite/db/", "test.db");
    }

    @AfterEach
    public void CleanUp () {
        messageQueue.CloseConnection();
    }

    @AfterEach
    public void cleanUp() {
        File db = new File("src/main/resources/sqlite/db/", "test.db");
        messageQueue.CloseConnection();
        try {
            if (!db.delete()) {
                System.out.println("Could not access database file and is therefore not deleted!");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
