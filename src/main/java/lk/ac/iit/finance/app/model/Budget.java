package lk.ac.iit.finance.app.model;

public class Budget {

    private double maxSpending;

    public Budget(double maxSpending) {
        this.maxSpending = maxSpending;
    }

    public double getMaxSpending() {

        return maxSpending;
    }

    public void setMaxSpending(double maxSpending) {

        this.maxSpending = maxSpending;
    }
}
