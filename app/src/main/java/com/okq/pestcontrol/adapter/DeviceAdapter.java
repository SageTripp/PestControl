package com.okq.pestcontrol.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.drawable.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.adapter.listener.OnItemClickListener;
import com.okq.pestcontrol.bean.Device;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 设备适配器
 * Created by Administrator on 2015/12/17.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    private Context context;
    private ArrayList<Device> deviceList;
    private OnItemClickListener itemClickListener;
    private HashMap<Integer, Boolean> mapFlags;

    public DeviceAdapter(Context context, List<Device> devices) {
        this.context = context;
        deviceList = new ArrayList<>(devices);
        mapFlags = new HashMap<>();
        for (int i = 0; i < devices.size(); i++) {
            mapFlags.put(i, false);
        }
    }

    @Override
    public DeviceAdapter.DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceHolder(LayoutInflater.from(context).inflate(R.layout.holder_device, parent, false));
    }

    @Override
    public void onBindViewHolder(DeviceAdapter.DeviceHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.setDeviceNum(String.format("%s(%s)", device.getDeviceNum(), device.getDeviceModel()));
        holder.setLocation(String.format("%s %s", device.getArea(), device.getPlace()));
        holder.setMap(device.getLat(), device.getLon());
        holder.setStatus(String.format("状态:%s", device.getStatus()));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    /**
     * 设置项目点击事件监听器
     *
     * @param listener 项目点击事件监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onViewRecycled(DeviceHolder holder) {
        holder.baiduMap.clear();
        super.onViewRecycled(holder);
    }

    public class DeviceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextureMapView map;
        private TextView deviceNum;
        private TextView status;
        private TextView location;
        private BaiduMap baiduMap;
        private MapStatus mapStatus;
        private ImageView iv;

        public DeviceHolder(View itemView) {
            super(itemView);
            map = (TextureMapView) itemView.findViewById(R.id.holder_device_map);
            deviceNum = (TextView) itemView.findViewById(R.id.holder_device_num);
            status = (TextView) itemView.findViewById(R.id.holder_device_status);
            location = (TextView) itemView.findViewById(R.id.holder_device_location);
            iv = (ImageView) itemView.findViewById(R.id.holder_device_map_image);
            map.showScaleControl(false);
            map.showZoomControls(false);
            baiduMap = map.getMap();
            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            UiSettings uiSettings = baiduMap.getUiSettings();
            uiSettings.setAllGesturesEnabled(false);
            itemView.setOnClickListener(this);
        }

        public void setMap(double lat, double lon) {
            //定义Maker坐标点
            LogUtil.i(String.format("进入第%d个", getAdapterPosition() + 1));
            if (null == mapFlags.get(getAdapterPosition()) || !mapFlags.get(getAdapterPosition())) {
                LogUtil.i(String.format("开始第%d个", getAdapterPosition() + 1));
                LatLng point = new LatLng(lat, lon);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_slide_menu_device);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap)
                        .zIndex(9)
                        .draggable(false);
                //在地图上添加Marker，并显示
                baiduMap.addOverlay(option);
                mapStatus = new MapStatus.Builder()
                        .target(point)
                        .zoom(13)
                        .build();
                baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
                baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        LogUtil.i(String.format("加载完成第%d个", getAdapterPosition() + 1));
                        baiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                            @Override
                            public void onSnapshotReady(Bitmap bitmap) {
                                LogUtil.i(String.format("截图第%d个", getAdapterPosition() + 1));
                                iv.setImageBitmap(bitmap);
                                iv.setVisibility(View.VISIBLE);
                                map.setVisibility(View.INVISIBLE);
                                mapFlags.put(getAdapterPosition(), true);
                            }
                        });

                    }
                });
            }
        }

        public void setDeviceNum(String num) {
            deviceNum.setText(num);
        }

        public void setStatus(String status) {
            this.status.setText(status);
        }

        public void setLocation(String loc) {
            location.setText(loc);
        }

        @Override
        public void onClick(View v) {
            if (null != itemClickListener) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
