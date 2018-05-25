package com.example.sam.myapplication.ui.login.contract;

import com.example.sam.myapplication.bean.UserBean;
import com.example.sam.myapplication.ui.base.BaseContract;

public interface LoginContract {
    interface View extends BaseContract.BaseView {
        void loginSuccess(UserBean userBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void login(String mobile, String password);
    }
}
