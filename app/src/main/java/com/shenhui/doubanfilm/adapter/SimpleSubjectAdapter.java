package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.app.GlideApp;
import com.shenhui.doubanfilm.bean.SimpleSubjectBean;
import com.shenhui.doubanfilm.support.util.CelebrityUtil;
import com.shenhui.doubanfilm.support.util.DensityUtil;
import com.shenhui.doubanfilm.support.util.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleSubjectAdapter extends BaseAdapter<RecyclerView.ViewHolder> {

    //ItemView的类型，FootView应用于加载更多
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOT = 1;

    //FootView的显示类型
    public static final int FOOT_LOADING = 0;
    public static final int FOOT_COMPLETED = 1;
    public static final int FOOT_FAIL = 2;
    private FootViewHolder mFootView;
    //用于判断是否是加载失败时点击的FootView
    public static final String FOOT_VIEW_ID = "-1";

    private Context mContext;
    private List<SimpleSubjectBean> mData;
    /**
     * 用于加载更多数据
     */
    private int mTotalDataCount = 0;
    /**
     * 判断是否属于“即将上映”
     */
    private boolean isComingFilm;


    public SimpleSubjectAdapter(Context context, List<SimpleSubjectBean> data) {
        this(context, data, false);
    }

    public SimpleSubjectAdapter(Context context, List<SimpleSubjectBean> data,
                                boolean isComingFilm) {
        this.mContext = context;
        this.mData = data;
        this.isComingFilm = isComingFilm;
    }

    /**
     * 用于加载数据时的url起点
     */
    public int getStart() {
        return mData.size();
    }

    public void setTotalDataCount(int totalDataCount) {
        this.mTotalDataCount = totalDataCount;
    }

    /**
     * 返回adapter数据的总数
     */
    public int getTotalDataCount() {
        return mTotalDataCount;
    }

    /**
     * 判断是否已经加载完毕
     */
    public boolean isLoadCompleted() {
        return mData.size() >= getTotalDataCount();
    }

    /**
     * 用于加载更多item
     */
    public void loadMoreData(List<SimpleSubjectBean> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void loadFail() {
        mFootView.setFootView(FOOT_FAIL);
    }

    /**
     * 用于更新数据
     *
     * @param data           更新的数据
     * @param totalDataCount 数据的总量，采取多次加载
     */
    public void updateList(List<SimpleSubjectBean> data, int totalDataCount) {
        this.mData = data;
        setTotalDataCount(totalDataCount);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOT) {
            if (mFootView == null) {
                View view = LayoutInflater.from(mContext).
                        inflate(R.layout.view_load_tips, parent, false);
                mFootView = new FootViewHolder(view);
            }
            return mFootView;
        } else {
            View view = LayoutInflater.from(mContext).
                    inflate(R.layout.item_simple_subject_layout, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == TYPE_FOOT) {
            ((FootViewHolder) viewHolder).update();
        } else {
            ((ItemViewHolder) viewHolder).update();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return TYPE_FOOT;
        } else {
            return TYPE_ITEM;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_item_simple_subject_image)
        ImageView image_film;
        @Bind(R.id.ll_item_simple_subject_rating)
        LinearLayout layout_rating;
        @Bind(R.id.rb_item_simple_subject_rating)
        RatingBar rating_bar;
        @Bind(R.id.tv_item_simple_subject_rating)
        TextView text_rating;
        @Bind(R.id.tv_item_simple_subject_count)
        TextView text_collect_count;
        @Bind(R.id.tv_item_simple_subject_title)
        TextView text_title;
        @Bind(R.id.tv_item_simple_subject_original_title)
        TextView text_original_title;
        @Bind(R.id.tv_item_simple_subject_genres)
        TextView text_genres;
        @Bind(R.id.tv_item_simple_subject_director)
        TextView text_director;
        @Bind(R.id.tv_item_simple_subject_cast)
        TextView text_cast;

        SimpleSubjectBean sub;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void update() {
            sub = mData.get(getLayoutPosition());
            if (!isComingFilm) {
                layout_rating.setVisibility(View.VISIBLE);
                float rate = (float) sub.getRating().getAverage();
                rating_bar.setRating(rate / 2);
                text_rating.setText(String.format("%s", rate));
                text_collect_count.setText(mContext.getString(R.string.collect));
                text_collect_count.append(String.format("%d", sub.getCollect_count()));
                text_collect_count.append(mContext.getString(R.string.count));
            }
            String title = sub.getTitle();
            String original_title = sub.getOriginal_title();
            text_title.setText(title);
            if (original_title.equals(title)) {
                text_original_title.setVisibility(View.GONE);
            } else {
                text_original_title.setText(original_title);
                text_original_title.setVisibility(View.VISIBLE);
            }
            text_genres.setText(StringUtil.getListString(sub.getGenres(), ','));
            text_director.setText(StringUtil.getSpannableString(
                    mContext.getString(R.string.directors), Color.GRAY));
            text_director.append(CelebrityUtil.list2String(sub.getDirectors(), '/'));
            text_cast.setText(StringUtil.getSpannableString(
                    mContext.getString(R.string.casts), Color.GRAY));
            text_cast.append(CelebrityUtil.list2String(sub.getCasts(), '/'));
            GlideApp.with(itemView.getContext())
                    .load(sub.getImages().getLarge())
                    .into(image_film);
        }

        @Override
        public void onClick(View view) {
            if (mCallback != null) {
                int position = getLayoutPosition();
                mCallback.onItemClick(mData.get(position).getId(),
                        mData.get(position).getImages().getLarge());
            }
        }
    }

    /**
     * recyclerView上拉加载更多的footViewHolder
     */
    class FootViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ProgressBar progress_bar;
        private TextView text_load_tip;


        public FootViewHolder(final View itemView) {
            super(itemView);
            progress_bar = (ProgressBar) itemView.findViewById(R.id.pb_view_load_tip);
            text_load_tip = (TextView) itemView.findViewById(R.id.tv_view_load_tip);
            itemView.setOnClickListener(this);
        }

        public void update() {
            if (isLoadCompleted()) setFootView(FOOT_COMPLETED);
            else setFootView(FOOT_LOADING);
        }

        public void setFootView(int event) {
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            switch (event) {
                case FOOT_LOADING:
                    params.height = DensityUtil.dp2px(mContext, 40f);
                    itemView.setLayoutParams(params);
                    progress_bar.setVisibility(View.VISIBLE);
                    text_load_tip.setText(mContext.getString(R.string.foot_loading));
                    itemView.setClickable(false);
                    break;
                case FOOT_COMPLETED:
                    params.height = 0;
                    itemView.setLayoutParams(params);
                    itemView.setClickable(false);
                    break;
                case FOOT_FAIL:
                    params.height = DensityUtil.dp2px(mContext, 40f);
                    itemView.setLayoutParams(params);
                    progress_bar.setVisibility(View.GONE);
                    text_load_tip.setText(mContext.getString(R.string.foot_fail));
                    itemView.setClickable(true);
            }
        }

        @Override
        public void onClick(View view) {
            if (mCallback != null) {
                setFootView(FOOT_LOADING);
                mCallback.onItemClick(FOOT_VIEW_ID, null);
            }
        }
    }
}
