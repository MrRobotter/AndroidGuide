package com.joinyon.androidguide;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 作者： JoinYon on 2018/8/10.
 * 邮箱：2816886869@qq.com
 */
public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {
    private Context mContext;
    private List<String> titles;

    public GuideAdapter(Context mContext, List<String> titles) {
        this.mContext = mContext;
        this.titles = titles;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.guide_item, null);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.rlItem.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#F0F0F0") : Color.parseColor("#F4F8FE"));
        String title = titles.get(position);
        holder.tvName.setText(title);
        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private RelativeLayout rlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            rlItem = itemView.findViewById(R.id.rlItem);
        }
    }
}
