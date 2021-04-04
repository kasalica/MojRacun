package com.example.mojracun.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojracun.R;
import com.example.mojracun.database.DataBaseHandler;

import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceiptLoadActivity extends AppCompatActivity {

    private TaxGovMeApiInterface TaxGovMeApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_load);

        /// Loading started
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mapr.tax.gov.me")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient().build())    //PRIVREMENO RESENJE. Ignoring certificate !!!!
                .build();

        TaxGovMeApiInterface = retrofit.create(TaxGovMeApiInterface.class);
        /// Loaded

        String crtd = getIntent().getStringExtra("keyCrtd").substring(0,19) + " 01:00";

        Map<String, String> fields = new HashMap<>();
        fields.put("iic", getIntent().getStringExtra("keyIic"));
        fields.put("dateTimeCreated", crtd);
        fields.put("tin", getIntent().getStringExtra("keyTin"));

        createPost(fields);
    }


    private void createPost(Map<String, String> fields) {

        Call<Receipt> call = TaxGovMeApiInterface.createPost(fields);

        call.enqueue(new Callback<Receipt>() {
            @Override
            public void onResponse(Call<Receipt> call, Response<Receipt> response) {

                if (!response.isSuccessful()) {

                    Log.e("   CODE:         ", String.valueOf(response.code()));
                    Log.e("   RESPONSE:     "," RESPONSE IS NOT SUCCESFUL!");
                    return;
                }

                Receipt postResponse = response.body();

                //inserting in dataBase
                DataBaseHandler DB = new DataBaseHandler(getBaseContext(), null, null, 1);
                DB.insertReceipt(postResponse);

                //starting ReceiptViewActivity with paramethers
                Intent intent = new Intent(getBaseContext(), ReceiptViewActivity.class);
                intent.putExtra("iic", postResponse.getIic());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<Receipt> call, Throwable t) {
                Log.e(" XXXXXXXXXXXXXXXXXXX", "CONNECTION FAILURE");
            }
        });
    }


    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}