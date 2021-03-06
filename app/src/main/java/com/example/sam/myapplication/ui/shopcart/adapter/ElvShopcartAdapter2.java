package com.example.sam.myapplication.ui.shopcart.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sam.myapplication.R;
import com.example.sam.myapplication.bean.GetCartsBean;
import com.example.sam.myapplication.bean.SellerBean;
import com.example.sam.myapplication.ui.shopcart.presenter.ShopcartPresenter;
import com.example.sam.myapplication.ui.widget.AddSubView;
import com.example.sam.myapplication.utils.SharedPreferencesUtils;

import java.util.List;

public class ElvShopcartAdapter2 extends BaseExpandableListAdapter {
    private Context context;
    private List<SellerBean> groupList;
    private List<List<GetCartsBean.DataBean.ListBean>> childList;
    private LayoutInflater inflater;
    private ProgressDialog progressDialog;
    private ShopcartPresenter shopcartPresenter;
    private final String uid;
    private final String token;
    private static final int CLICK_PRODUCT = 0;
    private static final int CLICK_SELLER = 1;
    private static final int CLICK_ALL = 2;
    private static int state = 0;
    private int cIndex = 0;//商家下商品的下标
    private int gIndex = 0;//点击的商家下标
    private int selected;//点击商家的时候，该商家的checkbox选中状态

