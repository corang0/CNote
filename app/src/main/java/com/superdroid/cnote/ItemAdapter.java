package com.superdroid.cnote;

import android.content.pm.PackageManager;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {
    private ArrayList<ItemData> mList;

    public ItemAdapter(ArrayList<ItemData> list) {
        this.mList = list;
    }

    public void setmList(ArrayList<ItemData> mList) {
        this.mList = mList;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos, int type);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView content;
        protected ImageView addIcon;
        protected ImageView removeIcon;
        protected LinearLayout background;

        public CustomViewHolder(View view) {
            super(view);
            this.content = (TextView) view.findViewById(R.id.content);
            this.addIcon = (ImageView) view.findViewById(R.id.addIcon);
            this.removeIcon = (ImageView) view.findViewById(R.id.removeIcon);
            this.background = (LinearLayout) view.findViewById(R.id.background);

            this.background.setOnClickListener(this);
            this.removeIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            switch (v.getId()) {
                case R.id.removeIcon:
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, 0);
                        }
                    }
                    break;
                case R.id.background:
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, 1);
                        }
                    }
                    break;
            }
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.content.setText(mList.get(position).getContent());
        viewholder.content.setTextColor(mList.get(position).getTextColor());
        if (mList.get(position).getDone() == 1) {
            viewholder.content.setPaintFlags(viewholder.content.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (mList.get(position).getType() == 0) {
            viewholder.addIcon.setVisibility(View.GONE);
            viewholder.removeIcon.setVisibility(View.GONE);
        }
        else if (mList.get(position).getType() == 1) {
            viewholder.addIcon.setVisibility(View.GONE);
            viewholder.removeIcon.setVisibility(View.VISIBLE);
        }
        else {
            viewholder.addIcon.setVisibility(View.VISIBLE);
            viewholder.removeIcon.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
