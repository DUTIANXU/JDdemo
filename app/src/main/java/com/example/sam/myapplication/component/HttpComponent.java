package com.example.sam.myapplication.component;



import com.example.sam.myapplication.ui.classify.ClassifyFragment;
import com.example.sam.myapplication.ui.classify.ListActivity;
import com.example.sam.myapplication.ui.classify.ListDetailsActivity;
import com.example.sam.myapplication.ui.homepage.HomePageFragment;
import com.example.sam.myapplication.ui.login.LoginActivity;
import com.example.sam.myapplication.ui.mine.MakeSureOrderActivity;
import com.example.sam.myapplication.ui.shopcart.ShopCartActivity;
import com.example.sam.myapplication.ui.mine.UserInfoActivity;
import com.example.sam.myapplication.model.HttpModule;
import com.example.sam.myapplication.ui.shopcart.ShopCartActivity2;

import dagger.Component;

/**
 * Created by sam on 2018/5/23.
 */
@Component(modules = HttpModule.class)
public interface HttpComponent {
    void inject(LoginActivity loginActivity);

    void inject(HomePageFragment homePageFragment);

    void inject(ClassifyFragment classifyFragment);

    void inject(ListActivity listActivity);

    void inject(ListDetailsActivity listDetailsActivity);

    void inject(ShopCartActivity shopCartActivity);

    void inject(ShopCartActivity2 shopCartActivity2);

    void inject(MakeSureOrderActivity makeSureOrderActivity);

    void inject(UserInfoActivity userInfoActivity);
}
