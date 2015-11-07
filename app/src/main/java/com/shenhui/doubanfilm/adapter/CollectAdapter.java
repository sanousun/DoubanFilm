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
import com.shenhui.doubanfilm.bean.SubjectBean;
import com.shenhui.doubanfilm.support.util.CelebrityUtil;
import com.shenhui.doubanfilm.support.util.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CollectAdapter extends BaseAdapter<CollectAdapter.ViewHolder> {

    private static final String URI_FOR_FILE = "file:/";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SubjectBean> mData;

    private OnItemClickListener callback;

    private SubjectBean undoSub;

    public CollectAdapter(Context context, List<SubjectBean> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateList(List<SubjectBean> data) {
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
        SubjectBean sub = mData.get(position);
        float rate = (float) sub.getRating().getAverage();
        holder.rating.setText(String.format("%s", rate));
        String title = sub.getTitle();
        holder.title.setText(title);
        holder.year.setText(String.format("  %s  ", sub.getYear()));
        holder.genres.setText(StringUtil.getListString(sub.getGenres(), ','));
        StringBuilder sb = new StringBuilder();
        sb.append(mContext.getString(R.string.directors)).
                append(CelebrityUtil.list2String(sub.getDirectors(), ',')).
                append('/').
                append(mContext.getString(R.string.casts)).
                append(CelebrityUtil.list2String(sub.getCasts(), ','));
        holder.celebrity.setText(sb.toString());
        if (sub.getLocalImageFile() != null) {
            imageLoader.displayImage(
                    String.format("%s%s", URI_FOR_FILE, sub.getLocalImageFile()),
                    holder.image, options);
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