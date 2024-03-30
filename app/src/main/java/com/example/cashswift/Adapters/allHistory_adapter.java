package com.example.cashswift.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cashswift.Models.transactionModel;
import com.example.cashswift.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class allHistory_adapter extends FirebaseRecyclerAdapter<transactionModel,allHistory_adapter.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public allHistory_adapter(@NonNull FirebaseRecyclerOptions<transactionModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull transactionModel model) {
        holder.fromUser.setText(model.getFromUser());
        holder.toUser.setText(model.getToUser());
        holder.Description.setText(model.getDescription());
        holder.amount.setText(model.getAmount());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_history_cardview,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView fromUser,toUser,Description,amount;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            fromUser = (TextView) itemView.findViewById(R.id.all_history_sender);
            toUser = (TextView) itemView.findViewById(R.id.all_history_receiver);
            Description = (TextView) itemView.findViewById(R.id.all_history_category);
            amount = (TextView) itemView.findViewById(R.id.all_history_amount);
        }
    }
}