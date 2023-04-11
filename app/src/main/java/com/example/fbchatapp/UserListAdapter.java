package com.example.fbchatapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Array;
import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    Activity activity;

    public ArrayList<UserClass> userClasses;

    public UserListAdapter(Activity activity, ArrayList<UserClass> userClasses) {
        this.activity = activity;
        this.userClasses = userClasses;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_adapter, parent, false);
        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        FirebaseAuth auth=FirebaseAuth.getInstance();


        if (userClasses.get(position).getID().trim().equals(auth.getUid())){
            holder.rlUser.setVisibility(View.GONE);
        }else {
            holder.rlUser.setVisibility(View.VISIBLE);
            holder.tvUserName.setText(userClasses.get(position).getUserName());

        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity,ChatMsgActivity.class);
                intent.putExtra("name",userClasses.get(position).getUserName());
                intent.putExtra("id",userClasses.get(position).getID());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName;

        RelativeLayout rlUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            rlUser=itemView.findViewById(R.id.rlUser);
        }
    }
}
