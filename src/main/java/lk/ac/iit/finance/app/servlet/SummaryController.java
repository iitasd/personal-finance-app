package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.TransactionManager;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/summary")
public class SummaryController extends HttpServlet {

    private static final long serialVersionUID = 6530318633036168643L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        showSummary(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        showSummary(req, resp);
    }

    private void showSummary(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("login.jsp");
            return;
        }

        String userId = (String) session.getAttribute("userId");

        TransactionManager transactionManager = TransactionManager.getInstance();

        req.setAttribute("budget", transactionManager.getCurrentMonthBudgetStatus(userId));
        req.setAttribute("expense", transactionManager.getCurrentMonthExpense(userId));
        req.setAttribute("income", transactionManager.getCurrentMonthIncome(userId));
        req.setAttribute("balance",
                transactionManager.getCurrentMonthExpense(userId) - transactionManager.getCurrentMonthIncome(userId));
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }

}
