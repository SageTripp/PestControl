package com.okq.pestcontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.adapter.listener.ItemTouchAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zst on 2016/3/21 0021.
 */
public class EnvironmentAdapter extends RecyclerView.Adapter<EnvironmentAdapter.ViewHolder> implements ItemTouchAdapter {

    private Context mContext;
    private List<String> environments = new ArrayList<>();
    private Map<String, String> outEnvirs = new HashMap<>();

    public EnvironmentAdapter(Context mContext, List<String> environments) {
        this.mContext = mContext;
        this.environments = environments;
        for (String s : environments) {
            String[] split = s.split("=");
            outEnvirs.put(split[0], split[1]);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.holder_edit_environment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String environment = environments.get(position);
        String[] split = environment.split("=");
        holder.setName(split[0]);
        String[] values = split[1].split("/");
        holder.setUpValue(values[0]);
        holder.setDownValue(values[1]);
    }

    @Override
    public int getItemCount() {
        return null == environments ? 0 : environments.size();
    }

    public String getLimit() {
        StringBuilder sb = new StringBuilder();
        for (String k : outEnvirs.keySet()) {
            sb.append(k);
            sb.append("=");
            sb.append(outEnvirs.get(k));
            sb.append(",");
        }
        if (sb.toString().endsWith(",")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        outEnvirs.remove(environments.get(position).split("=")[0]);
        environments.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private EditText upValue;
        private EditText downValue;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.environment_edit_name);
            upValue = (EditText) itemView.findViewById(R.id.environment_edit_up);
            downValue = (EditText) itemView.findViewById(R.id.environment_edit_down);
            upValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = outEnvirs.get(name.getText().toString());
                    s1 = s1.replaceAll("\\d*/", s.toString() + "/");
                    outEnvirs.put(name.getText().toString(), s1);
                }
            });
            downValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = outEnvirs.get(name.getText().toString());
                    s1 = s1.replaceAll("/\\d*", "/" + s.toString());
                    outEnvirs.put(name.getText().toString(), s1);
                }
            });
        }

        public void setName(String name) {
            this.name.setText(null == name ? "" : name);
        }

        void setUpValue(String upValue) {
            this.upValue.setText(null == upValue ? "" : upValue);
        }

        void setDownValue(String downValue) {
            this.downValue.setText(null == downValue ? "" : downValue);
        }
    }
}
