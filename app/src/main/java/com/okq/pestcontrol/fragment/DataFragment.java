package com.okq.pestcontrol.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.okq.pestcontrol.adapter.DataAdapter;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.PestKind;
import com.okq.pestcontrol.widget.ScreeningPopupWindow;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/3.
 */
@ContentView(value = R.layout.fragment_data)
public class DataFragment extends BaseFragment {

    @ViewInject(value = R.id.data_fra_menu_flag)
    private View menuPopupLocFlag;
    @ViewInject(value = R.id.data_fra_data_recy)
    private RecyclerView dataRecy;
    private RecyclerView.LayoutManager mManager;

    private ArrayList<PestInformation> pests;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        dataRecy.setLayoutManager(mManager);
        RecyclerView.Adapter adapter = new DataAdapter(getContext(),pests);
        dataRecy.setAdapter(adapter);
        dataRecy.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 加载数据
     */
    private void loadData() {
        pests = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            PestInformation pi = new PestInformation();
            pi.setArea("区域" + (i + 1));
            PestKind pk = new PestKind();
            pk.setKindName("种类" + (i + 1));
            pk.setKindFlag(i + 1);
            pi.setPestKind(pk);
            pi.setStartTime(System.currentTimeMillis() - 2 * 1000 * 60 * 60);
            pi.setEndTime(System.currentTimeMillis());
            pi.setSendTime(System.currentTimeMillis() - 1000 * 60 * 60);
            pi.setDeviceNum(i + "");
            pi.setTemperature(30 + i % 4);
            pi.setHumidity(80 + 2 * i % 4);
            pests.add(pi);
        }

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
