package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.TransactionManager;

import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Summary controller servlet.
 */
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

        double income = transactionManager.getCurrentMonthIncome(userId);
        double expense = transactionManager.getCurrentMonthExpense(userId);

        req.setAttribute("budget", transactionManager.getCurrentMonthBudgetStatus(userId));
        req.setAttribute("expense", expense);
        req.setAttribute("income", income);
        req.setAttribute("balance", income - expense);

        int chartIncome = 0;
        int chartExpense = 0;
        if (income > 0 || expense > 0) {
            chartIncome = (int) ((income * 100) / (income + expense));
            chartExpense = 100 - chartIncome;
        }
        req.setAttribute("chartIncome", chartIncome);
        req.setAttribute("chartExpense", chartExpense);

        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);

        StringBuilder dateArray = new StringBuilder();
        StringBuilder incomeArray = new StringBuilder();
        StringBuilder expenseArray = new StringBuilder();

        int i = 1;
        for (LocalDate date = start; date.isBefore(today); date = date.plusDays(1)) {
            if (i > 1) {
                dateArray.append(",");
                incomeArray.append(",");
                expenseArray.append(",");
            }
            dateArray.append("\"").append(i).append("\"");
            incomeArray.append(transactionManager.getMonthlyIncomeToDate(userId, date));
            expenseArray.append(transactionManager.getMonthlyExpenseToDate(userId, date));
            i++;
        }

        req.setAttribute("dateArray", dateArray.toString());
        req.setAttribute("incomeArray", incomeArray.toString());
        req.setAttribute("expenseArray", expenseArray.toString());

        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }

}
