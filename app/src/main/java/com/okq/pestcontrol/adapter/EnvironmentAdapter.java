package com.okq.pestcontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.okq.pestcontrol.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zst on 2016/3/21 0021.
 */
public class EnvironmentAdapter extends RecyclerView.Adapter<EnvironmentAdapter.ViewHolder> {

    private Context mContext;
    private List<String> environments = new ArrayList<>();

    public EnvironmentAdapter(Context mContext, List<String> environments) {
        this.mContext = mContext;
        this.environments = environments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.holder_environment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String environment = environments.get(position);
        String[] split = environment.split("=");
        holder.setName(split[0]);
        String[] values = split[1].split(",");
        holder.setUpValue(values[0]);
        holder.setDownValue(values[1]);
    }

    @Override
    public int getItemCount() {
        return environments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private EditText upValue;
        private EditText downValue;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.environment_edit_name);
            upValue = (EditText) itemView.findViewById(R.id.environment_edit_up);
            downValue = (EditText) itemView.findViewById(R.id.environment_edit_down);
        }

        public void setName(String name) {
            this.name.setText(null == name ? "" : name);
        }

        public void setUpValue(String upValue) {
            this.upValue.setText(null == upValue ? "" : upValue);
        }

        public void setDownValue(String downValue) {
            this.downValue.setText(null == downValue ? "" : downValue);
        }
    }
}
