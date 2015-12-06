package com.okq.pestcontrol.fragment;

import android.os.Bundle;
import android.view.View;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Bean;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 */
@ContentView(value = R.layout.fragment_data)
public class DataFragment extends BaseFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DbManager db = x.getDb(App.getDaoConfig());
        for (int i = 0; i < 10; i++) {
            Bean test = new Bean();
            test.setI1(i * 10 + 1);
            test.setI2(i * 10 + 2);
            test.setI3(i * 10 + 3);
            test.setI4(i * 10 + 4);
            test.setI5(i * 10 + 5);
            test.setS1("s1--" + i);
            test.setS2("s2--" + i);
            test.setS3("s3--" + i);
            test.setS4("s4--" + i);
            test.setS5("s5--" + i);
        }

        try {
            List<Bean> beans = db.selector(Bean.class).where("i1", "in", new int[]{1, 3, 4}).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
