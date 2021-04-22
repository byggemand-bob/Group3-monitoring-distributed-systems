package com.group3.monitorClient;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;


@SpringBootApplication
public class TestCall2 {
    private static String url = "jdbc:sqlite:src/main/resources/sqlite/db/";
    private static String fileName = "test.db";

    public static void main(String[] args){
        createNewDatabase();
        CreateNewTable("queue", "id integer PRIMARY KEY", "name text NOT NULL", "capacity real");
        TestCall2 app = new TestCall2();
        // insert three new rows
//        app.insert("Raw Materials", 3000);
//        app.insert("Semifinished Goods", 4000);
//        app.insert("Finished Goods", 5000);
        System.out.println(app.CheckIfExists("queue"));
    }
    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url+fileName)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void CreateNewTable(String table, String... args) {
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

    /**
     * Insert a new row into the warehouses table
     *
     * @param name
     * @param capacity
     */
    public void insert(String name, double capacity) {
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

    private static Connection Connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url+fileName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    /**
     * select all rows in the warehouses table
     */
    public void selectAll(String table,String... args){
        String cols = "";
        for (String arg: args){
            cols += arg + ", ";
        }
        cols = cols.substring(0, cols.length()-2);
        String sql = "SELECT "+cols+" FROM "+table;

        try (Connection conn = this.Connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getClass());
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

    public static void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//    public String CreateMessage(){
//        String sqlQuery
//        return null;
//    }



}
