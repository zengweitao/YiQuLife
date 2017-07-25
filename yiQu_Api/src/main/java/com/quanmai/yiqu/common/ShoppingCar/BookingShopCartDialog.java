package com.quanmai.yiqu.common.ShoppingCar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.ShoppingCar.adapter.PopupDishAdapter;
import com.quanmai.yiqu.common.ShoppingCar.imp.ShopCartImp;
import com.quanmai.yiqu.common.ShoppingCar.model.ShopCart;

/**
 * Created by cheng on 16-12-22.
 */
public class BookingShopCartDialog extends Dialog implements View.OnClickListener, ShopCartImp {

    private LinearLayout linearLayout, clearLayout;
    private RelativeLayout shopping_cart_bottom, linear_bottm;
    private ShopCart shopCart;
    private TextView textViewPredictIntegral;
    private TextView textViewConfirmBooking;
    private RecyclerView recycleview_shoppingcar;
    private PopupDishAdapter dishAdapter;
    private ShopCartDialogImp shopCartDialogImp;
    private ShopCartDialogOnClick shopCartDialogOnClick;
    private TextView textview_dor_shopingcar;

    public BookingShopCartDialog(Context context, ShopCart shopCart, int themeResId) {
        super(context, themeResId);
        this.shopCart = shopCart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shopping_car);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        clearLayout = (LinearLayout) findViewById(R.id.clear_layout);
        shopping_cart_bottom = (RelativeLayout) findViewById(R.id.shopping_cart_bottom);
        linear_bottm = (RelativeLayout) findViewById(R.id.linear_bottm);
        textViewPredictIntegral = (TextView) findViewById(R.id.textViewPredictIntegral);
        textViewConfirmBooking = (TextView) findViewById(R.id.textViewConfirmBooking);
        textview_dor_shopingcar = (TextView) findViewById(R.id.textview_dor_shopingcar);
        recycleview_shoppingcar = (RecyclerView) findViewById(R.id.recycleview_shoppingcar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//设置布局管理器
        recycleview_shoppingcar.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        shopping_cart_bottom.setOnClickListener(this);
        linear_bottm.setOnClickListener(this);
        textViewConfirmBooking.setOnClickListener(this);
        clearLayout.setOnClickListener(this);
        dishAdapter = new PopupDishAdapter(getContext(), shopCart);
        recycleview_shoppingcar.setAdapter(dishAdapter);
        dishAdapter.setShopCartImp(this);
        showTotalPrice();
    }

    @Override
    public void show() {
        super.show();
        animationShow(200);
    }

    @Override
    public void dismiss() {
        animationHide(200);
    }

    private void showTotalPrice() {
        if (shopCart != null && shopCart.getShoppingTotalPrice() > 0) {
            textViewPredictIntegral.setText(shopCart.getShoppingTotalPrice() + "益币");
            textview_dor_shopingcar.setVisibility(View.VISIBLE);
            //textview_dor_shopingcar.setText(""+shopCart.getShoppingAccount());

        } else {
            textview_dor_shopingcar.setVisibility(View.GONE);
        }
    }

    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY", 1000, 0).setDuration(mDuration)
        );
        animatorSet.start();
    }

    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY", 0, 1000).setDuration(mDuration)
        );
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                BookingShopCartDialog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //case R.id.shopping_cart_bottom:
            case R.id.shopping_cart_bottom:
                this.dismiss();
                break;
            case R.id.clear_layout:
                clear();
                //dishAdapter.clear();
                if (shopCartDialogImp != null) {
                    shopCartDialogImp.dialogDismiss();
                }
                break;
            case R.id.textViewConfirmBooking:
                if (shopCartDialogOnClick != null) {
                    shopCartDialogOnClick.dialogOnClick();
                }
                break;
        }
    }

    @Override
    public void add(View view, int postion) {
        showTotalPrice();
    }

    @Override
    public void remove(View view, int postion) {
        showTotalPrice();
        if (shopCart.getShoppingAccount() == 0) {
            this.dismiss();
        }
    }

    public ShopCartDialogImp getShopCartDialogImp() {
        return shopCartDialogImp;
    }

    public void setShopCartDialogImp(ShopCartDialogImp shopCartDialogImp) {
        this.shopCartDialogImp = shopCartDialogImp;
    }

    public ShopCartDialogOnClick getShopCartDialogOnClick() {
        return shopCartDialogOnClick;
    }

    public void setShopCartDialogOnClick(ShopCartDialogOnClick shopCartDialogOnClick) {
        this.shopCartDialogOnClick = shopCartDialogOnClick;
    }

    public interface ShopCartDialogImp {
        public void dialogDismiss();
    }

    public interface ShopCartDialogOnClick {
        public void dialogOnClick();
    }

    public void clear() {
        shopCart.clear();
        dishAdapter.clear();
        showTotalPrice();
        if (shopCart.getShoppingAccount() == 0) {
            this.dismiss();
        }
    }
}
