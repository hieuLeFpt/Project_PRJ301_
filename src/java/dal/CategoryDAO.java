/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import java.sql.*;

/**
 *
 * @author Trung Hieu
 */
public class CategoryDAO extends DBContext {

    public List<Category> findAll() {
        List<Category> listFound = new ArrayList<>();
        String sql = "SELECT [categoryID]\n"
                + "      ,[categoryName]\n"
                + "      ,[describe]\n"
                + "  FROM [dbo].[tblCategories]";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String categoryID = rs.getString("categoryID");
                String categoryName = rs.getString("categoryName");
                String describe = rs.getString("describe");
                listFound.add(new Category(categoryID, categoryName, describe));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }

    public List<Category> findByName(String categoryNameFind) {
        List<Category> listFound = new ArrayList<>();
        String sql = "SELECT [categoryID]\n"
                + "      ,[categoryName]\n"
                + "      ,[describe]\n"
                + "  FROM [dbo].[tblCategories] where [categoryName] like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, "%" + categoryNameFind.trim() + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String categoryID = rs.getString("categoryID");
                String categoryName = rs.getString("categoryName");
                String describe = rs.getString("describe");
                listFound.add(new Category(categoryID, categoryName, describe));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }

    public void insert(Category category) {
        String sql = "INSERT INTO [dbo].[tblCategories]\n"
                + "           ([categoryID]\n"
                + "           ,[categoryName]\n"
                + "           ,[describe])\n"
                + "     VALUES\n"
                + "          (?\n"
                + "           ,?\n"
                + "           ,?)";
        String sqlCheckDuplicate = "SELECT [categoryID]\n"
                + "      ,[categoryName]\n"
                + "      ,[describe]\n"
                + "  FROM [dbo].[tblCategories] where [categoryID] = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); PreparedStatement checkDuplicateStmt = connection.prepareStatement(sqlCheckDuplicate);) {
            checkDuplicateStmt.setObject(1, category.getCategoryID());
            ResultSet rsCheckDup = checkDuplicateStmt.executeQuery();
            if (rsCheckDup.next()) {
                return;
            }
            statement.setObject(1, category.getCategoryID());
            statement.setObject(2, category.getCategoryName());
            statement.setObject(3, category.getDescribe());
            statement.executeUpdate();
            //ResultSet rs = statement.getGeneratedKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Category category) {
        String sql = "UPDATE [dbo].[tblCategories]\n"
                + "   SET [categoryName] = ?\n"
                + "      ,[describe] = ?\n"
                + " WHERE [categoryID] like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, category.getCategoryName());
            statement.setObject(2, category.getDescribe());
            statement.setObject(3, category.getCategoryID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(Category category) {
        String sql = "DELETE FROM [dbo].[tblCategories]\n"
                + "      WHERE [categoryID] like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, category.getCategoryID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
