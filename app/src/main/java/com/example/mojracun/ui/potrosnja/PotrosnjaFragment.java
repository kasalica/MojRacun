package com.example.mojracun.ui.potrosnja;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojracun.R;

import com.example.mojracun.database.DataBaseHandler;
import com.example.mojracun.receipt.Receipt;
import com.example.mojracun.receipt.Seller;
import com.example.mojracun.ui.racuni.RecyclerViewAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.mojracun.ui.racuni.RecyclerViewAdapter.normalizeString;


public class PotrosnjaFragment extends Fragment {

    public static final String TABLE1_NAME = "RECEIPTS";
    float sum = 0f;
    DecimalFormat df = new DecimalFormat("###.##");

    PieChart pieChart;
    TextView textViewPieChartCenter;
    RecyclerView recyclerViewSellers;
    ArrayList<String> labels;
    ArrayList<Integer> colors;
    ArrayList<String> receiptSums;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_potrosnja, container, false);

        pieChart = root.findViewById(R.id.pieChart_view);
        textViewPieChartCenter = root.findViewById(R.id.pieChartCenterAmount);
        recyclerViewSellers = (RecyclerView) root.findViewById(R.id.recyclerViewSellers);


        initPieChart();
        showPieChart();



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

        recyclerViewSellers.setLayoutManager(new LinearLayoutManager(getContext()));

       // SellersViewAdapter adapter = new SellersViewAdapter(getContext(), labels ,colors);
       // recyclerViewSellers.setAdapter(adapter);
    }


    private void initPieChart(){

        //rotation of chart
        pieChart.setRotationEnabled(true);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setRotationAngle(0);
        pieChart.animateY(1000, Easing.EaseInOutQuad);

        //highlight the entry when it is tapped, default true if not set
        pieChart.setHighlightPerTapEnabled(true);

        //hole
        pieChart.setHoleColor(Color.parseColor("#E8E8E8"));
        pieChart.setHoleRadius(65);
        pieChart.setTransparentCircleRadius(70);

        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawCenterText(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawMarkers(false);
        pieChart.getDescription().setEnabled(false);



    }


    private void showPieChart(){

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> typeAmountMap = new HashMap<>();
        colors = new ArrayList<>();
        labels = new ArrayList<>();
        receiptSums = new ArrayList<>();


        String label = "";

        //initializing data
        DataBaseHandler DB = new DataBaseHandler(getContext(), null, null, 1);
        List<Seller> sellers = DB.readSellersData();

        for(int i=0; i<sellers.size(); i++ ){
            Float sellerReceiptsSum = sellers.get(i).getReceiptsSum();
            String receiptSellerName = normalizeString(sellers.get(i).getName());

            sum += sellerReceiptsSum;
            //set label
            typeAmountMap.put(receiptSellerName.split(" ")[0], sellerReceiptsSum  );
            labels.add(receiptSellerName.split(" ")[0]);
            receiptSums.add(df.format(sellers.get(i).getReceiptsSum()));

            //set color
            int senderNameInt = 0;
            for (int j=0; j<receiptSellerName.length()-2; j++) senderNameInt += receiptSellerName.charAt(j);
            Random randomColor = new Random(senderNameInt);
            int color = Color.argb(255, randomColor.nextInt(245),randomColor.nextInt(245),randomColor.nextInt(245));
            colors.add(color);

        }

        textViewPieChartCenter.setText(df.format(sum) + " €");


        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type), type));
        }

        //collecting the entries with label nameu
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        //setting text size of the value
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false);

        pieChart.setData(pieData);
        pieChart.invalidate();

        SellersViewAdapter adapter = new SellersViewAdapter(getContext(),colors, labels, receiptSums );
        recyclerViewSellers.setAdapter(adapter);
    }


}


