/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import dal.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import model.Product;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import model.Category;

public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();

        HttpSession session = request.getSession();
        List<Product> listProduct = (List<Product>) session.getAttribute("listProduct");

        if (listProduct == null) {
            listProduct = dao.findAll();
        }

        // để select category khi category ch đc tạo
        List<Category> listCategory = (List<Category>) session.getAttribute("listCategory");
        if (listCategory == null) {
            CategoryDAO cateDao = new CategoryDAO();
            listCategory = cateDao.findAll();
            session.setAttribute("listCategory", listCategory);
        }

        session.setAttribute("listProduct", listProduct);
        request.getRequestDispatcher("list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        List<Product> listProduct = null;

        try {
            switch (action) {
                case "search":
                    listProduct = searchProduct(request, response);
                    break;
                case "insert":
                    listProduct = insertProduct(request, response);
                    break;
                case "update":
                    listProduct = updateProduct(request, response);
                    break;
                case "delete":
                    listProduct = deleteProduct(request, response);
                    break;
                default:
                    throw new AssertionError();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        request.getSession().setAttribute("listProduct", listProduct);
        response.sendRedirect("list.jsp");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private List<Product> searchProduct(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("keyword");
        ProductDAO dao = new ProductDAO();
        List<Product> listProduct = dao.findByName(keyword);
        request.getSession().setAttribute("keyword", keyword);
        return listProduct;
    }

    private List<Product> insertProduct(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        ProductDAO dao = new ProductDAO();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String productName = request.getParameter("productName");
        String image = request.getParameter("image");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String categoryID = request.getParameter("categoryID");
        int status = Integer.parseInt(request.getParameter("status"));
        Date importDate = dateFormat.parse(request.getParameter("importDate"));
        Date usingDate = dateFormat.parse(request.getParameter("usingDate"));

        dao.insert(new Product(productName, image, price, quantity,
                categoryID, importDate, usingDate, status));
        return dao.findAll();
    }

    private List<Product> updateProduct(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        ProductDAO dao = new ProductDAO();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        int productID = Integer.parseInt(request.getParameter("productID"));
        String productName = request.getParameter("productName");
        String image = request.getParameter("image");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String categoryID = request.getParameter("categoryID");
        Date importDate = dateFormat.parse(request.getParameter("importDate"));
        Date usingDate = dateFormat.parse(request.getParameter("usingDate"));
        int status = Integer.parseInt(request.getParameter("status"));

        dao.update(new Product(productID, productName, image, price,
                quantity, categoryID, importDate, usingDate, status));
        return dao.findAll();
    }

    private List<Product> deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        ProductDAO dao = new ProductDAO();
        int productID = Integer.parseInt(request.getParameter("productID"));
        dao.deleteById(productID);
        return dao.findAll();
    }

}
