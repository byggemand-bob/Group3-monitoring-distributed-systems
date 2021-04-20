package com.group3.monitorClient.messenger.messages;

import com.group3.monitorClient.AbstractSQLManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager_Test extends AbstractSQLManagerTest {
    /* verifies the CheckIfExists() is able to confirm the existence of a given table */
    @Test
    public void testCheckIfExistsTestPass () {
        //Setup
        String tableName = "test";
        sqlManager.CreateNewTable(tableName, "id integer PRIMARY KEY", "name text NOT NULL", "capacity real");

        //Act
        boolean test = sqlManager.CheckIfTableExists(tableName);

        //Assert
        Assertions.assertTrue(test);
    }

    /* verifies the function CheckIfExists() isn't able to find a none existent table */
    @Test
    public void testCheckIfExistsTestFail () {
        //Setup
        String tableName = "test";
        //Act

        //Assert
        Assertions.assertFalse(sqlManager.CheckIfTableExists(tableName));
        Assertions.assertFalse(sqlManager.CheckIfTableExists("queue"));
    }

    /* verifies the function CreateNewTable() constructs a table as specified */
    @Test
    public void testCreateNewTableTestPass () throws SQLException {
        //Setup
        String tableName = "test";
        String[] args = {"id integer PRIMARY KEY", "name text NOT NULL", "capacity real DEFAULT 1"};
        sqlManager.CreateNewTable(tableName, args);
        ResultSet rs = sqlManager.GenericStmt("PRAGMA table_info(" + tableName + ")");
        //Act
        rs.next();

        String[] column1 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key
        rs.next();

        String[] column2 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key

        rs.next();

        String[] column3 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key


        //Assert
        //verifies there wasn't more columns than 3 in the table
        Assertions.assertFalse(rs.next());
        //checks column1
        Assertions.assertEquals("id", column1[0]);
        Assertions.assertEquals("integer", column1[1]);
        Assertions.assertEquals("0", column1[2]);
        Assertions.assertNull(column1[3]);
        Assertions.assertEquals("1", column1[4]);
        //checks column2
        Assertions.assertEquals("name", column2[0]);
        Assertions.assertEquals("text", column2[1]);
        Assertions.assertEquals("1", column2[2]);
        Assertions.assertNull(column2[3]);
        Assertions.assertEquals("0", column2[4]);
        //checks column3
        Assertions.assertEquals("capacity", column3[0]);
        Assertions.assertEquals("real", column3[1]);
        Assertions.assertEquals("0", column3[2]);
        Assertions.assertEquals("1", column3[3]);
        Assertions.assertEquals("0", column3[4]);
    }

    /* test Delete() function */
    @Test
    public void TestDeletePass() throws SQLException {
        //Setup
        String tableName = "test";
        sqlManager.CreateNewTable(tableName, "ID integer PRIMARY KEY AUTOINCREMENT", "MessageNum integer NOT NULL");

        sqlManager.ExecutePreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(1)");
        sqlManager.ExecutePreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(2)");
        sqlManager.ExecutePreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(3)");

        //Act
        sqlManager.Delete(tableName,"MessageNum = '2'");

        ResultSet rs = sqlManager.Select(tableName);

        rs.next();
        int firstMessageNum = rs.getInt("MessageNum");

        rs.next();
        int secondMessageNum = rs.getInt("MessageNum");

        //Assert
        Assertions.assertEquals(1, firstMessageNum);
        Assertions.assertEquals(3, secondMessageNum);
    }

    /* test DeleteAll() function */
    @Test
    public void TestDeleteAllPass() throws SQLException {
        //Setup
        String tableName = "test";
        sqlManager.CreateNewTable(tableName, "ID integer PRIMARY KEY AUTOINCREMENT", "MessageNum integer NOT NULL");

        sqlManager.ExecutePreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(1)");
        sqlManager.ExecutePreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(2)");
        sqlManager.ExecutePreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(3)");

        //Act
        sqlManager.DeleteAll(tableName);
        ResultSet rs = sqlManager.SelectFirst(tableName);

        //Assert
        Assertions.assertFalse(rs.next());
    }

    /* verifies the ResetAutoIncrement() reset the autoincrement to the highest increment number in the table */
    @Test
    public void testResetAutoincrementWithDataPass() throws SQLException {
        //Setup
        String tableName = "test";
        sqlManager.CreateNewTable(tableName, "ID integer PRIMARY KEY AUTOINCREMENT", "MessageNum integer NOT NULL");

        //Act
        //should be assigned ID = 1
        PreparedStatement pstmt = sqlManager.getPreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(?)");
        pstmt.setInt(1, 1);
        pstmt.executeUpdate();

        //should be assigned ID = 2
        pstmt = sqlManager.getPreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(?)");
        pstmt.setInt(1, 2);
        pstmt.executeUpdate();

        //should be assigned ID = 3
        pstmt = sqlManager.getPreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(?)");
        pstmt.setInt(1, 3);
        pstmt.executeUpdate();

        //deletes message 2
        sqlManager.Delete(tableName, "MessageNum = '2'");
        sqlManager.ResetAutoIncrement(tableName);

        //highest ID still 3, should be assigned ID = 4
        pstmt = sqlManager.getPreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(?)");
        pstmt.setInt(1, 4);
        pstmt.executeUpdate();

        ResultSet rs = sqlManager.Select(tableName, "MessageNum = '4'");

        int message4ID = rs.getInt("ID");

        //deletes messages 2 and 4
        sqlManager.Delete(tableName, "MessageNum = '3'");
        sqlManager.Delete(tableName, "MessageNum = '4'");
        sqlManager.ResetAutoIncrement(tableName);

        //highest ID should now be 1, should be assigned ID = 2
        pstmt = sqlManager.getPreparedStmt("INSERT INTO "+tableName+"(MessageNum) VALUES(?)");
        pstmt.setInt(1, 5);
        pstmt.executeUpdate();

        rs = sqlManager.Select(tableName, "MessageNum = '5'");

        int message5ID = rs.getInt("ID");

        //Assert
        Assertions.assertEquals(4, message4ID);
        Assertions.assertEquals(2, message5ID);
    }
}
