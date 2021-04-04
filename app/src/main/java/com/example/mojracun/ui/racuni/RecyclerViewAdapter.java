package com.example.mojracun.ui.racuni;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mojracun.R;
import com.example.mojracun.receipt.Receipt;
import com.example.mojracun.receipt.ReceiptViewActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

import static android.icu.lang.UCharacter.toLowerCase;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Receipt> mData;
    private LayoutInflater mInflater;
    // data is passed into the constructor
    RecyclerViewAdapter(Context context, List<Receipt> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_receipt, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String seller_name = normalizeString(mData.get(position).getSeller().getName());
        holder.textView_seller.setText(seller_name);
        holder.textView_totalPrice.setText(mData.get(position).getTotalPrice() +  " €");
        holder.textView_date.setText(mData.get(position).getDateTimeCreated().split("T")[0] );
        holder.textView_iic.setText(mData.get(position).getIic());

        String senderName = (String) holder.textView_seller.getText();
        String senderFirstLetter = (String) senderName.subSequence(0, 1);
        holder.textView_avatar.setText(senderFirstLetter);

        // Getting RANDOM collor acording to name
        GradientDrawable drawable = (GradientDrawable) holder.textView_avatar.getBackground();
        int senderNameInt = 0;
        for (int i=0; i<senderName.length()-2; i++) senderNameInt += senderName.charAt(i);
        Random randomColor = new Random(senderNameInt);
        int color = Color.argb(255, randomColor.nextInt(245),randomColor.nextInt(245),randomColor.nextInt(245));
        drawable.setColor(color);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView textView_seller;
        public TextView textView_totalPrice;
        public TextView textView_date;
        public TextView textView_avatar;
        public TextView textView_iic;


        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

        //    this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_seller = (TextView) itemView.findViewById(R.id.textView_seller);
            this.textView_totalPrice = (TextView) itemView.findViewById(R.id.textview_totalPrice);
            this.textView_date = (TextView) itemView.findViewById(R.id.textView_date);
            this.textView_avatar = itemView.findViewById(R.id.textView_avatar);
            this.textView_iic = itemView.findViewById(R.id.textView_iic);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            textView_iic = view.findViewById(R.id.textView_iic);
            String iic = (String) textView_iic.getText();
            Intent intent = new Intent(context, ReceiptViewActivity.class);
            intent.putExtra("iic", iic);
            context.startActivity(intent);
        }
    }

    public static String normalizeString(String str){
        String strLower = str.toLowerCase();
        String words[]=strLower.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }
}