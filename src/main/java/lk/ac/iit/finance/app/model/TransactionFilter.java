package lk.ac.iit.finance.app.model;

import java.util.List;

/**
 * Transaction filter interface.
 */
public interface TransactionFilter {
    List<Transaction> filterTransactions(List<Transaction> transactions);
}
