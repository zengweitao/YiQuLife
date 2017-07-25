package com.quanmai.yiqu.api.vo;

/**
 * Created by admin on 2017/6/30.
 */

public class RecycleGarbagesMapInfo {
    public String name; //物品名
    public int count; //单价
    public int numbers; //数量

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }
}
