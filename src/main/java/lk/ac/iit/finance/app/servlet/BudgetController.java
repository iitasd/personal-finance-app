package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.BudgetManager;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/budgets", "/add-budget" })
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
        } else {
            req.setAttribute("categories", budgetManager.getAllBudgetedCategories(userId));
            RequestDispatcher dispatcher = req.getRequestDispatcher("budget.jsp");
            dispatcher.forward(req, resp);
        }
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