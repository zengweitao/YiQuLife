package com.quanmai.yiqu.common.ShoppingCar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.common.ShoppingCar.imp.ShopCartImp;
import com.quanmai.yiqu.common.ShoppingCar.model.Dish;
import com.quanmai.yiqu.common.ShoppingCar.model.ShopCart;

import java.util.ArrayList;

/**
 * Created by cheng on 16-12-23.
 */
public class PopupDishAdapter extends RecyclerView.Adapter{

    private static String TAG = "PopupDishAdapter";
    private ShopCart shopCart;
    private Context context;
    private int itemCount;
    private ArrayList<RecycleGarbagesInfo> dishList;
    private ShopCartImp shopCartImp;

    public PopupDishAdapter(Context context, ShopCart shopCart){
        this.shopCart = shopCart;
        this.context = context;
        this.itemCount = shopCart.getDishAccount();
        this.dishList = new ArrayList<>();
        dishList.addAll(shopCart.getShoppingSingleMap().keySet());
        Log.e(TAG, "PopupDishAdapter: "+this.itemCount );
    }

    public void clear(){
        dishList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_shopping_car, parent, false);
        DishViewHolder viewHolder = new DishViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DishViewHolder dishholder = (DishViewHolder)holder;
        if (dishList.size()>0){
            final RecycleGarbagesInfo dish = getDishByPosition(position);
            if(dish!=null&&shopCart.getShoppingSingleMap().size()>0) {
                dishholder.textview_name.setText(dish.getGarbage());
                dishholder.textview_price.setText(dish.getPoint() + "益币");
                double num = shopCart.getShoppingSingleMap().get(dish);
                if (dish.getUnit().contains("斤")||dish.getUnit().contains("克")){
                    if (String.valueOf(num).endsWith(".0")){
                        dishholder.textview_number.setText(Math.round(num)+"");
                    }else {dishholder.textview_number.setText(num+"");}
                }else {
                    dishholder.textview_number.setText(Math.round(num)+"");
                }

        }

            /*dishholder.right_dish_add_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(shopCart.addShoppingSingle(dish)) {
                        notifyItemChanged(position);
                        if(shopCartImp!=null)
                            shopCartImp.add(view,position);
                    }
                }
            });*/

            /*dishholder.right_dish_remove_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(shopCart.subShoppingSingle(dish)){
                        dishList.clear();
                        dishList.addAll(shopCart.getShoppingSingleMap().keySet());
                        itemCount = shopCart.getDishAccount();;
                        notifyDataSetChanged();
                        if(shopCartImp!=null)
                            shopCartImp.remove(view,position);
                    }
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return this.itemCount;
    }

    public RecycleGarbagesInfo getDishByPosition(int position){
       return dishList.get(position);
    }

    public ShopCartImp getShopCartImp() {
        return shopCartImp;
    }

    public void setShopCartImp(ShopCartImp shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    private class DishViewHolder extends RecyclerView.ViewHolder{
        private TextView textview_name;
        private TextView textview_price;
        private TextView textview_number;

        public DishViewHolder(View itemView) {
            super(itemView);
            textview_name = (TextView)itemView.findViewById(R.id.textview_name);
            textview_price = (TextView)itemView.findViewById(R.id.textview_price);
            textview_number = (TextView) itemView.findViewById(R.id.textview_number);
        }

    }
}
