package com.okq.pestcontrol.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.okq.pestcontrol.R;
import com.okq.pestcontrol.adapter.listener.ItemTouchAdapter;
import com.okq.pestcontrol.kotlin.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zst on 2016/3/21 0021.
 */
public class PestAdapter extends RecyclerView.Adapter<PestAdapter.ViewHolder> implements ItemTouchAdapter {

    private Context mContext;
    private List<String> pests = new ArrayList<>();
    private Map<String, String> outPests = new HashMap<>();

    public PestAdapter(Context mContext, List<String> pests) {
        this.mContext = mContext;
        this.pests = pests;
        for (String s : pests) {
            String[] split = s.split("=");
            outPests.put(split[0], split[1]);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.holder_edit_pest, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String pest = pests.get(position);
        String[] split = pest.split("=");
        holder.setName(split[0]);
        holder.setValue(split[1]);
    }

    @Override
    public int getItemCount() {
        return null == pests ? 0 : pests.size();
    }

    public String getLimit() {
        StringBuilder sb = new StringBuilder();
        for (String k : outPests.keySet()) {
            sb.append(k);
            sb.append("=");
            sb.append(outPests.get(k));
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
        outPests.remove(pests.get(position).split("=")[0]);
        pests.remove(position);
        notifyItemRemoved(position);
    }

    public void onItemAdd(final int position) {
        final String[] pestsName = Data.INSTANCE.getPestsName(pests);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("添加害虫因子")
                .setItems(pestsName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String envir = pestsName[which];
                        pests.add(position, envir + "=0");
                        outPests.put(envir, "0");
                        notifyItemInserted(position);
                    }
                })
                .create()
                .show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private EditText value;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.pest_edit_name);
            value = (EditText) itemView.findViewById(R.id.pest_edit_limit);
            value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    outPests.put(name.getText().toString(), s.toString());
                }
            });
        }

        public void setName(String name) {
            this.name.setText(null == name ? "" : name);
        }

        void setValue(String value) {
            this.value.setText(null == value ? "" : value);
        }
    }
}
