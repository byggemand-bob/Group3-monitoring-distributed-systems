package com.Group3.monitorClient;

import com.Group3.monitorClient.Messenger.messageQueue.SQLManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


import java.io.File;

public class AbstractSQLTest {
    public static SQLManager sqlManager;// = new SQLManager("src/main/resources/sqlite/db/", "test.db");

    @BeforeAll
    public static void setup () {
        sqlManager = new SQLManager("src/main/resources/sqlite/db/", "test.db");
    }


    @AfterAll
    public static void cleanUp() {
        File db = new File(sqlManager.getPath()+sqlManager.getFileName());
        try {
            db.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
