package com.Group3.monitorClient;

import com.Group3.monitorClient.Messenger.messageQueue.SQLManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AbstractSQLTest {
    public static SQLManager sqlManager;// = new SQLManager("src/main/resources/sqlite/db/", "test.db");

    @BeforeEach
    public void setup () {
        sqlManager = new SQLManager("src/main/resources/sqlite/db/", "test.db");
    }

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
