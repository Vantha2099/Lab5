package com.example.lab4;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab4.api.ExpenseApiService;
import com.example.lab4.api.RetrofitClient;
import com.example.lab4.model.Expense;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EXPENSE_ID = "expense_id";
    private static final String DB_NAME = "YOUR_GUID_HERE"; // Replace with your GUID

    private TextView tvAmount, tvCurrency, tvDate, tvCategory, tvRemark, tvCreatedBy;
    private ExpenseApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_expense);

        tvAmount = findViewById(R.id.detail_amount);
        tvCurrency = findViewById(R.id.detail_currency);
        tvDate = findViewById(R.id.detail_date);
        tvCategory = findViewById(R.id.detail_category);
        tvRemark = findViewById(R.id.detail_remark);
        tvCreatedBy = findViewById(R.id.detail_created_by);

        apiService = RetrofitClient.getApiService();

        String expenseId = getIntent().getStringExtra(EXTRA_EXPENSE_ID);

        if (expenseId != null) {
            loadExpenseDetail(expenseId);
        } else {
            tvAmount.setText("Expense ID not provided");
        }
    }

    private void loadExpenseDetail(String expenseId) {
        apiService.getExpenseById(DB_NAME, expenseId).enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Expense expense = response.body();
                    displayExpense(expense);
                } else {
                    Toast.makeText(ExpenseDetailActivity.this, "Failed to load expense details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                Toast.makeText(ExpenseDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayExpense(Expense expense) {
        tvAmount.setText("Amount: " + expense.getAmount());
        tvCurrency.setText("Currency: " + expense.getCurrency());
        tvDate.setText("Date: " + expense.getCreatedDate());
        tvCategory.setText("Category: " + expense.getCategory());
        tvRemark.setText("Remark: " + expense.getRemark());
        tvCreatedBy.setText("Created By: " + expense.getCreatedBy());
    }
}