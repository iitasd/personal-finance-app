package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.CategoryManager;
import lk.ac.iit.finance.app.model.CategoryType;

import java.io.IOException;
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
        categoryManager.getIncomeCategoryList();
        categoryManager.getExpenseCategoryList();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CategoryManager categoryManager = CategoryManager.getInstance();
        String categoryName = req.getParameter("categoryName");
        String categoryDescription = req.getParameter("categoryDescription");
        String categoryType = req.getParameter("categoryType");
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("");
            return;
        }
        String userId = (String) session.getAttribute("userId");

        if (categoryType.equals(CategoryType.EXPENSE.toString())) {
            categoryManager.addExpenseCategory(categoryName, categoryDescription, userId, false, null);
            resp.sendRedirect("");
        } else if (categoryType.equals((CategoryType.INCOME.toString()))) {
            categoryManager.addIncomeCategory(categoryName, categoryDescription, userId, false);
            resp.sendRedirect("");
        } else {
            System.out.println("Invalid category type ..!!!");
            resp.sendRedirect("");
        }
    }
}