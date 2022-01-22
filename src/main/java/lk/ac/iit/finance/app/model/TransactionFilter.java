package lk.ac.iit.finance.app.model;

import java.util.List;

public interface TransactionFilter {
    List<Transaction> filterTransactions(List<Transaction> transactions);
}
