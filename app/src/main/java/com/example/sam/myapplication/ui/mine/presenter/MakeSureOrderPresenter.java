package com.example.sam.myapplication.ui.mine.presenter;

import com.example.sam.myapplication.bean.AddrsBean;
import com.example.sam.myapplication.bean.BaseBean;
import com.example.sam.myapplication.net.AddrsApi;
import com.example.sam.myapplication.net.CreateOrderApi;
import com.example.sam.myapplication.net.DeleteCartApi;
import com.example.sam.myapplication.net.UpdateCartApi;
import com.example.sam.myapplication.ui.base.BasePresenter;
import com.example.sam.myapplication.ui.mine.contract.MakeSureOrderContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MakeSureOrderPresenter extends BasePresenter<MakeSureOrderContract.View> implements
        MakeSureOrderContract.Presenter {
    private AddrsApi addrsApi;
    private CreateOrderApi createOrderApi;
    private UpdateCartApi updateCartApi;
    private DeleteCartApi deleteCartApi;


    @Inject
    public MakeSureOrderPresenter(AddrsApi addrsApi, CreateOrderApi createOrderApi, UpdateCartApi updateCartApi,
                                  DeleteCartApi deleteCartApi) {
        this.addrsApi = addrsApi;
        this.createOrderApi = createOrderApi;
        this.updateCartApi = updateCartApi;
        this.deleteCartApi = deleteCartApi;
    }

    @Override
    public void getAddrs(String uid, String token) {
        addrsApi.getAddrs(uid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<AddrsBean, List<AddrsBean.DataBean>>() {
                    @Override
                    public List<AddrsBean.DataBean> apply(AddrsBean addrsBean) throws Exception {
                        return addrsBean.getData();
                    }
                }).subscribe(new Consumer<List<AddrsBean.DataBean>>() {
            @Override
            public void accept(List<AddrsBean.DataBean> dataBeans) throws Exception {
                if (mView != null) {
                    mView.addrsSuccess(dataBeans);
                }
            }
        });

    }

    @Override
    public void createOrder(String uid, String price, String token) {
        createOrderApi.getCatagory(uid, price, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseBean, String>() {

                    @Override
                    public String apply(BaseBean baseBean) throws Exception {
                        return baseBean.getMsg();
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (mView != null) {
                    mView.createOrderSuccess(s);
                }
            }
        });

    }

    @Override
    public void updateCarts(String uid, String sellerid, String pid, String num, String selected, String token) {
        updateCartApi.updateCarts(uid, sellerid, pid, num, selected, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseBean, String>() {
                    @Override
                    public String apply(BaseBean baseBean) throws Exception {
                        return baseBean.getMsg();
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (mView != null) {
                    mView.updateCartsSuccess(s);
                }
            }
        });
    }

    @Override
    public void deleteCart(String uid, String pid, String token) {
        deleteCartApi.deleteCart(uid, pid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseBean, String>() {
                    @Override
                    public String apply(BaseBean baseBean) throws Exception {
                        return baseBean.getMsg();
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (mView != null) {
                    mView.updateCartsSuccess(s);
                }
            }
        });
    }


}
