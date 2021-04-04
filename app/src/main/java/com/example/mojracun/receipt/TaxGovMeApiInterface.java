package com.example.mojracun.receipt;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TaxGovMeApiInterface {
    @FormUrlEncoded
    @POST("ic/api/verifyInvoice")
        //@POST("posts")
    Call<Receipt> createPost(@FieldMap Map<String, String> fields);
}
