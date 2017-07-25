package com.quanmai.yiqu.api.vo;

import com.quanmai.yiqu.R;

import org.json.JSONException;
import org.json.JSONObject;

public class VipInfo {
    public int level;
    public String level_name;
    public int level_img_id;
    public int level_big_img_id;

    public VipInfo() {
        level = 0;
        level_name = "LV1";
        level_img_id = R.drawable.level_one;
        level_big_img_id = R.drawable.level_b_0;
    }

    public VipInfo(JSONObject jsonObject, String key) throws JSONException {
        level = jsonObject.getInt(key);
        switch (level) {
            case 2:
                level_name = "LV2";
                level_img_id = R.drawable.level_two;
                level_big_img_id = R.drawable.level_b_1;
                break;
            case 3:
                level_name = "LV3";
                level_img_id = R.drawable.level_three;
                level_big_img_id = R.drawable.level_b_2;
                break;
            case 4:
                level_name = "LV4";
                level_img_id = R.drawable.level_four;
                level_big_img_id = R.drawable.level_b_3;
                break;
            case 5:
                level_name = "LV5";
                level_img_id = R.drawable.level_five;
                level_big_img_id = R.drawable.level_b_0;
                break;
            case 6:
                level_name = "LV6";
                level_img_id = R.drawable.level_six;
                level_big_img_id = R.drawable.level_b_0;
                break;
            default:
                level_name = "LV1";
                level_img_id = R.drawable.level_one;
                level_big_img_id = R.drawable.level_b_0;
                break;
        }
    }

    public void setLevel(int level) {
        this.level = level;
        switch (level) {
            case 2:
                level_name = "LV2";
                level_img_id = R.drawable.level_two;
                level_big_img_id = R.drawable.level_b_1;
                break;
            case 3:
                level_name = "LV3";
                level_img_id = R.drawable.level_three;
                level_big_img_id = R.drawable.level_b_2;
                break;
            case 4:
                level_name = "LV4";
                level_img_id = R.drawable.level_four;
                level_big_img_id = R.drawable.level_b_3;
                break;
            case 5:
                level_name = "LV5";
                level_img_id = R.drawable.level_five;
                level_big_img_id = R.drawable.level_b_0;
                break;
            case 6:
                level_name = "LV6";
                level_img_id = R.drawable.level_six;
                level_big_img_id = R.drawable.level_b_0;
                break;
            default:
                level_name = "LV1";
                level_img_id = R.drawable.level_one;
                level_big_img_id = R.drawable.level_b_0;
                break;
        }
    }
}
