package com.example.cashswift.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cashswift.R;
import com.example.cashswift.Models.Transaction;

import java.util.List;

public class sent_adapter extends RecyclerView.Adapter<sent_adapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    // Constructor
    public sent_adapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_history_cardview, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView fromUser, toUser, description, amount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            fromUser = itemView.findViewById(R.id.all_history_sender);
            toUser = itemView.findViewById(R.id.all_history_receiver);
            description = itemView.findViewById(R.id.all_history_category);
            amount = itemView.findViewById(R.id.all_history_amount);
        }

        public void bind(Transaction transaction) {
            fromUser.setText(transaction.getFromUser());
            toUser.setText(transaction.getToUser());
            description.setText(transaction.getDescription());
            amount.setText(transaction.getAmount());
        }
    }
}
