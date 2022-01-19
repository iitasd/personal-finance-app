package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.BudgetManager;
import lk.ac.iit.finance.app.model.BudgetUsage;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/budgets", "/add-budget", "/delete-budget" })
public class BudgetController extends HttpServlet {

    private static final long serialVersionUID = -3207379419471628611L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("login.jsp");
            return;
        }

        BudgetManager budgetManager = BudgetManager.getInstance();
        String userId = (String) session.getAttribute("userId");
        String action = req.getServletPath();
        if ("/add-budget".equals(action)) {
            req.setAttribute("categories", budgetManager.getAllNotBudgetedCategories(userId));
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-budget.jsp");
            dispatcher.forward(req, resp);
        } else if ("/delete-budget".equals(action)) {
            String categoryId = req.getParameter("categoryId");
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                budgetManager.deleteBudget(categoryId);
            }
        }
        req.setAttribute("categories", budgetManager.getAllBudgetedCategories(userId));
        List<BudgetUsage> budgetUsages = budgetManager.listBudgetUsages(userId);
        if (budgetUsages.size() > 0) {
            req.setAttribute("budgetUsage",
                    budgetUsages.stream().collect(Collectors.toMap(BudgetUsage::getCategoryId, Function.identity())));
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("budget.jsp");
        dispatcher.forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("login.jsp");
            return;
        }

        String categoryId = req.getParameter("category");
        double maxSpending = Double.parseDouble(req.getParameter("limit"));
        String userId = (String) session.getAttribute("userId");

        BudgetManager budgetManager = BudgetManager.getInstance();
        budgetManager.addBudget(categoryId, maxSpending, userId);
        req.setAttribute("msg", "Budget for the category added successfully!");
        req.setAttribute("categories", budgetManager.getAllNotBudgetedCategories(userId));
        RequestDispatcher dispatcher = req.getRequestDispatcher("add-budget.jsp");
        dispatcher.forward(req, resp);
    }
}