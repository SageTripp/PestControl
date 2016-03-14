package com.okq.pestcontrol.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.activity.PestInfoDetailsActivity;
import com.okq.pestcontrol.adapter.DataAdapter;
import com.okq.pestcontrol.adapter.listener.OnItemClickListener;
import com.okq.pestcontrol.adapter.listener.OnItemLongClickListener;
import com.okq.pestcontrol.application.App;
import com.okq.pestcontrol.bean.Device;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.param.PestScreeningParam;
import com.okq.pestcontrol.dbDao.PestInformationDao;
import com.okq.pestcontrol.task.DataTask;
import com.okq.pestcontrol.task.DeleteHistoryDataTask;
import com.okq.pestcontrol.task.TaskInfo;
import com.okq.pestcontrol.util.SortUtil;
import com.okq.pestcontrol.widget.ScreeningDialog;

import org.joda.time.DateTime;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据展示页面 Created by Administrator on 2015/12/3.
 */
@ContentView(value = R.layout.fragment_data)
public class DataFragment extends BaseFragment {

    private static final String DATE_FORMAT = "YYYY-MM-dd";
    @ViewInject(value = R.id.data_fra_menu_flag)
    private View menuPopupLocFlag;
    @ViewInject(value = R.id.data_fra_data_recy)
    private RecyclerView dataRecy;
    @ViewInject(value = R.id.data_fra_data_fresh)
    private SwipeRefreshLayout dataFreshLayout;
    @ViewInject(value = R.id.data_select_bottom)
    private LinearLayout selectBottom;
    @ViewInject(value = R.id.data_select_button_all)
    private Button selectBtn;
    @ViewInject(value = R.id.data_select_button_delete)
    private Button deleteBtn;
    private RecyclerView.LayoutManager mManager;

    private DataAdapter adapter;
    private ArrayList<PestInformation> informations;
    private PestScreeningParam screeningParam;
    private HashMap<Integer, Boolean> isASC = new HashMap<>();
    private List<String> ids = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置开始就进行加载(需要同时设置偏移和setRefreshing(true))
//        dataFreshLayout.setProgressViewOffset(true, 0, 200);
        loadAll();
        initSort();
        dataFreshLayout.setProgressViewEndTarget(true, 200);
        dataFreshLayout.setSize(SwipeRefreshLayout.LARGE);
        dataFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataFreshLayout.setEnabled(false);
                //加载数据
                dataFreshLayout.setEnabled(true);
            }
        });
        dataFreshLayout.setColorSchemeColors(R.color.BLUE, R.color.GREEN, R.color.RED, R.color.YELLOW);
