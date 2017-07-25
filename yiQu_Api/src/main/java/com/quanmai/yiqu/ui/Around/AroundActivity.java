package com.quanmai.yiqu.ui.Around;

import android.os.Bundle;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.ui.fragment.AroundFragment;

public class AroundActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new AroundFragment()).commit();
    }
}
