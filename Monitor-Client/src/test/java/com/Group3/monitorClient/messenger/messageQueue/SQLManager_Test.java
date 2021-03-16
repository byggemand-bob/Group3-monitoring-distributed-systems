package com.Group3.monitorClient.messenger.messageQueue;

import com.Group3.monitorClient.AbstractSQLTest;
import com.Group3.monitorClient.Messenger.messageQueue.SQLManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        String[] args = {"id integer PRIMARY KEY", "name text NOT NULL", "capacity real"};
        sqlManager.CreateNewTable(tableName, args);
        ResultSet rs = sqlManager.GenericSQLQuery("PRAGMA table_info(" + tableName + ")");
        //Act
        rs.next();

        String[] row1 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key
        rs.next();

        String[] row2 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key

        rs.next();

        String[] row3 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key


        //Assert
        //checks row1
        Assertions.assertEquals("id", row1[0]);
        Assertions.assertEquals("integer", row1[1]);
        Assertions.assertEquals("0", row1[2]);
        Assertions.assertEquals(null, row1[3]);
        Assertions.assertEquals("1", row1[4]);
        //checks row2
        Assertions.assertEquals("name", row2[0]);
        Assertions.assertEquals("text", row2[1]);
        Assertions.assertEquals("1", row2[2]);
        Assertions.assertEquals(null, row2[3]);
        Assertions.assertEquals("0", row2[4]);
        //checks row3
        Assertions.assertEquals("capacity", row3[0]);
        Assertions.assertEquals("real", row3[1]);
        Assertions.assertEquals("0", row3[2]);
        Assertions.assertEquals(null, row3[3]);
        Assertions.assertEquals("0", row3[4]);


    }


}
