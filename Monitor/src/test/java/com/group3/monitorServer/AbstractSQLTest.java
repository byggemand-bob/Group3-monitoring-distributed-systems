package com.group3.monitorServer;

import com.group3.monitorServer.controller.messages.SQLManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class AbstractSQLTest {
    public static SQLManager sqlManager;// = new SQLManager("src/main/resources/sqlite/db/", "test.db");

    @BeforeAll
    static void setupDir() {
        File path = new File("src/main/resources/sqlite/db/");
        path.mkdirs();
    }

    /* Creates a new sql database before each test */
    @BeforeEach
    public void setup () {
        sqlManager = new SQLManager("src/main/resources/sqlite/db/", "test.db");
    }

    /* Deletes the sql database after each test */
    @AfterEach
    public void cleanUp() {
        File db = new File(sqlManager.getPath()+sqlManager.getFileName());
        sqlManager.CloseConnection();
        try {
            if (!db.delete()) {
                System.out.println("AbstractSQLTest: Could not access database file. It was therefore not deleted!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
