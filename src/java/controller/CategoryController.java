/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.Category;

public class CategoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO dao = new CategoryDAO();
        HttpSession session = request.getSession();
        List<Category> listCategory = (List<Category>) session.getAttribute("listCategory");

        if (listCategory == null) {
            listCategory = dao.findAll();
        }
        session.setAttribute("listCategory", listCategory);
        request.getRequestDispatcher("category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null
                ? "" : request.getParameter("action");       
        List<Category> listCategory;
        switch (action) {
            case "search":
                listCategory = searchCategory(request, response);
                break;
            case "insert":
                listCategory = insertCategory(request, response);
                break;
            case "update":
                listCategory = updateCategory(request, response);
                break;
            case "delete":
                listCategory = deleteCategory(request, response);
                break;
            default:
                throw new AssertionError();
        }
        request.getSession().setAttribute("listCategory", listCategory);
        response.sendRedirect("category");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CategoryController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CategoryController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private List<Category> searchCategory(HttpServletRequest request, HttpServletResponse response) {
        CategoryDAO dao = new CategoryDAO();
        String categoryNameFind = request.getParameter("categoryName");
        List<Category> listCategory = dao.findByName(categoryNameFind);
        request.getSession().setAttribute("categoryName", categoryNameFind);
        return listCategory;
    }

    private List<Category> insertCategory(HttpServletRequest request, HttpServletResponse response) {
        CategoryDAO dao = new CategoryDAO();
        String categoryID = request.getParameter("categoryID");
        String categoryName = request.getParameter("categoryName");
        String describe = request.getParameter("describe");

        Category category = new Category(categoryID, categoryName, describe);
        dao.insert(category);
        return dao.findAll();
    }

    private List<Category> updateCategory(HttpServletRequest request, HttpServletResponse response) {
        CategoryDAO dao = new CategoryDAO();

        String categoryID = request.getParameter("categoryID");
        String categoryName = request.getParameter("categoryName");
        String describe = request.getParameter("describe");

        Category category = new Category(categoryID, categoryName, describe);
        dao.update(category);
        return dao.findAll();
    }

    private List<Category> deleteCategory(HttpServletRequest request, HttpServletResponse response) {
        CategoryDAO dao = new CategoryDAO();
        String id = request.getParameter("categoryID");
        Category category = new Category();
        category.setCategoryID(id);
        dao.deleteById(category);
        return dao.findAll();
    }
}
