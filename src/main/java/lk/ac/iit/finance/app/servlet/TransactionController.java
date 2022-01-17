package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.CategoryManager;
import lk.ac.iit.finance.app.manager.TransactionManager;
import lk.ac.iit.finance.app.model.CategoryType;
import lk.ac.iit.finance.app.model.ExpenseCategory;
import lk.ac.iit.finance.app.model.IncomeCategory;
import lk.ac.iit.finance.app.model.RecurringPeriod;
import lk.ac.iit.finance.app.model.RecurringState;
import lk.ac.iit.finance.app.model.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/transactions", "/add-income", "/add-expense" })
public class TransactionController extends HttpServlet {

    private static final long serialVersionUID = 4338446574324616595L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("login.jsp");
            return;
        }

        CategoryManager categoryManager = CategoryManager.getInstance();
        String action = req.getServletPath();
        if ("/add-income".equals(action)) {
            req.setAttribute("categories", categoryManager.getIncomeCategoryList());
            req.setAttribute("transactionType", "Income");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-transaction.jsp");
            dispatcher.forward(req, resp);
        } else if ("/add-expense".equals(action)) {
            req.setAttribute("categories", categoryManager.getExpenseCategoryList());
            req.setAttribute("transactionType", "Expense");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-transaction.jsp");
            dispatcher.forward(req, resp);
        } else {
            req.setAttribute("transactions", TransactionManager.getInstance().getTransactions());
            RequestDispatcher dispatcher = req.getRequestDispatcher("transactions.jsp");
            dispatcher.forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No login session found..!!!");
            resp.sendRedirect("");
            return;
        }

        double amount = Double.parseDouble(req.getParameter("amount"));
        String transactionType = req.getParameter("transactionType");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(req.getParameter("date"), formatter);
        String note = req.getParameter("note");
        String userId = (String) session.getAttribute("userId");
        boolean isRecurring = "on".equals(req.getParameter("recurrence"));
        String frequency = "";
        int occurrenceCount = 0;
        if(isRecurring) {
            frequency = req.getParameter("frequency");
            occurrenceCount = Integer.parseInt(req.getParameter("occurrenceCount"));
        }
        String categoryId = req.getParameter("category");

        CategoryManager categoryManager = CategoryManager.getInstance();
        if (transactionType.equalsIgnoreCase(CategoryType.EXPENSE.toString())) {
            ExpenseCategory expenseCategory = CategoryManager.getInstance().getExpenseCategory(categoryId);
            RecurringState recurringState = getRecurringState(isRecurring, frequency, occurrenceCount);
            TransactionManager.getInstance()
                    .addExpense(amount, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), note, userId,
                            expenseCategory, recurringState);
            req.setAttribute("msg", "Expense added successfully!");
            req.setAttribute("categories", categoryManager.getExpenseCategoryList());
            req.setAttribute("transactionType", "Expense");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-transaction.jsp");
            dispatcher.forward(req, resp);
        } else if (transactionType.equalsIgnoreCase((CategoryType.INCOME.toString()))) {
            IncomeCategory incomeCategory = CategoryManager.getInstance().getIncomeCategory(categoryId);
            RecurringState recurringState = getRecurringState(isRecurring, frequency, occurrenceCount);
            TransactionManager.getInstance()
                    .addIncome(amount, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), note, userId,
                            incomeCategory, recurringState);
            req.setAttribute("msg", "Income added successfully!");
            req.setAttribute("categories", categoryManager.getIncomeCategoryList());
            req.setAttribute("transactionType", "Income");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-transaction.jsp");
            dispatcher.forward(req, resp);
        } else {
            req.setAttribute("errorMsg", "Failed to add transaction!");
            //TODO
            RequestDispatcher dispatcher = req.getRequestDispatcher("/transactions");
            dispatcher.forward(req, resp);
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