package com.okq.pestcontrol.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.adapter.DataAdapter;
import com.okq.pestcontrol.bean.PestInformation;
import com.okq.pestcontrol.bean.param.PestScreeningParam;
import com.okq.pestcontrol.dbDao.PestInformationDao;
import com.okq.pestcontrol.widget.ScreeningDialog;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
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
    @ViewInject(value = R.id.data_fra_data_fresh)
    private SwipeRefreshLayout dataFreshLayout;
    private RecyclerView.LayoutManager mManager;

    private ArrayList<PestInformation> pests;
    private int lastVisibleItemPosition;
    private int firstVisibleItemPosition;
    private DataAdapter adapter;
    private boolean isLoadingMore = false;
    private ArrayList<PestInformation> informations;
    private PestScreeningParam screeningParam;
    private int page = 0;
    private int count = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadAll();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置开始就进行加载(需要同时设置偏移和setRefreshing(true))
//        dataFreshLayout.setProgressViewOffset(true, 0, 200);
        dataFreshLayout.setProgressViewEndTarget(true, 200);
        dataFreshLayout.setSize(SwipeRefreshLayout.LARGE);
        dataFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataFreshLayout.setEnabled(false);
                LogUtil.v("开始加载数据");
                loadData();
                LogUtil.v("加载数据完成");
                dataFreshLayout.setEnabled(true);
            }
        });
        dataFreshLayout.setColorSchemeColors(R.color.BLUE, R.color.GREEN, R.color.RED, R.color.YELLOW);
//        dataFreshLayout.setRefreshing(true);
        dataRecy.scrollBy(0, 200);
        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dataRecy.setLayoutManager(mManager);
        adapter = new DataAdapter(getContext(), pests);
        dataRecy.setAdapter(adapter);
        dataRecy.setItemAnimator(new DefaultItemAnimator());
        dataRecy.setOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
//                        lastVisibleItemPosition = ((LinearLayoutManager) mManager).findLastVisibleItemPosition();
//                        firstVisibleItemPosition = ((LinearLayoutManager) mManager).findFirstVisibleItemPosition();
//                        LogUtil.v("lastVisibleItemPosition" + lastVisibleItemPosition);
//                        LogUtil.v("FirstVisibleItemPosition--->" + ((LinearLayoutManager) mManager).findFirstVisibleItemPosition());
//                        LogUtil.v("FirstCompletelyVisibleItemPosition--->" + ((LinearLayoutManager) mManager).findFirstCompletelyVisibleItemPosition());
                        super.onScrolled(recyclerView, dx, dy);
                        int lastVisibleItem = ((LinearLayoutManager) mManager).findLastVisibleItemPosition();
                        int totalItemCount = mManager.getItemCount();
                        //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                        // dy>0 表示向下滑动
                        if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                            if (isLoadingMore) {
                                LogUtil.v("正在加载更多");
                            } else {
                                loadMore();//这里多线程也要手动控制isLoadingMore
                            }
                        }
                    }
                }

        );
        loadData();
    }

    /**
     * 加载所有数据
     */
    private void loadAll() {
        try {
            if (null == screeningParam)
                informations = new ArrayList<>(PestInformationDao.findAll());
            else
                informations = new ArrayList<>(PestInformationDao.find(screeningParam));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        page = 0;
        if (informations.size() >= count)
            pests = new ArrayList<>(informations.subList(0, count));
        else
            pests = new ArrayList<>(informations.subList(0, informations.size()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                int s = (int) (Math.random() * (pests.size() - 11));
//                pests = new ArrayList<>(pests.subList(s, s + 10));
                dataFreshLayout.setRefreshing(false);
                adapter.refreshData(pests);
            }
        }, 1000 * 2);
    }

    public void loadMore() {
        page++;
        isLoadingMore = true;
        dataFreshLayout.setRefreshing(true);
//            pests = new ArrayList<>(PestInformationDao.findAll());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((page + 1) * count < informations.size())
                    pests.addAll(pests.size(), informations.subList(page * count, (page + 1) * count));
                else if (page * count < informations.size())
                    pests.addAll(pests.size(), informations.subList(page * count, informations.size()));
                dataFreshLayout.setRefreshing(false);
                final int firstVisibleItemPosition = ((LinearLayoutManager) mManager).findFirstVisibleItemPosition();
                adapter.refreshData(pests);
                isLoadingMore = false;
                ((LinearLayoutManager) mManager).scrollToPositionWithOffset(firstVisibleItemPosition, 0);
            }
        }, 1000 * 2);
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
                final ScreeningDialog screen = new ScreeningDialog(getContext(), getFragmentManager());
                screen.setOnScreeningFinishListener(new ScreeningDialog.OnScreeningFinishListener() {
                    @Override
                    public void onFinished(PestScreeningParam data) {
                        screeningParam = data;
                        loadAll();
                        loadData();
                        screen.dismiss();
                    }

                    @Override
                    public PestScreeningParam onOpen() {
                        return screeningParam;
                    }
                });
                screen.show();
//                screen.showAsDropDown(menuPopupLocFlag);
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.alpha = 0.7f;
//                getActivity().getWindow().setAttributes(lp);
                break;
        }
        return true;
    }
}
