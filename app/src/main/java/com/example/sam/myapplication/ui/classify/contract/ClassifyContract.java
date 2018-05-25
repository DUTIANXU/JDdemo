package com.example.sam.myapplication.ui.classify.contract;

import com.example.sam.myapplication.bean.CatagoryBean;
import com.example.sam.myapplication.bean.ProductCatagoryBean;
import com.example.sam.myapplication.ui.base.BaseContract;

public interface ClassifyContract {
    interface View extends BaseContract.BaseView {
        void getProductCatagorySuccess(ProductCatagoryBean productCatagoryBean);

        void getCatagorySuccess(CatagoryBean catagoryBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getProductCatagory(String cid);

        void getCatagory();
    }
}
