
package com.xiaomai.geek.ui.module.password;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomai.geek.R;

import java.io.File;
import java.util.List;

/**
 * Created by XiaoMai on 2017/4/11 17:39.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {

    private List<File> files;

    public FileListAdapter(List<File> files) {
        this.files = files;
    }

    @Override
    public int getItemCount() {
        return files.size() + 1;
    }

    public void setNewData(List<File> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         * 使用inflate(R.layout.item_file, null)会造成条目的宽度不能铺面全屏
         * 改为inflate(R.layout.item_file, parent, false)就可以了 inflate
         * 传入parent和null与什么区别
         */
        return new FileViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false));
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, final int position) {
        if (position == 0) {
            holder.tv_fileName.setText("...");
            holder.iv_fileType.setImageResource(R.drawable.folder);
        } else {
            File file = files.get(position - 1);
            if (file.isDirectory()) {
                holder.iv_fileType.setImageResource(R.drawable.folder);
            } else {
                holder.iv_fileType.setImageResource(R.drawable.file);
            }
            holder.tv_fileName.setText(file.getName());
        }
        holder.ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        View ll_container;

        ImageView iv_fileType;

        TextView tv_fileName;

        public FileViewHolder(View itemView) {
            super(itemView);
            iv_fileType = (ImageView) itemView.findViewById(R.id.file_type);
            tv_fileName = (TextView) itemView.findViewById(R.id.tv_fileName);
            ll_container = itemView.findViewById(R.id.ll_container);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
