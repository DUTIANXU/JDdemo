package com.example.sam.myapplication.ui.homepage.contract;

import com.example.sam.myapplication.bean.AdBean;
import com.example.sam.myapplication.bean.CatagoryBean;
import com.example.sam.myapplication.ui.base.BaseContract;

public interface HomPageContract {
    interface View extends BaseContract.BaseView {
        void getAdSuccess(AdBean adBean);

        void getCatagorySuccess(CatagoryBean catagoryBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getAd();

        void getCatagory();
    }

}
