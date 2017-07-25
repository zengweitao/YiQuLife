package com.quanmai.yiqu.common.ShoppingCar.model;

import android.support.annotation.MainThread;
import android.util.Log;

import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cheng on 16-11-12.
 */
public class ShopCart {
    private int shoppingAccount;//商品总数
    private int shoppingTotalPrice;//商品总价钱
    private Map<RecycleGarbagesInfo, Double> shoppingSingle;//单个物品的总价价钱
    List<RecycleGarbagesInfo> garbageList;

    public ShopCart() {
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle = new HashMap<>();
        this.garbageList = new ArrayList<>();
    }

    public int getShoppingAccount() {
        shoppingAccount=shoppingSingle.size();
        return shoppingAccount;
    }

    public int getShoppingTotalPrice() {
        shoppingTotalPrice=0;
        if (getDishAccount() <= 0) {
            return 0;
        }
        DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数

        for (int i = 0; i < garbageList.size(); i++) {
            Log.e("--分别输出", "== " +garbageList.size()+"  "+ shoppingSingle.get(garbageList.get(i))+"  "+Integer.parseInt(garbageList.get(i).getPoint()));
            shoppingTotalPrice = (int) (shoppingTotalPrice + Math.round((shoppingSingle.get(garbageList.get(i)) * Integer.parseInt(garbageList.get(i).getPoint()))));
        }
        Log.e("--计算总和", "== " + shoppingTotalPrice);

        if (shoppingTotalPrice > 1000) {
            return shoppingTotalPrice=1000;
        } else if (shoppingTotalPrice <= 0) {
            return shoppingTotalPrice=0;
        } else {
            return shoppingTotalPrice;
        }
    }

    public Map<RecycleGarbagesInfo, Double> getShoppingSingleMap() {
        return shoppingSingle;
    }

    public boolean setMap(Map<RecycleGarbagesInfo, Double> maps, List<RecycleGarbagesInfo> garbageList) {
        if (garbageList.size() > 0) {
            this.garbageList.clear();
            this.shoppingSingle.clear();
            this.garbageList.addAll(garbageList);
            this.shoppingSingle.putAll(maps);
            shoppingTotalPrice=getShoppingTotalPrice();
            return true;
        }
        clear();
        return false;
    }

    public int getDishAccount() {
        return shoppingSingle.size();
    }

    public void clear() {
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle.clear();
        this.garbageList.clear();
    }
}
