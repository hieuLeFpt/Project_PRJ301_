/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Orders;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Trung Hieu
 */
public class OrdersDAO extends DBContext {

    public List<Orders> findAll() {
        List<Orders> listFound = new ArrayList<>();
        String sql = "SELECT *\n"
                + "  FROM [dbo].[tblOrders]";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                Date orderDate = rs.getDate("orderDate");
                double total = rs.getDouble("total");
                String userID = rs.getString("userID");
                listFound.add(new Orders(orderID, orderDate, total, userID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }

    public List<Orders> findByOrderID(int orderIDFind) {
        List<Orders> listFound = new ArrayList<>();
        String sql = "SELECT [orderID]\n"
                + "      ,[orderDate]\n"
                + "      ,[total]\n"
                + "      ,[userID]\n"
                + "  FROM [dbo].[tblOrders] where [orderID] = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, orderIDFind);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                Date orderDate = rs.getDate("orderDate");
                double total = rs.getDouble("total");
                String userID = rs.getString("userID");
                listFound.add(new Orders(orderID, orderDate, total, userID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }

    
}
