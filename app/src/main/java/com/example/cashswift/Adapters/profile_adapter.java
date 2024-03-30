package com.example.cashswift.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cashswift.Models.usersModel;
import com.example.cashswift.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class profile_adapter extends FirebaseRecyclerAdapter<usersModel,profile_adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public profile_adapter(@NonNull FirebaseRecyclerOptions<usersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull usersModel model) {
        holder.name.setText(model.getName());
        holder.upi.setText(model.getUpiId());
        holder.number.setText(model.getNumber());

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name,upi,number;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.profile_name);
            upi = (TextView) itemView.findViewById(R.id.profile_upi);
            number = (TextView) itemView.findViewById(R.id.profile_number);
        }
    }
}