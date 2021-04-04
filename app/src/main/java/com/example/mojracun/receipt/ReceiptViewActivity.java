package com.example.mojracun.receipt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mojracun.R;
import com.example.mojracun.database.DataBaseHandler;

public class ReceiptViewActivity extends AppCompatActivity {

    public static final String TABLE1_NAME = "RECEIPTS";

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_view);
        String iic = getIntent().getStringExtra("iic");

        DataBaseHandler DB = new DataBaseHandler(this, null, null, 1);
        Receipt receipt = DB.readReceipt(iic);

        showReceipt(receipt);
    }

    public void showReceipt(Receipt receipt){

        setContentView(R.layout.activity_receipt_view);

        textViewResult = findViewById(R.id.text_view_result);

        String content = "";
        Seller seller = receipt.getSeller();
        Item[] items = receipt.getItems();

        //content += "ID: " + receipt.getId() + "\n\n";

        content += "Ukupna cijena: " + receipt.getTotalPrice() + " € \n\n";

        content += "Datum: " + receipt.getDateTimeCreated().split("T")[0] + "\n";
        content += "Vrijeme: " + receipt.getDateTimeCreated().split("T")[1].split(".0")[0] + "\n\n";
        //content += "Datum: " + postResponse.getReceiptDate().split("T")[0] + "\n";
        //content += "Vrijeme: " + postResponse.getReceiptTime().split("T")[1].split(".0")[0] + "\n\n";

        content += "Mjesto: " + seller.getName() + "\n" ;
        content += "Adresa: " + seller.getAddress() + "\n";
        content += "Grad: " + seller.getTown() + "\n\n\n";

        content += "Artikli:\n\n";

        for (int i=0; i<items.length; i++)
        {
            content += "Br. " + (i+1) + ". \n"
                    + "  Ime: " + items[i].getName() + "\n"
                    + "  Količina: " + items[i].getQuantity() + "\n"
                    + "  Cijena: " + items[i].getPriceAfterVat() + " € \n";
        }

        textViewResult.setText(content);
    }
}