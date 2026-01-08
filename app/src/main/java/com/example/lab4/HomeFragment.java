package com.example.lab4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.lab4.api.ExpenseApiService;
import com.example.lab4.api.RetrofitClient;
import com.example.lab4.model.Expense;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView tvAmount, tvCategory, tvDate, tvRemark;
    private ExpenseApiService apiService;
    private static final String DB_NAME = "38c225f5-2ef6-49b6-966a-383c894c9bf6"; // ⚠️ Replace with your actual GUID

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        tvAmount = v.findViewById(R.id.tv_latest_amount);
        tvCategory = v.findViewById(R.id.tv_latest_category);
        tvDate = v.findViewById(R.id.tv_latest_date);
        tvRemark = v.findViewById(R.id.tv_latest_remark);

        apiService = RetrofitClient.getApiService();

        // Load latest expense from API
        loadLatestExpense();

        return v;
    }

    private void loadLatestExpense() {
        // Fetch the first page with limit 1 to get the latest expense
        apiService.getExpenses(DB_NAME, 1, 1).enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Expense latest = response.body().get(0);
                    displayExpense(latest);
                } else {
                    tvAmount.setText("No expenses found");
                    tvCategory.setText("Add your first expense!");
                    tvDate.setText("");
                    tvRemark.setText("");
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Log.e("HomeFragment", "Error loading expense", t);
                tvAmount.setText("Error loading data");
                tvCategory.setText("Please check your connection");
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayExpense(Expense expense) {
        tvAmount.setText("Amount: " + expense.getAmount() + " " + expense.getCurrency());
        tvCategory.setText("Category: " + expense.getCategory());
        tvDate.setText("Date: " + expense.getCreatedDate());
        tvRemark.setText("Remark: " + expense.getRemark());
    }
}