/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import model.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

/**
 *
 * @author Trung Hieu
 */
public class ProductDAO extends DBContext {

    public List<Product> findAll() {
        List<Product> listFound = new ArrayList<>();
        String sql = "SELECT *\n"
                + "  FROM [dbo].[tblProducts]";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("productID");
                String productName = rs.getString("productName");
                String image = rs.getString("image");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String categoryID = rs.getString("categoryID");
                Date importDate = rs.getDate("importDate");
                Date usingDate = rs.getDate("usingDate");
                int status = rs.getInt("status");
                listFound.add(new Product(productID, productName, image, price,
                        quantity, categoryID, importDate, usingDate, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }

    public List<Product> findByName(String keyword) {
        List<Product> listFound = new ArrayList<>();
        String sql = "SELECT [productID]\n"
                + "      ,[productName]\n"
                + "      ,[image]\n"
                + "      ,[price]\n"
                + "      ,[quantity]\n"
                + "      ,[categoryID]\n"
                + "      ,[importDate]\n"
                + "      ,[usingDate]\n"
                + "      ,[status]\n"
                + "  FROM [dbo].[tblProducts] where [productName] like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("productID");
                String productName = rs.getString("productName");
                String image = rs.getString("image");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String categoryID = rs.getString("categoryID");
                Date importDate = rs.getDate("importDate");
                Date usingDate = rs.getDate("usingDate");
                int status = rs.getInt("status");
                listFound.add(new Product(productID, productName, image, price,
                        quantity, categoryID, importDate, usingDate, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }

    public void insert(Product product) {
        String insertSql = "INSERT INTO [dbo].[tblProducts]\n"
                + "           ([productName]\n"
                + "           ,[image]\n"
                + "           ,[price]\n"
                + "           ,[quantity]\n"
                + "           ,[categoryID]\n"
                + "           ,[importDate]\n"
                + "           ,[usingDate]\n"
                + "           ,[status])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?)";
        String checkSql = "SELECT * FROM [dbo].[tblCategories] WHERE categoryID = ?";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(checkSql); 
                PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            checkStmt.setString(1, product.getCategoryID());
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                return;
            }

            insertStmt.setObject(1, product.getProductName());
            insertStmt.setObject(2, product.getImage());
            insertStmt.setDouble(3, product.getPrice());
            insertStmt.setObject(4, product.getQuantity());
            insertStmt.setObject(5, product.getCategoryID());
            insertStmt.setObject(6, product.getImportDate());
            insertStmt.setObject(7, product.getUsingDate());
            insertStmt.setObject(8, product.getStatus());
            insertStmt.executeUpdate();
            //ResultSet rs = statement.getGeneratedKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Product product) {
        String sql = "UPDATE [dbo].[tblProducts]\n"
                + "   SET [productName] = ?,\n"
                + "       [image] = ?,\n"
                + "       [price] = ?,\n"
                + "       [quantity] = ?,\n"
                + "       [categoryID] = ?,\n"
                + "       [importDate] = ?,\n"
                + "       [usingDate] = ?,\n"
                + "       [status] = ?\n"
                + " WHERE [productID] = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, product.getProductName());
            statement.setObject(2, product.getImage());
            statement.setObject(3, product.getPrice());
            statement.setObject(4, product.getQuantity());
            statement.setObject(5, product.getCategoryID());
            statement.setObject(6, product.getImportDate());
            statement.setObject(7, product.getUsingDate());
            statement.setObject(8, product.getStatus());
            statement.setObject(9, product.getProductID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeStatus(int productID, int status) {
        String sql = "UPDATE [dbo].[tblProducts]\n"
                + "   SET [status] = ?\n"
                + " WHERE productID=?";

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, status);
            ptm.setInt(2, productID);
            ptm.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void deleteById(int productID) {
        String deleteSql = "DELETE FROM [dbo].[tblProducts] WHERE productID = ?";
        String checkProductIdInOD = "SELECT *\n"
                    + "  FROM [dbo].[tblOrderDetails]\n"
                    + "  WHERE productID = ?";
        try (
                PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
                PreparedStatement checkStmt = connection.prepareStatement(checkProductIdInOD)) {
            checkStmt.setObject(1, productID);
            ResultSet rs = checkStmt.executeQuery();
            if(rs.next()){
                changeStatus(productID, 0);
                return;
            }
            deleteStmt.setInt(1, productID);
            deleteStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
