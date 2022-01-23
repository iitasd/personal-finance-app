package lk.ac.iit.finance.app.task;

import lk.ac.iit.finance.app.manager.TransactionManager;

import java.util.TimerTask;

/**
 * Transaction scheduler class
 */
public class TransactionScheduler extends TimerTask {
    @Override
    public void run() {
        TransactionManager.getInstance().executeFutureRecursiveTransaction();
    }
}
