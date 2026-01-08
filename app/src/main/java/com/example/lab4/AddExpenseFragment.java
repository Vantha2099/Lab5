package com.example.lab4;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.lab4.api.ExpenseApiService;
import com.example.lab4.api.ISO8601DateAdapter;
import com.example.lab4.api.RetrofitClient;
import com.example.lab4.model.Expense;
import com.google.firebase.auth.FirebaseAuth;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExpenseFragment extends Fragment {

    private EditText etAmount, etCurrency, etCategory, etRemark;
    private Button btnSave;
    private ExpenseApiService apiService;
    private static final String DB_NAME = "YOUR_GUID_HERE"; // Replace with your GUID

    public AddExpenseFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_expense, container, false);

        etAmount = v.findViewById(R.id.et_amount);
        etCurrency = v.findViewById(R.id.et_currency);
        etCategory = v.findViewById(R.id.et_category);
        etRemark = v.findViewById(R.id.et_remark);
        btnSave = v.findViewById(R.id.btn_save);

        apiService = RetrofitClient.getApiService();

        btnSave.setOnClickListener(view -> saveExpense());

        return v;
    }

    private void saveExpense() {
        String amount = etAmount.getText().toString().trim();
        String currency = etCurrency.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String remark = etRemark.getText().toString().trim();

        if (TextUtils.isEmpty(amount)) {
            etAmount.setError("Amount is required");
            return;
        }

        if (TextUtils.isEmpty(currency)) {
            etCurrency.setError("Currency is required");
            return;
        }

        if (TextUtils.isEmpty(category)) {
            etCategory.setError("Category is required");
            return;
        }

        btnSave.setEnabled(false);

        // Create expense object
        String id = UUID.randomUUID().toString();
        String createdBy = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String createdDate = ISO8601DateAdapter.getCurrentISO8601Timestamp();

        Expense expense = new Expense(id, amount, currency, createdDate, category, remark, createdBy);

        // Send POST request
        apiService.createExpense(DB_NAME, expense).enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                btnSave.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Expense added successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();

                    // Navigate to list fragment
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new ExpenseListFragment())
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Failed to add expense", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                btnSave.setEnabled(true);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        etAmount.setText("");
        etCurrency.setText("");
        etCategory.setText("");
        etRemark.setText("");
    }
}