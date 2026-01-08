package com.example.lab4.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.lab4.ExpenseDetailActivity;
import com.example.lab4.R;
import com.example.lab4.model.Expense;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Expense> items;
    private Context context;

    public ExpenseAdapter(List<Expense> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpenseAdapter.ViewHolder holder, int position) {
        Expense e = items.get(position);
        holder.tvAmount.setText(e.getAmount() + " " + e.getCurrency());
        holder.tvCategory.setText("Category: " + e.getCategory());
        holder.tvDate.setText("Date: " + e.getCreatedDate());
        holder.tvRemark.setText("Remark: " + e.getRemark());

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ExpenseDetailActivity.class);
            i.putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_ID, e.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvCategory, tvDate, tvRemark;
        ViewHolder(View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvRemark = itemView.findViewById(R.id.tv_remark);
        }
    }
}