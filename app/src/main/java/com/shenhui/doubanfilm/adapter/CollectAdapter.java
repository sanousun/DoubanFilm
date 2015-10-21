package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BaseAdapter;
import com.shenhui.doubanfilm.bean.Subject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CollectAdapter extends BaseAdapter<CollectAdapter.ViewHolder> {

    private static final String URI_FOR_FILE = "file:/";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Subject> mData;

    private OnItemClickListener callback;

    private Subject undoSub;

    public CollectAdapter(Context context, List<Subject> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateList(List<Subject> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_collect_layout, parent, false);
        return new ViewHolder(view, new OnResponseClickListener() {
            @Override
            public void onEnterClick(int pos) {
                callback.itemClick(mData.get(pos).getId());
            }

            @Override
            public void onDeleteClick(View v, final int pos) {
                removeItem(pos);
            }
        });
    }

    /**
     * 移除相应的item
     */
    private void removeItem(int pos) {
        notifyItemRemoved(pos);
        undoSub = mData.get(pos);
        mData.remove(pos);
        callback.itemRemove(pos, undoSub.getId());
    }

    /**
     * 用于撤销“取消收藏”操作
     */
    public void cancelRemove(int pos) {
        if (undoSub != null) {
            notifyItemInserted(pos);
            mData.add(pos, undoSub);
            undoSub = null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subject sub = mData.get(position);
        float rate = (float) sub.getRating().getAverage();
        holder.rating.setText(String.format("%s", rate));
        String title = sub.getTitle();
        holder.title.setText(title);
        holder.year.setText(String.format("  %s  ", sub.getYear()));
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < sub.getGenres().size(); i++) {
            s.append(i == 0 ? "" : "，").append(sub.getGenres().get(i));
        }
        holder.genres.setText(s.toString());
        s = new StringBuilder();
        s.append(mContext.getString(R.string.directors));
        for (int i = 0; i < sub.getDirectors().size(); i++) {
            s.append(i == 0 ? "" : "，").append(sub.getDirectors().get(i).getName());
        }
        s.append(String.format("/%s", mContext.getString(R.string.casts)));
        for (int i = 0; i < sub.getCasts().size(); i++) {
            s.append(i == 0 ? "" : "，");
            s.append(sub.getCasts().get(i).getName());
        }
        holder.celebrity.setText(s.toString());
        if (sub.getLocalImageFile() != null) {
            imageLoader.displayImage(URI_FOR_FILE + sub.getLocalImageFile(), holder.image, options);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        @Bind(R.id.iv_col_image)
        ImageView image;
        @Bind(R.id.tv_col_rating)
        TextView rating;
        @Bind(R.id.tv_col_title)
        TextView title;
        @Bind(R.id.tv_col_year)
        TextView year;
        @Bind(R.id.tv_col_genres)
        TextView genres;
        @Bind(R.id.tv_col_cel)
        TextView celebrity;
        @Bind(R.id.tv_col_delete)
        TextView delete;
        @Bind(R.id.tv_col_enter)
        TextView enter;

        private OnResponseClickListener mListener;

        public ViewHolder(View itemView, OnResponseClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
            delete.setOnClickListener(this);
            enter.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == delete) {
                mListener.onDeleteClick(view, getAdapterPosition());
            } else {
                mListener.onEnterClick(getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void itemClick(String id);

        void itemRemove(int pos, String id);
    }

    public interface OnResponseClickListener {
        void onEnterClick(int pos);

        void onDeleteClick(View v, int pos);
    }
}