/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import helper.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Userr
 */
public class comboBoxDao {
    private koneksi connection;

    public comboBoxDao(koneksi connection) {
        this.connection = connection;
    }

    public List<Object[]> getAllData(String query, String[] columns) {
       List<Object[]> data = new ArrayList<>();
    try (Connection connection = koneksi.getConnection()) {
        PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Object[] row = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                row[i] = rs.getObject(columns[i]);
            }
            data.add(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return data;
    }
}
