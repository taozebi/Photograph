package com.ewide.photograph.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewide.photograph.R;
import com.ewide.photograph.app.project.model.entry.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目列表适配器
 * Created by Taoze on 2018/9/8.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    private List<Project> mDatas;
    private Context mContext;

    public ProjectListAdapter(Context context, List<Project> mDatas) {
        this.mDatas = mDatas;
        mContext = context;

    }

    public void setDatas(List<Project> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public List<Project> getDatas() {
        return this.mDatas;
    }

    public Object getItem(int position) {
        if (getDatas() != null && getDatas().size() > 0) {
            return getDatas().get(position);
        } else {
            return null;
        }
    }

    @Override
    public ProjectListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_project, parent, false);
        ProjectListAdapter.ViewHolder viewHolder = new ProjectListAdapter.ViewHolder(view, itemClickListener, itemLongClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectListAdapter.ViewHolder holder, int position) {
        Project data = mDatas.get(position);
        holder.mProjectItemNameTv.setText(data.getProjectName());
        holder.mProjectItemDescTv.setText(data.getProjectPath());
        String time = data.getCreateTime();
        if(!TextUtils.isEmpty(time)){
            time = time.substring(0,10);
        }
        holder.mProjectItemTimeTv.setText(time);
        holder.mProjectItemCountTv.setText(data.getFileCount() + "");
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mProjectItemLl)
        LinearLayout mProjectItemLl;
        @BindView(R.id.mProjectItemIconIv)
        ImageView mProjectItemIconIv;
        @BindView(R.id.mProjectItemNameTv)
        TextView mProjectItemNameTv;
        @BindView(R.id.mProjectItemDescTv)
        TextView mProjectItemDescTv;

        @BindView(R.id.mProjectItemCountTv)
        TextView mProjectItemCountTv;

        @BindView(R.id.mProjectItemTimeTv)
        TextView mProjectItemTimeTv;

        private ProjectListAdapter.OnItemClickListener itemClickListener;
        private ProjectListAdapter.OnItemLongClickListener itemLongClickListener;

        ViewHolder(View itemView, ProjectListAdapter.OnItemClickListener listener, ProjectListAdapter.OnItemLongClickListener mLongListener) {
            super(itemView);
            this.itemClickListener = listener;
            this.itemLongClickListener = mLongListener;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemLongClickListener.onItemLongClick(v, (Integer) v.getTag());
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            });
            ButterKnife.bind(this, itemView);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int postion);

    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View view, int postion);

    }

}
