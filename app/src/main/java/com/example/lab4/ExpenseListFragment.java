package com.example.lab4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab4.adapter.ExpenseAdapter;
import com.example.lab4.api.ExpenseApiService;
import com.example.lab4.api.RetrofitClient;
import com.example.lab4.model.Expense;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private List<Expense> expenseList = new ArrayList<>();
    private ExpenseApiService apiService;
    private static final String DB_NAME = "YOUR_GUID_HERE"; // Replace with your GUID

    // Pagination variables
    private int currentPage = 1;
    private int pageSize = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    public ExpenseListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expense_list, container, false);

        recyclerView = v.findViewById(R.id.recycler_expenses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        apiService = RetrofitClient.getApiService();

        adapter = new ExpenseAdapter(expenseList, getContext());
        recyclerView.setAdapter(adapter);

        // Load initial data
        loadExpenses(currentPage);

        // Setup infinite scroll pagination
        setupPagination(layoutManager);

        // Setup swipe to delete
        setupSwipeToDelete();

        return v;
    }

    private void loadExpenses(int page) {
        if (isLoading || isLastPage) return;

        isLoading = true;

        apiService.getExpenses(DB_NAME, page, pageSize).enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Expense> newExpenses = response.body();

                    if (newExpenses.isEmpty()) {
                        isLastPage = true;
                    } else {
                        int oldSize = expenseList.size();
                        expenseList.addAll(newExpenses);
                        adapter.notifyItemRangeInserted(oldSize, newExpenses.size());

                        if (newExpenses.size() < pageSize) {
                            isLastPage = true;
                        }
                    }

                    Log.d("ExpenseList", "Loaded page " + page + ", items: " + newExpenses.size());
                } else {
                    Toast.makeText(getContext(), "Failed to load expenses", Toast.LENGTH_SHORT).show();
                    Log.e("ExpenseList", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                isLoading = false;
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ExpenseList", "Error loading expenses", t);
            }
        });
    }

    private void setupPagination(LinearLayoutManager layoutManager) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        currentPage++;
                        loadExpenses(currentPage);
                        Log.d("ExpenseList", "Loading page: " + currentPage);
                    }
                }
            }
        });
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Expense expense = expenseList.get(position);

                deleteExpense(expense, position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void deleteExpense(Expense expense, int position) {
        apiService.deleteExpense(DB_NAME, expense.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    expenseList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(getContext(), "Expense deleted", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyItemChanged(position);
                    Toast.makeText(getContext(), "Failed to delete expense", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                adapter.notifyItemChanged(position);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}