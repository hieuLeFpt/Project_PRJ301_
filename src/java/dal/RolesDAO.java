/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import model.Roles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Trung Hieu
 */
public class RolesDAO extends DBContext {

    public List<Roles> findAll() {
        List<Roles> listFound = new ArrayList<>();
        String sql = "SELECT [roleID]\n"
                + "      ,[roleName]\n"
                + "  FROM [dbo].[tblRoles]";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int roleID = rs.getInt("roleID");
                String roleName = rs.getString("roleName");
                listFound.add(new Roles(roleID, roleName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFound;
    }
}
