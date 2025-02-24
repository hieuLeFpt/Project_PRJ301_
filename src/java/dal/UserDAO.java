/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.List;
import model.Users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Trung Hieu
 */
public class UserDAO extends DBContext {

    public List<Users> findAll() {
        List<Users> listFound = new ArrayList<>();
        String sql = "SELECt *"
                + "  FROM [dbo].[tblUsers]";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String userID = rs.getString("userID");
                String fullName = rs.getString("fullName").trim();
                String password = rs.getString("password").trim();
                int roleID = rs.getInt("roleID");
                String address = rs.getString("address");
                String phone = rs.getString("phone").trim();
                String email = rs.getString("email");
                boolean activate = rs.getBoolean("activate");
                listFound.add(new Users(userID, fullName, password,
                        roleID, address, phone, email, activate));
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
        return listFound;
    }

    public Users findByPhoneAndPassword(String phone, String password) {

        String sql = "SELECT [phone]\n"
                + "      ,[password]\n"
                + "  FROM [dbo].[tblUsers]\n"
                + "  where [phone] = ? and [password] = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, phone);
            statement.setObject(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String phone_Found = rs.getString("phone").trim();
                String password_Found = rs.getString("password").trim();
                Users user = new Users();
                user.setPhone(phone_Found);
                user.setPassword(password_Found);
                return user;
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    public List<Users> findByName(String userName) {
        List<Users> listFound = new ArrayList<>();
        String sql = "SELECT [userID]\n"
                + "      ,[fullName]\n"
                + "      ,[password]\n"
                + "      ,[roleID]\n"
                + "      ,[address]\n"
                + "      ,[phone]\n"
                + "      ,[email]\n"
                + "      ,[activate]\n"
                + "  FROM [dbo].[tblUsers] where [fullName] like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, "%" + userName.trim() + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String userID = rs.getString("userID");
                String fullName = rs.getString("fullName").trim();
                String password = rs.getString("password").trim();
                int roleID = rs.getInt("roleID");
                String address = rs.getString("address");
                String phone = rs.getString("phone").trim();
                String email = rs.getString("email");
                boolean activate = rs.getBoolean("activate");
                listFound.add(new Users(userID, fullName, password, roleID,
                        address, phone, email, activate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }

    public boolean isAdmin(String userName, String password) {
        String sql = "SELECT u.userID, u.fullName, u.password, u.roleID, r.roleName, \n"
                + "       u.address, u.phone, u.email, u.activate\n"
                + "FROM tblUsers u\n"
                + "INNER JOIN tblRoles r ON u.roleID = r.roleID\n"
                + "WHERE u.phone = ? AND u.password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1,userName);
            statement.setObject(2, password);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                int roleID = rs.getInt("roleID");
                return roleID == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insert(Users users) {
        String sql = "INSERT INTO [dbo].[tblUsers]\n"
                + "           ([userID]\n"
                + "           ,[fullName]\n"
                + "           ,[password]\n"
                + "           ,[roleID]\n"
                + "           ,[address]\n"
                + "           ,[phone]\n"
                + "           ,[email]\n"
                + "           ,[activate])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?)";
        String sqlCheckDup = "SELECT *\n"
                + "  FROM [dbo].[tblUsers] where userID like ?";

        try (PreparedStatement statement = connection.prepareStatement(sql); PreparedStatement checkDupSt = connection.prepareStatement(sqlCheckDup)) {
            checkDupSt.setObject(1, users.getUserID());
            ResultSet rsCheckDup = checkDupSt.executeQuery();
            if (rsCheckDup.next()) {
                return;
            }
            statement.setObject(1, users.getUserID());
            statement.setObject(2, users.getFullName());
            statement.setObject(3, users.getPassword());
            statement.setInt(4, users.getRoleID());
            statement.setObject(5, users.getAddress());
            statement.setObject(6, users.getPhone());
            statement.setObject(7, users.getEmail());
            statement.setObject(8, users.getActivate());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Users users) {
        String sql = "UPDATE [dbo].[tblUsers]\n"
                + "   SET [fullName] = ?\n"
                + "      ,[password] = ?\n"
                + "      ,[roleID] = ?\n"
                + "      ,[address] =?\n"
                + "      ,[phone] = ?\n"
                + "      ,[email] = ?\n"
                + "      ,[activate] = ?\n"
                + " WHERE [userID] like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(8, users.getUserID());
            statement.setObject(1, users.getFullName());
            statement.setObject(2, users.getPassword());
            statement.setInt(3, users.getRoleID());
            statement.setObject(4, users.getAddress());
            statement.setObject(5, users.getPhone());
            statement.setObject(6, users.getEmail());
            statement.setObject(7, users.getActivate());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(Users users) {
        String sql = "DELETE FROM [dbo].[tblUsers]\n"
                + "      WHERE userID like ?";
        String sqlCheck = "SELECT *\n"
                + "  FROM [dbo].[tblOrders] where [userID] = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql); PreparedStatement checkSt = connection.prepareStatement(sqlCheck)) {
            checkSt.setObject(1, users.getUserID());
            ResultSet rsCheck = checkSt.executeQuery();
            if (rsCheck.next()) {
                return;
            }
            statement.setObject(1, users.getUserID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
