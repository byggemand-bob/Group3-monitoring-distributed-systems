package com.Group3.monitorClient.Messenger.messagequeue;

import java.sql.*;
public class SQLManager {
    private String url;
    private String fileName;
    private Connection conn;

    public SQLManager(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
        conn = Connect();
    }

    public Connection Connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url+fileName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public ResultSet SelectAll(String table, String... args){
        StringBuilder cols = new StringBuilder();
        for (String arg: args){
            cols.append(arg).append(",");
        }
        cols = new StringBuilder(cols.substring(0, cols.length() - 1));
        String sql = "SELECT "+cols+" FROM "+table;

        try (Connection conn = this.Connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void CreateNewTable(String table, String... args) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(table).append(" (\n");
        for (String arg: args){
            sql.append("\t").append(arg).append(",\n");
        }
        sql = new StringBuilder(sql.substring(0,sql.length()-2)).append("\n");
        sql.append(");");
        System.out.println(sql);

        try (Connection conn = DriverManager.getConnection(url+fileName);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Insert(String name, double capacity) {
        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

        try (Connection conn = this.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean CheckIfExists(String tableName) {
        String sql = "SELECT name FROM sqlite_master WHERE name = '" + tableName +"'";

        try (Connection conn = this.Connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void ResetAutoIncrement(String tableName) {
        String sql = "UPDATE sqlite_sequence SET seq = 0 WHERE name = " + tableName;

        try (Connection conn = this.Connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void SendMessage (MessageInterface message){
        String sqlMessage = message.CreateMessage();

        try (Connection conn = this.Connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlMessage)){
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
