package com.superdroid.cnote;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.CustomViewHolder> {
    private ArrayList<MemoData> mList;

    public MemoAdapter(ArrayList<MemoData> list) {
        this.mList = list;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView date;
        protected TextView colorBar;
        protected LinearLayout background;
        protected ImageView checkIcon;
        protected ImageView lockIcon;

        public CustomViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title_listitem);
            this.date = (TextView) view.findViewById(R.id.date_listitem);
            this.colorBar = (TextView) view.findViewById(R.id.colorBar);
            this.background = (LinearLayout) view.findViewById(R.id.background);
            this.checkIcon = (ImageView) view.findViewById(R.id.icon_check);
            this.lockIcon = (ImageView) view.findViewById(R.id.icon_lock);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memo_list, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.title.setText(mList.get(position).getTitle());
        viewholder.title.setTextColor(mList.get(position).getTextColor());
        viewholder.date.setText(mList.get(position).getDate());
        viewholder.colorBar.setBackgroundColor(mList.get(position).getColor());
        viewholder.background.setBackgroundColor(mList.get(position).getLightColor());
        if (mList.get(position).getDone() == 1) {
            viewholder.title.setPaintFlags(viewholder.title.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        viewholder.lockIcon.setVisibility(View.GONE);
        viewholder.checkIcon.setVisibility(View.GONE);
        if (mList.get(position).getPwd() != null) {
            viewholder.lockIcon.setImageResource(R.drawable.ic_lock_24px);
            viewholder.lockIcon.setVisibility(View.VISIBLE);
        }
        if (mList.get(position).getType() == 1) {
            viewholder.checkIcon.setImageResource(R.drawable.ic_check_24px);
            viewholder.checkIcon.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
