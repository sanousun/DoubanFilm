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
        if (sub.getRating() != null) {
            float rate = (float) sub.getRating().getAverage();
            holder.text_rating.setText(String.format("%s", rate));
        }
        String title = sub.getTitle();
        holder.text_title.setText(title);
        holder.text_year.setText(String.format("  %s  ", sub.getYear()));
        holder.text_genres.setText(StringUtil.getListString(sub.getGenres(), ','));
        holder.text_cast.setText(String.format("%s%s/%s%s",
                mContext.getString(R.string.directors),
                CelebrityUtil.list2String(sub.getDirectors(), ','),
                mContext.getString(R.string.casts),
                CelebrityUtil.list2String(sub.getCasts(), ',')));
        if (sub.getLocalImageFile() != null) {
            imageLoader.displayImage(
                    String.format("%s%s", URI_FOR_FILE, sub.getLocalImageFile()),
                    holder.image_film, options);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        @Bind(R.id.iv_item_collect_image)
        ImageView image_film;
        @Bind(R.id.tv_item_collect_rating)
        TextView text_rating;
        @Bind(R.id.tv_item_collect_title)
        TextView text_title;
        @Bind(R.id.tv_item_collect_year)
        TextView text_year;
        @Bind(R.id.tv_item_collect_genres)
        TextView text_genres;
        @Bind(R.id.tv_item_collect_cel)
        TextView text_cast;
        @Bind(R.id.tv_item_collect_delete)
        TextView btn_delete;
        @Bind(R.id.tv_item_collect_enter)
        TextView btn_enter;

        private OnResponseClickListener mListener;

        public ViewHolder(View itemView, OnResponseClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
            btn_delete.setOnClickListener(this);
            btn_enter.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == btn_delete) {
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