package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.CategoryManager;
import lk.ac.iit.finance.app.model.AbstractCategory;
import lk.ac.iit.finance.app.model.CategoryType;
import lk.ac.iit.finance.app.task.TransactionScheduler;
import lk.ac.iit.finance.app.util.CategoryUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/categories", "/add-category", "/delete-category"})
@WebListener
public class CategoryController extends HttpServlet implements ServletContextListener {

    private static final long serialVersionUID = 1130564429969244567L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("login.jsp");
            return;
        }
        CategoryManager categoryManager = CategoryManager.getInstance();
        String userId = (String) session.getAttribute("userId");

        String action = req.getServletPath();
        if ("/add-category".equals(action)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-category.jsp");
            dispatcher.forward(req, resp);
        } else if ("/delete-category".equals(action)) {
            String categoryId = req.getParameter("categoryId");
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                categoryManager.deleteCategory(categoryId, userId);
            }
        }

        List<AbstractCategory> categories = new ArrayList<>();
        categories.addAll(categoryManager.getIncomeCategoryList(userId));
        categories.addAll(categoryManager.getExpenseCategoryList(userId));
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

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String userId = "d9e43010-ac22-479a-bd40-74b79de17dc3";
        CategoryUtil.addDefaultCategories(userId);
        TransactionScheduler transactionScheduler = new TransactionScheduler();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(transactionScheduler, TimeUnit.MINUTES.toMillis(1), TimeUnit.DAYS.toMillis(1));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}