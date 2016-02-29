package com.okq.pestcontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.adapter.listener.OnItemClickListener;
import com.okq.pestcontrol.adapter.listener.OnItemLongClickListener;
import com.okq.pestcontrol.bean.PestInformation;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 害虫信息展示adapter Created by Administrator on 2015/12/8.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PestInformation> pestList;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener longClickListener;

    private SparseBooleanArray sba;

    private boolean isCheckMod = false;

    public DataAdapter(Context context, ArrayList<PestInformation> pestList) {
        this.mContext = context;
        this.pestList = pestList == null ? new ArrayList<PestInformation>() : pestList;
        this.sba = new SparseBooleanArray(pestList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.holder_data, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.divider.setVisibility(View.VISIBLE);
        PestInformation pi = pestList.get(position);
        holder.setArea(pi.getArea());
        holder.setTemperature(pi.getTemperature());
        holder.setHumidity(pi.getHumidity());
        holder.setPestKind(pi.getPestKind().getKindName() + "  " + position + "/" + getItemCount());
        holder.setSendTime(pi.getSendTime());
        holder.clrSelected();
        if (isCheckMod && sba.get(position, false))
            holder.setSelected();
        if (position == getItemCount() - 1) {
            holder.divider.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pestList.size();
    }


    public void refreshData(ArrayList<PestInformation> newList) {
        this.pestList = newList == null ? new ArrayList<PestInformation>() : newList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void clrSelectMod() {
        this.isCheckMod = false;
        sba.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getSelected() {
        List<Integer> selects = new ArrayList<>();
        for (int i = 0; i < sba.size(); i++) {
            boolean b = sba.valueAt(i);
            if (b)
                selects.add(sba.keyAt(i));
        }
        return selects;
    }

    public void selectAll(boolean isSel) {
        for (int i = 0; i < pestList.size(); i++) {
            sba.put(i, isSel);
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView pestKind;
        private TextView temperature;
        private TextView humidity;
        private TextView area;
        private TextView sendTime;
        public View divider;
        private View holder;

        public ViewHolder(View itemView) {
            super(itemView);
            pestKind = (TextView) itemView.findViewById(R.id.holder_data_pest_kind);
            temperature = (TextView) itemView.findViewById(R.id.holder_data_temperature);
            humidity = (TextView) itemView.findViewById(R.id.holder_data_humidity);
            area = (TextView) itemView.findViewById(R.id.holder_data_area);
            sendTime = (TextView) itemView.findViewById(R.id.holder_data_send_time);
            divider = itemView.findViewById(R.id.data_fra_item_divider);
            divider.setVisibility(View.VISIBLE);
            holder = itemView;
            holder.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setSelected() {
            holder.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
        }

        public void clrSelected() {
            holder.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        public void setArea(String area) {
            this.area.setText(area);
        }

        public void setPestKind(String pestKind) {
            this.pestKind.setText(pestKind);
        }

        public void setTemperature(int temperature) {
            this.temperature.setText(String.format("%d℃", temperature));
        }

        public void setHumidity(int humidity) {
            this.humidity.setText(String.format("%d%%", humidity));
        }

        public void setSendTime(long sendTime) {
            this.sendTime.setText(new DateTime(sendTime).toString("yyyy/MM/dd HH:mm"));
        }

        @Override
        public void onClick(View v) {
            if (isCheckMod) {
                if (null != longClickListener) {
                    sba.put(getAdapterPosition(), !sba.get(getAdapterPosition(), false));
//                    setSelected();
                    longClickListener.onSelect(getAdapterPosition(), getAdapterPosition(), true);
                    notifyDataSetChanged();
                }
            } else {
                if (null != itemClickListener) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            isCheckMod = true;
            if (null != longClickListener) {
//                setSelected();
                sba.put(getAdapterPosition(), true);
                longClickListener.onLongClick(getAdapterPosition(), getAdapterPosition(), true);
                notifyDataSetChanged();
            }
            return true;
        }
    }
}
