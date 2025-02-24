/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.RolesDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;
import model.Roles;
import model.Users;

public class UsersController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO dao = new UserDAO();
        HttpSession session = request.getSession();
        List<Users> listUser = (List<Users>) session.getAttribute("listUser");

        if (listUser == null) {
            listUser = dao.findAll();
        }
        List<Roles> listRole = (List<Roles>) session.getAttribute("listRole");
        if (listRole == null) {
            RolesDAO roleDao = new RolesDAO();
            listRole = roleDao.findAll();
            session.setAttribute("listRole", listRole);
        }

        session.setAttribute("listUser", listUser);
        request.getRequestDispatcher("user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null
                ? "" : request.getParameter("action");
        List<Users> listUser = null;

        try {
            switch (action) {
                case "search":
                    listUser = searchUser(request, response);
                    break;
                case "insert":
                    listUser = insertUser(request, response);
                    break;
                case "update":
                    listUser = updateUser(request, response);
                    break;
                case "delete":
                    listUser = deleteUser(request, response);
                    break;
                default:
                    throw new AssertionError();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("listUser", listUser);
        response.sendRedirect("user");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UsersController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UsersController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private List<Users> searchUser(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("userName");
        UserDAO dao = new UserDAO();
        List<Users> listProduct = dao.findByName(userName);
        request.getSession().setAttribute("userName", userName);
        return listProduct;
    }

    private List<Users> insertUser(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        UserDAO dao = new UserDAO();

        String userID = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");
        int roleID = Integer.parseInt(request.getParameter("roleID"));
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        boolean activate = Boolean.parseBoolean(request.getParameter("status"));

        dao.insert(new Users(userID, fullName, password, roleID,
                address, phone, email, activate));
        return dao.findAll();
    }

    private List<Users> updateUser(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        UserDAO dao = new UserDAO();

        String userID = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");
        int roleID = Integer.parseInt(request.getParameter("roleID"));
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        boolean activate = Boolean.parseBoolean(request.getParameter("activate"));

        dao.update(new Users(userID, fullName, password, roleID,
                address, phone, email, activate));
        return dao.findAll();
    }

    private List<Users> deleteUser(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        UserDAO dao = new UserDAO();
        String userID = request.getParameter("userID");
        Users user = new Users();
        user.setUserID(userID);
        dao.deleteById(user);
        return dao.findAll();
    }


}
