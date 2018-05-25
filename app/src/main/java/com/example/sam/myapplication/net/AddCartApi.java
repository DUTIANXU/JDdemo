package com.example.sam.myapplication.net;

import com.example.sam.myapplication.bean.BaseBean;

import io.reactivex.Observable;

/**
 * Created by sam on 2018/5/23.
 */

public class AddCartApi {
    private static AddCartApi addCartApi;
    private AddCartApiService addCartApiService;

    private AddCartApi(AddCartApiService addCartApiService) {
        this.addCartApiService = addCartApiService;
    }

    public static AddCartApi getAddCartApi(AddCartApiService addCartApiService) {
        if (addCartApi == null) {
            addCartApi = new AddCartApi(addCartApiService);
        }
        return addCartApi;
    }

    public Observable<BaseBean> getCatagory(String uid, String pid, String token) {
        return addCartApiService.addCart(uid, pid, token);
    }
}
