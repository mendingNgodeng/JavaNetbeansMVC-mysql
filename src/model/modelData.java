/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import helper.koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class modelData {
      public List<Object[]> getAllData(String query, String[] columns) {

             List<Object[]> students = new ArrayList<>();

        try (Connection connection = koneksi.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Object[] rowData = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                rowData[i] = rs.getObject(columns[i]); // Fetch dynamically by column name
            }
                students.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
//      validate login
       public String validateUser(String username, String password, String query) {
        String role = null;

        try (Connection connection = koneksi.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("role"); // Get the user's role
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error: " + e.getMessage());
        }

        return role; // Return null if validation fails
    }
       

    public boolean unique(String value, String columnName, String query) {
          
    try ( Connection connection = koneksi.getConnection()) {
        PreparedStatement stmt = connection.prepareStatement(query);
        // Set the value to the query's parameter
        stmt.setString(1, value);
        
        // Execute the query
        try (ResultSet rs = stmt.executeQuery()) {
            // Return true if a match is found
            return rs.next();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
      
       public boolean insertData(String query,Object[] values) {
//        String query = "INSERT INTO students (nama, mobile, asal) VALUES (?, ?,?)";
        try (Connection connection = koneksi.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
          for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]); // 1-based index for SQL
        }
          
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }
       
       public boolean updateData(String query,Object[] values) {
//        String query = "UPDATE tb_student SET name = ?, email = ? WHERE id_student = ?";
         try (Connection connection = koneksi.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]); // 1-based index for SQL
        }

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
         
         
    }
       
       public boolean deleteData(String query,int id) {
    try {

        var connection = koneksi.getConnection();
        var preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        int rowsDeleted = preparedStatement.executeUpdate();
        return rowsDeleted > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
