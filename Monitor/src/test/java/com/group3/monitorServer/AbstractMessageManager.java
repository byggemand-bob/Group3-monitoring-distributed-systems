package com.group3.monitorServer;

import com.group3.monitorServer.controller.messages.SQLManagerOLD;
import com.group3.monitorServer.controller.messages.SQLMessageManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class AbstractMessageManager {
    public static SQLMessageManager sqlMessageManager;// = new SQLManager("src/main/resources/sqlite/db/", "test.db");

    @BeforeAll
    static void setupDir() {
        File path = new File("src/main/resources/sqlite/db/");
        path.mkdirs();
    }

    /* Creates a new sql database before each test */
    @BeforeEach
    public void setup () {
        sqlMessageManager = new SQLMessageManager("src/main/resources/sqlite/db/", "test.db", "test");
    }

    /* Deletes the sql database after each test */
    @AfterEach
    public void cleanUp() {
        File db = new File(sqlMessageManager.getPath()+ sqlMessageManager.getFileName());
        sqlMessageManager.CloseConnection();
        try {
            if (!db.delete()) {
                System.out.println("AbstractSQLTest: Could not access database file. It was therefore not deleted!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
