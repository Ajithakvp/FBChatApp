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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

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

            if (msgClass.isSeen()==true){

                viewHolder.ivread.setVisibility(View.VISIBLE);
                viewHolder.ivunread.setVisibility(View.GONE);

            }else {
                viewHolder.ivread.setVisibility(View.GONE);
                viewHolder.ivunread.setVisibility(View.VISIBLE);
            }

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.AlertDialogCustom);
                    builder.setTitle("Delete Message");
                    builder.setMessage("Are You Sure To Delete This Message");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DatabaseReference databaseReference = database.getReference().child("chat").child("message").child(msgClass.getMessage());
                            databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(activity, "Long", Toast.LENGTH_SHORT).show();

                                }
                            });





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
        ImageView ivread,ivunread;

        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
            tvTime = itemView.findViewById(R.id.tvTime);
            rlSender = itemView.findViewById(R.id.rlSender);
            ivunread=itemView.findViewById(R.id.ivunread);
            ivread=itemView.findViewById(R.id.ivread);



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
