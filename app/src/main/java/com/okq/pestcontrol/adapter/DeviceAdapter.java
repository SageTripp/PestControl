package com.okq.pestcontrol.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.okq.pestcontrol.R;
import com.okq.pestcontrol.adapter.listener.OnItemClickListener;
import com.okq.pestcontrol.bean.Device;

import org.joda.time.DateTime;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 设备适配器 Created by Administrator on 2015/12/17.
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
    public void onBindViewHolder(final DeviceAdapter.DeviceHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.setDeviceNum(String.format("%s(%s)", device.getDeviceNum(), device.getDeviceModel()));
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult != null && reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    holder.setLocation(reverseGeoCodeResult.getAddress());
                }
            }
        });
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(new LatLng(device.getLat(), device.getLon()));
        geoCoder.reverseGeoCode(option);
//        holder.setLocation(String.format("%s %s", device.getDevice(), device.getPlace()));
        holder.setMap(device.getLat(), device.getLon());
        holder.setStatus(String.format("状态:%s", device.getStatus() == 1 ? "在线" : "离线"));
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
        private View touchView;

        public DeviceHolder(View itemView) {
            super(itemView);
            map = (TextureMapView) itemView.findViewById(R.id.holder_device_map);
            deviceNum = (TextView) itemView.findViewById(R.id.holder_device_num);
            status = (TextView) itemView.findViewById(R.id.holder_device_status);
            location = (TextView) itemView.findViewById(R.id.holder_device_location);
            iv = (ImageView) itemView.findViewById(R.id.holder_device_map_image);
            touchView = itemView.findViewById(R.id.holder_device_touch);
            map.showScaleControl(false);
            map.showZoomControls(false);
            baiduMap = map.getMap();
            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            UiSettings uiSettings = baiduMap.getUiSettings();
            uiSettings.setAllGesturesEnabled(false);
            touchView.setOnClickListener(this);
        }

        public void setMap(final double lat, final double lon) {
            //定义Maker坐标点
            LogUtil.i(String.format("进入第%d个", getAdapterPosition() + 1));
            Bitmap pic = getMyBitMap(lat, lon);
            if (null != pic) {
                iv.setImageBitmap(pic);
                iv.setVisibility(View.VISIBLE);
                map.setVisibility(View.INVISIBLE);
                mapFlags.put(getAdapterPosition(), true);
            }
            if (null == mapFlags.get(getAdapterPosition()) || !mapFlags.get(getAdapterPosition())) {
                LogUtil.i(String.format("开始第%d个", getAdapterPosition() + 1));
                LatLng point = new LatLng(lat, lon);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_location);
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
                        LoadMapThread thread = new LoadMapThread(lat, lon);
                        thread.start();
                    }
                });
            }
        }

        /**
         * 设置设备编号
         *
         * @param num 设备编号
         */
        public void setDeviceNum(String num) {
            deviceNum.setText(num);
        }

        /**
         * 设置设备状态
         *
         * @param status 设备状态
         */
        public void setStatus(String status) {
            this.status.setText(status);
        }

        /**
         * 设置设备坐标
         *
         * @param loc 设备坐标
         */
        public void setLocation(String loc) {
            location.setText(loc);
        }

        @Override
        public void onClick(View v) {
            if (null != itemClickListener) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }

        /**
         * 保存地图图片到本地
         *
         * @param mBitmap 要保存的地图图片
         * @param lat     纬度
         * @param lon     经度
         */
        public void saveMyBitmap(Bitmap mBitmap, double lat, double lon) {
            String bitName = String.valueOf(lat) +
                    "__" +
                    lon;
            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "PestControl" + File.separator + "pic");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(dir.getPath(), bitName.replace(".", "_") + ".jpg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream out = new FileOutputStream(f);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 根据经纬度从本地获取地图图片
         *
         * @param lat 纬度
         * @param lon 经度
         * @return 如果有返回地图图片, 否则返回null
         */
        public Bitmap getMyBitMap(double lat, double lon) {
            String bitName = String.valueOf(lat) +
                    "__" +
                    lon;
            File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "PestControl" + File.separator + "pic", bitName.replace(".", "_") + ".jpg");
            if (file.exists()) {
                DateTime date = new DateTime(file.lastModified());
                if (date.isBefore(DateTime.now().plusMonths(-1))) {//如果是一个月 前的图片文件,则将其删除
                    file.delete();
                    return null;
                } else
                    return BitmapFactory.decodeFile(file.getPath());
            } else {
                return null;
            }
        }

        private class LoadMapThread extends Thread {

            double lat;
            double lon;

            public LoadMapThread(double lat, double lon) {
                this.lat = lat;
                this.lon = lon;
            }

            @Override
            public void run() {
                LogUtil.i(String.format("加载完成第%d个", getAdapterPosition() + 1));
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                baiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        LogUtil.i(String.format("截图第%d个", getAdapterPosition() + 1));
                        iv.setImageBitmap(bitmap);
                        iv.setVisibility(View.VISIBLE);
                        map.setVisibility(View.INVISIBLE);
                        mapFlags.put(getAdapterPosition(), true);
                        saveMyBitmap(bitmap, lat, lon);
                    }
                });

            }
        }

    }
}
