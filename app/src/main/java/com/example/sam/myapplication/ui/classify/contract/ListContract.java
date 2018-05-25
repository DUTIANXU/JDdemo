package com.example.sam.myapplication.ui.classify.contract;

import com.example.sam.myapplication.bean.ProductsBean;
import com.example.sam.myapplication.ui.base.BaseContract;

import java.util.List;

public interface ListContract {
    interface View extends BaseContract.BaseView {
        void onSuccess(List<ProductsBean.DataBean> list);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getProducts(String pscid);
    }
}
