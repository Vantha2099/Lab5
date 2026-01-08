package com.example.lab4.model;

import java.util.ArrayList;
import java.util.List;

public class ExpenseData {

    private static final List<Expense> expenses = new ArrayList<>();

    static {
        // dummy sample data
        expenses.add(new Expense(1, "5.50", "USD", "2025-09-01", "Snack", "Coffee"));
        expenses.add(new Expense(2, "12.00", "USD", "2025-09-02", "Food", "Lunch"));
        expenses.add(new Expense(3, "60.00", "USD", "2025-09-03", "Transport", "Taxi fare"));
        expenses.add(new Expense(4, "25.00", "USD", "2025-09-04", "Groceries", "Supermarket"));
        expenses.add(new Expense(5, "100.00", "USD", "2025-09-05", "Shopping", "T-shirt"));
    }

    public static List<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }

    public static Expense findExpenseById(int id) {
        for (Expense e : expenses) {
            if (e.getId() == id) return e;
        }
        return null;
    }
}
