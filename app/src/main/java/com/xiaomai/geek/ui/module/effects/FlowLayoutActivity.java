package com.xiaomai.geek.ui.module.effects;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.data.module.Effect;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.widget.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoMai on 2017/11/21.
 */

public class FlowLayoutActivity extends BaseActivity {

    private List<Effect> mEffects = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_flow_layout;
    }

    @Override
    public void initViews() {
        super.initViews();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new FlowLayoutManager());

        for (int i = 0; i < 100; i++) {
            mEffects.add(new Effect("card", R.layout.label_layout));
        }

        MyAdapter adapter = new MyAdapter(mEffects);
        recyclerView.setAdapter(adapter);
    }

    private static class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final String TAG = "MyAdapter";
        @Nullable
        private List<Effect> list;
        @Nullable
        private OnItemClickListener onItemClickListener;

        public MyAdapter(@Nullable List<Effect> list) {
            this.list = list;
        }

        public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
            onItemClickListener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AppLog.d(TAG, "onCreateViewHolder");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flowlayout, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            AppLog.d(TAG, "onBindViewHolder");
            final Effect effect = getItem(position);
            if (effect == null) {
                return;
            }
            Holder holder;
            if (viewHolder instanceof Holder) {
                holder = (Holder) viewHolder;
                holder.textView.setText(String.valueOf(position));
                if (onItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(effect);
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public Effect getItem(int position) {
            if (list == null) {
                return null;
            }
            return list.get(position);
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {
        TextView textView;

        private Holder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text);
        }
    }

    private interface OnItemClickListener {
        void onItemClick(Effect effect);
    }
}
