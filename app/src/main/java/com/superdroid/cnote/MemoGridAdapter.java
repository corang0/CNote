package com.superdroid.cnote;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoGridAdapter extends RecyclerView.Adapter<MemoGridAdapter.CustomViewHolder> {
    private ArrayList<MemoData> mList;

    public MemoGridAdapter(ArrayList<MemoData> list) {
        this.mList = list;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView content;
        protected TextView colorBar;
        protected LinearLayout background;
        protected ImageView checkIcon;
        protected ImageView lockIcon;

        public CustomViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title_listitem);
            this.content = (TextView) view.findViewById(R.id.content_listitem);
            this.colorBar = (TextView) view.findViewById(R.id.colorBar);
            this.background = (LinearLayout) view.findViewById(R.id.background);
            this.checkIcon = (ImageView) view.findViewById(R.id.icon_check);
            this.lockIcon = (ImageView) view.findViewById(R.id.icon_lock);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memo_grid_list, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.title.setText(mList.get(position).getTitle());
        viewholder.title.setTextColor(mList.get(position).getTextColor());
        viewholder.content.setText(mList.get(position).getDate());
        viewholder.content.setTextColor(mList.get(position).getTextColor());
        viewholder.colorBar.setBackgroundColor(mList.get(position).getColor());
        viewholder.background.setBackgroundColor(mList.get(position).getLightColor());
        if (mList.get(position).getDone() == 1) {
            viewholder.title.setPaintFlags(viewholder.title.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            viewholder.content.setPaintFlags(viewholder.title.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (mList.get(position).getPwd() != null) {
            viewholder.content.setVisibility(View.INVISIBLE);
            viewholder.lockIcon.setVisibility(View.VISIBLE);
        }
        else {
            viewholder.content.setVisibility(View.VISIBLE);
            viewholder.lockIcon.setVisibility(View.INVISIBLE);
        }
        if (mList.get(position).getType() == 1) {
            viewholder.checkIcon.setImageResource(R.drawable.ic_check_24px);
        }
    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
