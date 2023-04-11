package com.example.fbchatapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class MsgAdapter extends RecyclerView.Adapter {

    Activity activity;

    public ArrayList<MsgClass> msgClasses;

    FirebaseAuth auth;
    FirebaseDatabase database;


    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;
    boolean click = true;

    public MsgAdapter(Activity activity, ArrayList<MsgClass> msgClasses) {
        this.activity = activity;
        this.msgClasses = msgClasses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(activity).inflate(R.layout.sender, parent, false);
            return new senderVierwHolder(view);
        } else {
            View view = LayoutInflater.from(activity).inflate(R.layout.receiver, parent, false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        auth = FirebaseAuth.getInstance();

        MsgClass msgClass = msgClasses.get(position);
        String senderid = auth.getUid();
        String id = msgClass.getSenderId();
        String SenderId = senderid + id;
        String RecveiverId=id+senderid;


        database = FirebaseDatabase.getInstance();


        if (holder.getClass() == senderVierwHolder.class) {
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            viewHolder.msgtxt.setText(msgClass.getMessage());
            viewHolder.msgtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (click == true) {
                        viewHolder.tvTime.setText(msgClass.getTime());
                        viewHolder.tvTime.setVisibility(View.VISIBLE);
                        click = false;
                    } else {
                        viewHolder.tvTime.setVisibility(View.GONE);
                        click = true;

                    }
                }
            });

            ((senderVierwHolder) holder).msgtxt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    Toast.makeText(activity, "Long", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.AlertDialogCustom);
                    builder.setTitle("Delete Message");
                    builder.setMessage("Are You Sure To Delete This Message");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();

              return false;
                }
            });


        } else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(msgClass.getMessage());


            viewHolder.msgtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (click == true) {
                        viewHolder.tvTime.setText(msgClass.getTime());
                        viewHolder.tvTime.setVisibility(View.VISIBLE);
                        click = false;
                    } else {
                        viewHolder.tvTime.setVisibility(View.GONE);

                        click = true;
                    }
                }
            });

            ((reciverViewHolder) holder).msgtxt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {



                    return false;
                }
            });
        }

    }




    @Override
    public int getItemCount() {
        return msgClasses.size();
    }

    @Override
    public int getItemViewType(int position) {
        MsgClass messages = msgClasses.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    class senderVierwHolder extends RecyclerView.ViewHolder {

        TextView msgtxt, tvTime;
        RelativeLayout rlSender;

        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
            tvTime = itemView.findViewById(R.id.tvTime);
            rlSender = itemView.findViewById(R.id.rlSender);


        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder {
        TextView msgtxt, tvTime;
        RelativeLayout rlReceiver;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtxt = itemView.findViewById(R.id.recivertextset);
            tvTime = itemView.findViewById(R.id.tvTime);
            rlReceiver = itemView.findViewById(R.id.rlReceiver);
        }
    }
}
