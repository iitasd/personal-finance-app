package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.CategoryManager;
import lk.ac.iit.finance.app.model.AbstractCategory;
import lk.ac.iit.finance.app.model.CategoryType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/categories")
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CategoryManager categoryManager = CategoryManager.getInstance();
        List<AbstractCategory>  categories = new ArrayList<>();
        categories.addAll(categoryManager.getIncomeCategoryList());
        categories.addAll(categoryManager.getExpenseCategoryList());
        req.setAttribute("categories", categories);
        RequestDispatcher dispatcher = req.getRequestDispatcher("categories.jsp");
        dispatcher.forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CategoryManager categoryManager = CategoryManager.getInstance();
        String categoryName = req.getParameter("categoryName");
        String categoryDescription = req.getParameter("categoryDescription");
        String categoryType = req.getParameter("categoryType");
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("login.jsp");
            return;
        }

        String userId = (String) session.getAttribute("userId");

        if (categoryType.equalsIgnoreCase(CategoryType.EXPENSE.toString())) {
            categoryManager.addExpenseCategory(categoryName, categoryDescription, userId, false, null);
            req.setAttribute("msg", "Expense category created successfully!");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-category.jsp");
            dispatcher.forward(req, resp);
        } else if (categoryType.equalsIgnoreCase((CategoryType.INCOME.toString()))) {
            categoryManager.addIncomeCategory(categoryName, categoryDescription, userId, false);
            req.setAttribute("msg", "Income category created successfully!");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-category.jsp");
            dispatcher.forward(req, resp);
        } else {
            req.setAttribute("errorMsg", "Failed to add category!");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-category.jsp");
            dispatcher.forward(req, resp);
        }
    }
}