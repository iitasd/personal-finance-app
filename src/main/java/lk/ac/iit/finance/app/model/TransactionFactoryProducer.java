package lk.ac.iit.finance.app.model;

public class TransactionFactoryProducer {

    public static AbstractTransactionFactory getFactory(RecurringState recurringState) {
        if (recurringState != null && recurringState.isRecurring()) {
            return new RecurringTransactionFactory(recurringState);
        } else {
            return new NonRecurringTransactionFactory();
        }
    }
}
