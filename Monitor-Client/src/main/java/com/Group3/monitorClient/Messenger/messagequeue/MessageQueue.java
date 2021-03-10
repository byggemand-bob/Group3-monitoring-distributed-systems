package com.Group3.monitorClient.Messenger.messagequeue;

import com.Group3.monitorClient.Messenger.SecureSynchronizedQueue;

import java.sql.*;

public class MessageQueue extends SecureSynchronizedQueue<MessageInterface> {
    private String url;
    private String fileName;
    private Connection conn;

    public MessageQueue(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
        this.conn = connect();
        ResultSet rs = selectAll("queue", "*");
        try {
            if (rs != null && rs.next()) {
                reload(rs);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    protected void save(MessageInterface data) {

    }

    @Override
    protected void remove(MessageInterface data) {

    }

    private void reload(ResultSet rs){
        try {
            do {
                //TODO: load the queue
            } while (rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url+fileName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public ResultSet selectAll(String table,String... args){
        StringBuilder cols = new StringBuilder();
        for (String arg: args){
            cols.append(arg).append(",");
        }
        cols = new StringBuilder(cols.substring(0, cols.length() - 1));
        String sql = "SELECT "+cols+" FROM "+table;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void createNewTable(String table, String... args) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS warehouses (\n");
        for (String arg: args){
            sql.append(arg).append(", ");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 2));

        try (Connection conn = DriverManager.getConnection(url+fileName);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
