package com.example.sam.myapplication.net;

import com.example.sam.myapplication.bean.GetCartsBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetCartApiService {
    @FormUrlEncoded
    @POST("product/getCarts")
    Observable<GetCartsBean> getCart(@Field("Uid") String uid,
                                     @Field("Token") String token);

}
