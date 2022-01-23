package lk.ac.iit.finance.app.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User transaction filter class.
 */
public class UserTransactionFilter implements TransactionFilter {
    private String userId;

    public UserTransactionFilter(String userId) {
        this.userId = userId;
    }

    @Override
    public List<Transaction> filterTransactions(List<Transaction> transactions) {
        return transactions.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList());
    }
}
