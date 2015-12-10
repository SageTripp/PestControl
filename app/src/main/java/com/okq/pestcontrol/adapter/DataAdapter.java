package com.okq.pestcontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.bean.PestInformation;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * 害虫信息展示adapter
 * Created by Administrator on 2015/12/8.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PestInformation> pestList;

    public DataAdapter(Context context, ArrayList<PestInformation> pestList) {
        this.mContext = context;
        this.pestList = pestList == null ? new ArrayList<PestInformation>() : pestList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.holder_data, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PestInformation pi = pestList.get(position);
        holder.setArea(pi.getArea());
        holder.setTemperature(pi.getTemperature());
        holder.setHumidity(pi.getHumidity());
        holder.setPestKind(pi.getPestKind().getKindName() + "  " + position + "/" + getItemCount());
        holder.setSendTime(pi.getSendTime());
    }

    @Override
    public int getItemCount() {
        return pestList.size();
    }


    public void refrashData(ArrayList<PestInformation> newList) {
        this.pestList = newList == null ? new ArrayList<PestInformation>() : newList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView pestKind;
        private TextView temperature;
        private TextView humidity;
        private TextView area;
        private TextView sendTime;

        public ViewHolder(View itemView) {
            super(itemView);
            pestKind = (TextView) itemView.findViewById(R.id.holder_data_pest_kind);
            temperature = (TextView) itemView.findViewById(R.id.holder_data_temperature);
            humidity = (TextView) itemView.findViewById(R.id.holder_data_humidity);
            area = (TextView) itemView.findViewById(R.id.holder_data_area);
            sendTime = (TextView) itemView.findViewById(R.id.holder_data_send_time);
        }

        public void setArea(String area) {
            this.area.setText(area);
        }

        public void setPestKind(String pestKind) {
            this.pestKind.setText(pestKind);
        }

        public void setTemperature(int temperature) {
            this.temperature.setText(temperature + "℃");
        }

        public void setHumidity(int humidity) {
            this.humidity.setText(humidity + "%");
        }

        public void setSendTime(long sendTime) {
            this.sendTime.setText(new DateTime(sendTime).toString("yyyy/MM/dd HH:mm"));
        }
    }
}
