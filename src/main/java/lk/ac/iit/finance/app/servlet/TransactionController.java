package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.CategoryManager;
import lk.ac.iit.finance.app.manager.TransactionManager;
import lk.ac.iit.finance.app.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@WebServlet("/transactions")
public class TransactionController extends HttpServlet {

    private static final long serialVersionUID = 4338446574324616595L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        double amount = Double.parseDouble(req.getParameter("amount"));
        String categoryType = req.getParameter("categoryType");
        Date date = null;
        String note = req.getParameter("note");
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("");
            return;
        }
        String userId = (String) session.getAttribute("userId");
        boolean isRecurring = Boolean.parseBoolean(req.getParameter("isRecurring"));
        String period = req.getParameter("period");
        int occurrenceCount = Integer.parseInt(req.getParameter("occurrenceCount"));
        String categoryId = req.getParameter("categoryId");


        if (categoryType.equals(CategoryType.EXPENSE.toString())) {
            ExpenseCategory expenseCategory = CategoryManager.getInstance().getExpenseCategory(categoryId);
            RecurringState recurringState = getRecurringState(isRecurring, period, occurrenceCount);
            TransactionManager.getInstance().addExpense(amount, date, note, userId, expenseCategory, recurringState);
            resp.sendRedirect("");
        } else if (categoryType.equals((CategoryType.INCOME.toString()))) {
            IncomeCategory incomeCategory = CategoryManager.getInstance().getIncomeCategory(categoryId);
            RecurringState recurringState = getRecurringState(isRecurring, period, occurrenceCount);
            TransactionManager.getInstance().addIncome(amount, date, note, userId, incomeCategory, recurringState);
            resp.sendRedirect("");
        } else {
            System.out.println("Invalid transaction type ..!!!");
            resp.sendRedirect("");
        }
    }

    private RecurringState getRecurringState(boolean isRecurring, String period, int occurrenceCount) {
        if (!isRecurring) {
            return null;
        } else {
            return new RecurringState(true, RecurringPeriod.fromString(period), occurrenceCount);
        }
    }
}