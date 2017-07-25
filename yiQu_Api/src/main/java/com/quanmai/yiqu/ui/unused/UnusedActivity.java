package com.quanmai.yiqu.ui.unused;

import android.os.Bundle;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.ui.fragment.UnusedFragment;

public class UnusedActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout,new UnusedFragment()).commit();
    }
}
