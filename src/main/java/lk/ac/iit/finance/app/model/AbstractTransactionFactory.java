package lk.ac.iit.finance.app.model;

import java.time.LocalDate;

/**
 * Abstraction transaction factory class
 */
public abstract class AbstractTransactionFactory {
    public abstract Transaction getTransaction(boolean isExpense, double amount, LocalDate date, String userId,
                                               TransactionCategory category, String note);
}
