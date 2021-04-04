package com.example.mojracun.ui.racuni;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojracun.R;
import com.example.mojracun.database.DataBaseHandler;
import com.example.mojracun.receipt.Receipt;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RacuniFragment extends Fragment {

    public static final String TABLE1_NAME = "RECEIPTS";
    RecyclerView recyclerViewReceipts;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_racuni, container, false);
        recyclerViewReceipts = (RecyclerView) root.findViewById(R.id.recyclerView);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh(){
        DataBaseHandler DB = new DataBaseHandler(getContext(), null, null, 1);
        List<Receipt> receipts = DB.readReceiptsData(TABLE1_NAME);

        recyclerViewReceipts.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), receipts);
        recyclerViewReceipts.setAdapter(adapter);
    }
}