    public ElvShopcartAdapter2(Context context, List<SellerBean> groupList, List<List<GetCartsBean.DataBean
            .ListBean>> childList, ProgressDialog progressDialog, ShopcartPresenter shopcartPresenter) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
        this.progressDialog = progressDialog;
        this.shopcartPresenter = shopcartPresenter;
        inflater = LayoutInflater.from(context);
        uid = (String) SharedPreferencesUtils.getParam(context, "uid", "");
        token = (String) SharedPreferencesUtils.getParam(context, "token", "");
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.shopcart_seller_item, null);
            groupViewHolder.cbSeller = convertView.findViewById(R.id.cbSeller);
            groupViewHolder.tvSeller = convertView.findViewById(R.id.tvSeller);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final SellerBean sellerBean = groupList.get(groupPosition);
        groupViewHolder.cbSeller.setChecked(sellerBean.isSelected());
        groupViewHolder.tvSeller.setText(sellerBean.getSellerName());

        //给商家的checkbox设置点击事件
        groupViewHolder.cbSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = CLICK_SELLER;
                gIndex = groupPosition;
                //显示进度条
                progressDialog.show();
                GetCartsBean.DataBean.ListBean listBean = childList.get(groupPosition).get(cIndex);
                int pid = listBean.getPid();
                int num = listBean.getNum();
                selected = groupViewHolder.cbSeller.isChecked() ? 1 : 0;
                shopcartPresenter.updateCarts(uid, sellerBean.getSellerid() + "", pid + "", num + "", selected + "",
                        token);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup
                                     parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.shopcart_seller_product_item, null);
            childViewHolder.cbProduct = convertView.findViewById(R.id.cbProduct);
            childViewHolder.iv = convertView.findViewById(R.id.iv);
            childViewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            childViewHolder.tvPrice = convertView.findViewById(R.id.tvPrice);
            childViewHolder.tvDel = convertView.findViewById(R.id.tvDel);
            childViewHolder.addSubView = convertView.findViewById(R.id.addSubCard);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final GetCartsBean.DataBean.ListBean listBean = childList.get(groupPosition).get(childPosition);
        childViewHolder.cbProduct.setChecked(listBean.getSelected() == 1 ? true : false);
        childViewHolder.tvTitle.setText(listBean.getTitle());
        childViewHolder.tvPrice.setText(listBean.getPrice() + "");
        Glide.with(context).load(listBean.getImages().split("\\|")[0]).into(childViewHolder.iv);
        childViewHolder.addSubView.setNum(listBean.getNum() + "");


        //给二级列表的checkbox设置点击事件
        childViewHolder.cbProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = CLICK_PRODUCT;
                //更新购物车
                SellerBean sellerBean = groupList.get(groupPosition);
                String sellerid = sellerBean.getSellerid();
                int pid = listBean.getPid();
                int num = listBean.getNum();
                int selected = childViewHolder.cbProduct.isChecked() ? 1 : 0;
                progressDialog.show();
                shopcartPresenter.updateCarts(uid, sellerid, pid + "", num + "", selected + "", token);
            }
        });

        //给减号设置点击事件
        childViewHolder.addSubView.setSubOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = CLICK_PRODUCT;
                //先获取原先的数量
                int num = listBean.getNum();
                if (num == 1) {
                    Toast.makeText(context, "数量不能小于1", Toast.LENGTH_SHORT).show();
                    return;
                }
                num--;
                //去更新购物车
                SellerBean sellerBean = groupList.get(groupPosition);
                String sellerid = sellerBean.getSellerid();
                int pid = listBean.getPid();
                int selected = childViewHolder.cbProduct.isChecked() ? 1 : 0;
                progressDialog.show();
                shopcartPresenter.updateCarts(uid, sellerid, pid + "", num + "", selected + "", token);
            }
        });

        //给加号设置点击事件
        childViewHolder.addSubView.setAddOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = CLICK_PRODUCT;
                //先获取原先的数量
                int num = listBean.getNum();
                num++;
                //去更新购物车
                SellerBean sellerBean = groupList.get(groupPosition);
                String sellerid = sellerBean.getSellerid();
                int pid = listBean.getPid();
                int selected = childViewHolder.cbProduct.isChecked() ? 1 : 0;
                progressDialog.show();
                shopcartPresenter.updateCarts(uid, sellerid, pid + "", num + "", selected + "", token);
            }
        });
        //给删除设置点击事件
        childViewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = CLICK_PRODUCT;
                //去删除购物车
                int pid = listBean.getPid();
                shopcartPresenter.deleteCart(uid, pid + "", token);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        CheckBox cbSeller;
        TextView tvSeller;
    }

    class ChildViewHolder {
        CheckBox cbProduct;
        ImageView iv;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvDel;
        AddSubView addSubView;
    }


    /**
     * 更新购物车成功后走的方法
     */
    public void updateSuceess() {
        progressDialog.dismiss();
        if (state == CLICK_PRODUCT) {
            shopcartPresenter.getCarts(uid, token);
        } else if (state == CLICK_SELLER) {
            //判断该商家下是否还有别的商品，如果有则继续更新
            cIndex++;
            //获取该商家下的商品列表个数
            int pCount = childList.get(gIndex).size();
            if (cIndex >= pCount) {
                //该商家下的商品已经遍历完成
                cIndex = 0;
                shopcartPresenter.getCarts(uid, token);
            } else {
                //接着更新该商家下的其它商品
                SellerBean sellerBean = groupList.get(gIndex);
                String sellerid = sellerBean.getSellerid();
                GetCartsBean.DataBean.ListBean listBean = childList.get(gIndex).get(cIndex);
                int pid = listBean.getPid();
                int num = listBean.getNum();
                shopcartPresenter.updateCarts(uid, sellerid + "", pid + "", num + "", selected + "", token);
            }
        } else if (state == CLICK_ALL) {
            //先更新该商家下的商品
            cIndex++;
            if (cIndex >= childList.get(gIndex).size()) {
                //该商家下的商品已经更新完成
                cIndex = 0;
                gIndex++;
                if (gIndex >= groupList.size()) {
                    //所有商家都已更新完成
                    //重新获取购物车列表
                    shopcartPresenter.getCarts(uid, token);
                    return;
                }
                //接着更新该商家下的其它商品
                SellerBean sellerBean = groupList.get(gIndex);
                String sellerid = sellerBean.getSellerid();
                GetCartsBean.DataBean.ListBean listBean = childList.get(gIndex).get(cIndex);
                int pid = listBean.getPid();
                int num = listBean.getNum();
                shopcartPresenter.updateCarts(uid, sellerid + "", pid + "", num + "", selected + "", token);
            } else {
                //接着更新该商家下的其它商品
                SellerBean sellerBean = groupList.get(gIndex);
                String sellerid = sellerBean.getSellerid();
                GetCartsBean.DataBean.ListBean listBean = childList.get(gIndex).get(cIndex);
                int pid = listBean.getPid();
                int num = listBean.getNum();
                shopcartPresenter.updateCarts(uid, sellerid + "", pid + "", num + "", selected + "", token);
            }
        }
    }

    /**
     * 遍历所有商家，判断是否都选中
     *
     * @return
     */
    public boolean isAllSelected() {
        for (int i = 0; i < groupList.size(); i++) {
            if (!groupList.get(i).isSelected()) {
                return false;
            }
        }
        return true;
    }

    public void changAllState(boolean bool) {
        progressDialog.show();
        state = CLICK_ALL;
        gIndex = 0;
        cIndex = 0;
        selected = bool ? 1 : 0;
        SellerBean sellerBean = groupList.get(gIndex);
        String sellerid = sellerBean.getSellerid();
        GetCartsBean.DataBean.ListBean listBean = childList.get(gIndex).get(cIndex);
        int pid = listBean.getPid();
        int num = listBean.getNum();
        shopcartPresenter.updateCarts(uid, sellerid, pid + "", num + "", selected + "", token);
    }

    public String[] getMoneyAndCount() {
        double money = 0;
        int count = 0;
        for (int i = 0; i < groupList.size(); i++) {
            for (int j = 0; j < childList.get(i).size(); j++) {
                GetCartsBean.DataBean.ListBean listBean = childList.get(i).get(j);
                if (listBean.getSelected() == 1) {
                    count += listBean.getNum();
                    money += listBean.getPrice() * listBean.getNum();
                }
            }
        }
        return new String[]{money + "", count + ""};
    }

}
