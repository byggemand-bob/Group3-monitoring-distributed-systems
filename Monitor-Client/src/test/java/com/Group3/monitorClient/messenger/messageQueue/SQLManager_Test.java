package com.Group3.monitorClient.messenger.messageQueue;

import com.Group3.monitorClient.AbstractSQLTest;
import com.Group3.monitorClient.Messenger.messageQueue.SQLManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager_Test extends AbstractSQLTest {

    @Test
    public void testCheckIfExistsTestPass () {
        //Setup
        String tableName = "test";
        sqlManager.CreateNewTable(tableName, "id integer PRIMARY KEY", "name text NOT NULL", "capacity real");
        //Act

        //Assert
        Assertions.assertTrue(sqlManager.CheckIfExists(tableName));
    }

    @Test
    public void testCreateNewTableTestPass () throws SQLException {
        //Setup
        String tableName = "test";
        sqlManager.CreateNewTable(tableName, "id integer PRIMARY KEY", "name text NOT NULL", "capacity real");
        ResultSet rs = sqlManager.GenericSQLQuery("SELECT name FROM sqlite_master WHERE name = '" + tableName +"'");
//        ResultSet rs = sqlManager.GenericSQLQuery("pragma table_info(" + "queue" + ")");
//        ResultSet rs = sqlManager.GenericSQLQuery("SELECT COLUMN_NAME,* FROM sqlite_master.COLUMNS WHERE TABLE_NAME = " + "queue");
        boolean result;
        String col1,col2,col3;
        result = rs.next();
        result = sqlManager.CheckIfExists(tableName);

        //Act
        System.out.println(rs.next());
        System.out.println(rs);
        result = rs.next();
        col1 = rs.getString(0);
        col2 = rs.getString(1);
        col3 = rs.getString(2);

        System.out.println(rs);
        System.out.println(col1);
        System.out.println(col2);
        System.out.println(col3);
        //Assert
        Assertions.assertTrue(result);
    }


}
