package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.BudgetManager;
import lk.ac.iit.finance.app.manager.CategoryManager;
import lk.ac.iit.finance.app.model.AbstractCategory;
import lk.ac.iit.finance.app.model.CategoryType;
import lk.ac.iit.finance.app.model.ExpenseCategory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/budgets", "/add-budget"})
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

        String userId = (String) session.getAttribute("userId");
        List<ExpenseCategory> allBudget = BudgetManager.getInstance().getAllBudget(userId);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryId = req.getParameter("categoryId");
        double maxSpending = Double.parseDouble(req.getParameter("maxSpending"));

        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("login.jsp");
            return;
        }

        String userId = (String) session.getAttribute("userId");

        BudgetManager.getInstance().addBudget(categoryId, maxSpending, userId);
    }
}