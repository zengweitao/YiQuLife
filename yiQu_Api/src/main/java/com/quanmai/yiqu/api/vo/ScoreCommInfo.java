package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 巡检--选择小区实体
 * Created by 95138 on 2016/11/14.
 */

public class ScoreCommInfo implements Serializable {
    private String letter;
    private List<ScoreCommDetailInfo> list;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<ScoreCommDetailInfo> getList() {
        return list;
    }

    public void setList(List<ScoreCommDetailInfo> list) {
        this.list = list;
    }
}
