package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.manager.CategoryManager;
import lk.ac.iit.finance.app.manager.TransactionManager;
import lk.ac.iit.finance.app.model.CategoryType;
import lk.ac.iit.finance.app.model.ExpenseCategory;
import lk.ac.iit.finance.app.model.Income;
import lk.ac.iit.finance.app.model.IncomeCategory;
import lk.ac.iit.finance.app.model.RecurringPeriod;
import lk.ac.iit.finance.app.model.RecurringState;
import lk.ac.iit.finance.app.model.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {
        "/transactions", "/add-income", "/add-expense", "/recurring-transactions", "/edit-transaction"
})
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
        String userId = (String) session.getAttribute("userId");

        CategoryManager categoryManager = CategoryManager.getInstance();
        String action = req.getServletPath();
        if ("/add-income".equals(action)) {
            req.setAttribute("categories", categoryManager.getIncomeCategoryList(userId));
            req.setAttribute("transactionType", "Income");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-transaction.jsp");
            dispatcher.forward(req, resp);
        } else if ("/add-expense".equals(action)) {
            req.setAttribute("categories", categoryManager.getExpenseCategoryList(userId));
            req.setAttribute("transactionType", "Expense");
            RequestDispatcher dispatcher = req.getRequestDispatcher("add-transaction.jsp");
            dispatcher.forward(req, resp);
        } else if ("/recurring-transactions".equals(action)) {
            req.setAttribute("transactions", TransactionManager.getInstance().getRecursiveTransactions());
            RequestDispatcher dispatcher = req.getRequestDispatcher("recurring-transactions.jsp");
            dispatcher.forward(req, resp);
        } else if ("/edit-transaction".equals(action)) {
            String transactionId = req.getParameter("transactionId");
            boolean isRecurring = "true".equals(req.getParameter("recurring"));
            Transaction transaction;
            if (isRecurring) {
                req.setAttribute("recurring", "true");
                transaction = TransactionManager.getInstance().getRecurringTransaction(transactionId);
            } else {
                transaction = TransactionManager.getInstance().getTransaction(transactionId);
            }
            req.setAttribute("transaction", transaction);
            if (transaction instanceof Income) {
                req.setAttribute("transactionType", "Income");
            } else {
                req.setAttribute("transactionType", "Expense");
            }
            req.setAttribute("action", "edit");
            req.setAttribute("categories", categoryManager.getIncomeCategoryList(userId));
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

        String transactionId = req.getParameter("transactionId");

        double amount = Double.parseDouble(req.getParameter("amount"));
        String transactionType = req.getParameter("transactionType");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(req.getParameter("date"), formatter);
        String note = req.getParameter("note");
        String userId = (String) session.getAttribute("userId");
        boolean isRecurring = "on".equals(req.getParameter("recurrence"));
        String frequency = "";
        int occurrenceCount = 0;
        if (isRecurring) {
            frequency = req.getParameter("frequency");
            occurrenceCount = Integer.parseInt(req.getParameter("occurrenceCount"));
        }
        String categoryId = req.getParameter("category");
        boolean isEdit = "edit".equals(req.getParameter("operation"));

        CategoryManager categoryManager = CategoryManager.getInstance();
        if (transactionType.equalsIgnoreCase(CategoryType.EXPENSE.toString())) {
            ExpenseCategory expenseCategory = CategoryManager.getInstance().getExpenseCategory(categoryId);
            RecurringState recurringState = getRecurringState(isRecurring, frequency, occurrenceCount);
            if (isEdit) {
                if (recurringState.isRecurring()) {
                    TransactionManager.getInstance().editRecurringTransaction(transactionId, amount, note, recurringState);
                    resp.sendRedirect("recurring-transactions");
                } else {
                    TransactionManager.getInstance().editTransaction(transactionId, amount, date, note);
                    resp.sendRedirect("transactions");
                }
            } else {
                TransactionManager.getInstance()
                        .addExpense(amount, date, note, userId, expenseCategory, recurringState);
                req.setAttribute("msg", "Expense added successfully!");
                req.setAttribute("categories", categoryManager.getExpenseCategoryList(userId));
                req.setAttribute("transactionType", "Expense");
                RequestDispatcher dispatcher = req.getRequestDispatcher("add-transaction.jsp");
                dispatcher.forward(req, resp);
            }
        } else if (transactionType.equalsIgnoreCase((CategoryType.INCOME.toString()))) {
            IncomeCategory incomeCategory = CategoryManager.getInstance().getIncomeCategory(categoryId);
            RecurringState recurringState = getRecurringState(isRecurring, frequency, occurrenceCount);
            if (isEdit) {
                if (recurringState.isRecurring()) {
                    TransactionManager.getInstance().editRecurringTransaction(transactionId, amount, note, recurringState);
                    resp.sendRedirect("recurring-transactions");
                } else {
                    TransactionManager.getInstance().editTransaction(transactionId, amount, date, note);
                    resp.sendRedirect("transactions");
                }
            } else {
                TransactionManager.getInstance().addIncome(amount, date, note, userId, incomeCategory, recurringState);
                req.setAttribute("msg", "Income added successfully!");
                req.setAttribute("categories", categoryManager.getIncomeCategoryList(userId));
                req.setAttribute("transactionType", "Income");
                RequestDispatcher dispatcher = req.getRequestDispatcher("add-transaction.jsp");
                dispatcher.forward(req, resp);
            }
        } else {
            req.setAttribute("errorMsg", "Failed to add transaction!");
            //TODO
            RequestDispatcher dispatcher = req.getRequestDispatcher("/transactions");
            dispatcher.forward(req, resp);
        }
    }

    private RecurringState getRecurringState(boolean isRecurring, String period, int occurrenceCount) {
        if (!isRecurring) {
            return new RecurringState(false, null, 0);
        } else {
            return new RecurringState(true, RecurringPeriod.fromString(period), occurrenceCount);
        }
    }
}