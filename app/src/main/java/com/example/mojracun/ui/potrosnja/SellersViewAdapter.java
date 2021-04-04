package com.example.mojracun.ui.potrosnja;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.icu.lang.UCharacter.toLowerCase;


public class SellersViewAdapter extends RecyclerView.Adapter<SellersViewAdapter.ViewHolder> {

    private Context context;
    private List<Integer> colors;
    private List<String> labels;
    private List<String> receiptSums;
    private LayoutInflater mInflater;
    // data is passed into the constructor
    SellersViewAdapter(Context context, ArrayList<Integer> colors, ArrayList<String> labels, ArrayList<String> receiptSums) {
        this.mInflater = LayoutInflater.from(context);
        this.colors = colors;
        this.labels = labels;
        this.receiptSums = receiptSums;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_seller, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView_seller.setText(labels.get(position));
        String sum = receiptSums.get(position).toString();
        holder.textView_totalPrice.setText(receiptSums.get(position)+ " €");

        holder.textView_seller.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle,0,0,0);
        holder.textView_seller.setCompoundDrawablePadding(6);
        Drawable[] drawables = holder.textView_seller.getCompoundDrawables();
        for (Drawable drawable : drawables) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(colors.get(position), PorterDuff.Mode.SRC_IN));
            }
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return labels.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView_seller;
        public TextView textView_totalPrice;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            //    this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_seller = (TextView) itemView.findViewById(R.id.textView_seller);
            this.textView_totalPrice = itemView.findViewById(R.id.textview_totalPrice);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TO DO!!!!!!!!!!!!!!!!!!
        }
    }
}
