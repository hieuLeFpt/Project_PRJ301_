/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import model.OrderDetails;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Trung Hieu
 */
public class OrderDetailsDAO extends DBContext {

    public List<OrderDetails> findAll() {
        List<OrderDetails> listFound = new ArrayList<>();
        String sql = "SELECT *\n"
                + "  FROM [dbo].[OrderDetails]";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int detailID = rs.getInt("detailID");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                int orderID = rs.getInt("orderID");
                int productID = rs.getInt("productID");
                listFound.add(new OrderDetails(detailID, price, quantity,
                        orderID, productID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }

    public OrderDetails findOrderDetailByOderID(int orderIDFind) {
        OrderDetails od = null;
        String sql = "SELECT [detailID]\n"
                + "      ,[price]\n"
                + "      ,[quantity]\n"
                + "      ,[orderID]\n"
                + "      ,[productID]\n"
                + "  FROM [dbo].[tblOrderDetails] where [orderID] = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, orderIDFind);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {  
                od = new OrderDetails();  
                od.setDetailID(rs.getInt("detailID"));
                od.setPrice(rs.getDouble("price"));
                od.setProductID(rs.getInt("productID"));
                od.setQuantity(rs.getInt("quantity"));
                od.setOrderID(rs.getInt("orderID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return od;
    }

}
