package com.group3.monitorClient;

import com.group3.monitorClient.messenger.messages.SQLManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class AbstractSQLTest {
    public static SQLManager sqlManager;// = new SQLManager("src/main/resources/sqlite/db/", "test.db");

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
                System.out.println("Could not access database file and is therefore not deleted!");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
