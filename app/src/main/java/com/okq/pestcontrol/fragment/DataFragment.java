package com.okq.pestcontrol.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Bean;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;
import com.okq.pestcontrol.widget.ScreeningPopupWindow;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 */
@ContentView(value = R.layout.fragment_data)
public class DataFragment extends BaseFragment {

    @ViewInject(value = R.id.data_fra_menu_flag)
    private View menuPopupLocFlag;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!menu.hasVisibleItems())
            inflater.inflate(R.menu.data_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.data_menu_sort://排序

                break;
            case R.id.data_menu_screening://筛选
                final ScreeningPopupWindow screen = new ScreeningPopupWindow(getContext(), getFragmentManager());
                screen.setOnScreeningFinishListener(new ScreeningPopupWindow.OnScreeningFinishListener() {
                    @Override
                    public void onFinished(Bundle data) {
                        PestInformation pi = (PestInformation) data.getSerializable("data");
                        Toast.makeText(getContext(), pi.getArea(), Toast.LENGTH_LONG).show();
                        screen.dismiss();
//                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                        lp.alpha = 1.0f;
//                        getActivity().getWindow().setAttributes(lp);
                    }

                    @Override
                    public Bundle onOpen() {
                        Bundle bundle = new Bundle();

                        PestInformation pi = new PestInformation();
                        PestKind pk = new PestKind();
                        pk.setKindFlag(1);
                        pk.setKindName("种类1");
                        pi.setArea("area");
                        pi.setPestKind(pk);
                        pi.setStartTime(System.currentTimeMillis());
                        pi.setEndTime(System.currentTimeMillis());
                        bundle.putSerializable("data", pi);
                        return bundle;
                    }
                });
                screen.showAsDropDown(menuPopupLocFlag);
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.alpha = 0.7f;
//                getActivity().getWindow().setAttributes(lp);
                break;
        }
        return true;
    }
}
