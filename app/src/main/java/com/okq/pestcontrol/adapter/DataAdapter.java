package com.okq.pestcontrol.adapter;

import android.content.Context;
import android.os.Vibrator;
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
        holder.setDevice(pi.getDeviceid());
        holder.setPestKind(pi.getPest());
        holder.setSendTime(pi.getTime());
        holder.setNum(pi.getValue());
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
        private TextView device;
        private TextView sendTime;
        private TextView num;
        public View divider;
        private View holder;

        public ViewHolder(View itemView) {
            super(itemView);
            pestKind = (TextView) itemView.findViewById(R.id.holder_data_pest_kind);
            device = (TextView) itemView.findViewById(R.id.holder_data_device);
            sendTime = (TextView) itemView.findViewById(R.id.holder_data_send_time);
            num = (TextView) itemView.findViewById(R.id.holder_data_num);
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

        public void setDevice(String device) {
            this.device.setText(device);
        }

        public void setPestKind(String pestKind) {
            this.pestKind.setText(pestKind);
        }

        public void setSendTime(String sendTime) {
            this.sendTime.setText(sendTime);
        }

        public void setNum(int num) {
            this.num.setText(String.format("%d", num));
        }

        @Override
        public void onClick(View v) {
            if (isCheckMod) {
                if (null != longClickListener) {
                    sba.put(getAdapterPosition(), !sba.get(getAdapterPosition(), false));
//                    setSelected();
                    longClickListener.onSelect(getAdapterPosition(), getAdapterPosition(), sba.get(getAdapterPosition()));
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
            if (!isCheckMod) {
                isCheckMod = true;
                if (null != longClickListener) {
                    Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                    sba.put(getAdapterPosition(), true);
                    longClickListener.onLongClick(getAdapterPosition(), getAdapterPosition(), true);
                    notifyDataSetChanged();
                }
                return true;
            }
            return false;
        }
    }
}