//        dataFreshLayout.setRefreshing(true);
        dataFreshLayout.setEnabled(false);
        dataRecy.scrollBy(0, 200);
        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dataRecy.setLayoutManager(mManager);
        adapter = new DataAdapter(getContext(), informations);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), PestInfoDetailsActivity.class);
                intent.putExtra("pestInfo", informations.get(position));
                startActivity(intent);
            }
        });
        dataRecy.setAdapter(adapter);
        dataRecy.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onSelect(int position, int id, boolean checked) {
//                deleteBtn.setBackgroundColor(Color.TRANSPARENT);
                int size = adapter.getSelected().size();
                if (size > 0) {
                    deleteBtn.setBackgroundColor(getResources().getColor(R.color.RED));
                    deleteBtn.setText(String.format("删除(%d)", size));
                } else {
                    deleteBtn.setBackgroundColor(Color.TRANSPARENT);
                    deleteBtn.setText("取消");
                }
                if (size == informations.size()) {
                    selectBtn.setText("取消全选");
                } else {
                    selectBtn.setText("全选");
                }

                if (checked) {
                    if (!ids.contains(id + ""))
                        ids.add(id + "");
                } else {
                    if (ids.contains(id + "")) {
                        ids.remove(id + "");
                    }
                }
            }

            @Override
            public void onLongClick(int position, int id, boolean checked) {
                ids = new ArrayList<String>();
                selectBottom.setVisibility(View.VISIBLE);
                deleteBtn.setBackgroundColor(getResources().getColor(R.color.RED));
                deleteBtn.setText(String.format("删除(%d)", adapter.getSelected().size()));
                if (checked) {
                    if (!ids.contains(id + ""))
                        ids.add(id + "");
                } else {
                    if (ids.contains(id + "")) {
                        ids.remove(id + "");
                    }
                }
            }
        });
    }

    @Event(value = R.id.data_select_button_all)
    private void selectAll(View v) {
        if (((Button) v).getText().equals("全选")) {
            ((Button) v).setText("取消全选");
            adapter.selectAll(true);
            deleteBtn.setBackgroundColor(getResources().getColor(R.color.RED));
            deleteBtn.setText(String.format("删除(%d)", adapter.getSelected().size()));
        } else {
            ((Button) v).setText("全选");
            adapter.selectAll(false);
            deleteBtn.setBackgroundColor(Color.TRANSPARENT);
            deleteBtn.setText("取消");
        }
    }

    @Event(value = R.id.data_select_button_delete)
    private void delete(View view) {
        if (((Button) view).getText().equals("取消")) {
            adapter.clrSelectMod();
            selectBottom.setVisibility(View.GONE);
        } else {
            StringBuilder IDs = new StringBuilder();
            for (String id : ids) {
                IDs.append(id).append(",");
            }
            if (IDs.length() > 0)
                IDs.deleteCharAt(IDs.length() - 1);
            final DeleteHistoryDataTask task = new DeleteHistoryDataTask(IDs.toString());
            task.setTaskInfo(new TaskInfo<String>() {
                @Override
                public void onPreTask() {

                }

                @Override
                public void onTaskFinish(String b, String result) {

                    if (b.equals("success")) {
                        adapter.clrSelectMod();
                        selectBottom.setVisibility(View.GONE);
                        loadAll();
                    }

                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false)
                    .setTitle("删除数据")
                    .setMessage("确定要删除这些数据")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(menuPopupLocFlag, "删除", Snackbar.LENGTH_LONG).show();
                            task.execute();
                        }
                    })
                    .create()
                    .show();
        }
    }

    /**
     * 初始化排序
     */
    private void initSort() {
        isASC.put(R.id.data_menu_sort_device, false);
        isASC.put(R.id.data_menu_sort_pest_kind, false);
        isASC.put(R.id.data_menu_sort_time, false);
    }

    /**
     * 加载所有数据
     */
    private void loadAll() {
        informations = new ArrayList<>();

        DataTask task;
        if (null == screeningParam) {//默认查询最近3个月内的所有设备的全部数据
            screeningParam = new PestScreeningParam();
            String dataType = "1";
            long start = DateTime.now().plusMonths(-3).getMillis();
            long end = DateTime.now().getMillis();

            StringBuilder devs = new StringBuilder();
            ArrayList<String> deviceItems = new ArrayList<>();
            DbManager db = x.getDb(App.getDaoConfig());
            try {
                List<Device> all = db.findAll(Device.class);
                for (Device device : all) {
                    deviceItems.add(device.getDeviceNum());
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            for (String s : deviceItems) {
                devs.append(s);
                devs.append(",");
            }
            if (devs.length() > 0)
                devs.deleteCharAt(devs.length() - 1);

            screeningParam.setDataType(dataType);
            screeningParam.setDevice(devs.toString());
            screeningParam.setStartTime(start);
            screeningParam.setEndTime(end);
        }

        String start = new DateTime(screeningParam.getStartTime()).toString(DATE_FORMAT);
        String end = new DateTime(screeningParam.getEndTime()).toString(DATE_FORMAT);
        task = new DataTask(screeningParam.getDevice(), start, end, screeningParam.getDataType());
        task.setTaskInfo(new TaskInfo<List<PestInformation>>() {
            ProgressDialog pd = new ProgressDialog(getContext());

            @Override
            public void onPreTask() {
                pd.setMessage("正在加载数据");
                pd.setCancelable(false);
                pd.show();
                dataFreshLayout.setRefreshing(true);
            }

            @Override
            public void onTaskFinish(String b, List<PestInformation> result) {
                if (null != pd && pd.isShowing())
                    pd.dismiss();
                dataFreshLayout.setRefreshing(false);
                if (b.equals("fail")) {
                    try {
                        informations = new ArrayList<>(PestInformationDao.findAll());
                        adapter.refreshData(informations);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else if (b.equals("success")) {
                    informations = new ArrayList<PestInformation>(result);
                    adapter.refreshData(informations);
                }
            }
        });
        task.execute();

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

            case R.id.data_menu_sort_device:
                SortUtil.sortList(informations, "device", isASC.get(item.getItemId()));
                break;
            case R.id.data_menu_sort_pest_kind:
                SortUtil.sortList(informations, "pestKind.kindName", isASC.get(item.getItemId()));
                break;
            case R.id.data_menu_sort_time:
                SortUtil.sortList(informations, "sendTime", isASC.get(item.getItemId()));
                break;
            case R.id.data_menu_screening://筛选
                final ScreeningDialog screen = new ScreeningDialog(getContext(), getFragmentManager());
                screen.setOnScreeningFinishListener(new ScreeningDialog.OnScreeningFinishListener() {
                    @Override
                    public void onFinished(PestScreeningParam data) {
                        screeningParam = data;
                        loadAll();
                        adapter.refreshData(informations);
                        screen.dismiss();
                    }

                    @Override
                    public PestScreeningParam onOpen() {
                        return screeningParam;
                    }
                });
                screen.show();
                break;
        }
        //排序翻转
        if (isASC.containsKey(item.getItemId())) {
            isASC.put(item.getItemId(), !isASC.get(item.getItemId()));
            adapter.refreshData(informations);
        }
        return true;
    }
}
