package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.app.GlideApp;
import com.shenhui.doubanfilm.bean.SimpleCardBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleFilmAdapter extends RecyclerView.Adapter<SimpleFilmAdapter.ViewHolder> {

    private Context mContext;
    private List<SimpleCardBean> mData = new ArrayList<>();
    private OnItemClickListener callback;

    public SimpleFilmAdapter(Context context) {
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    public void update(List<SimpleCardBean> data) {
        mData.clear();
        notifyDataSetChanged();
        for (int i = 0; i < data.size(); i++) {
            mData.add(data.get(i));
            notifyItemInserted(i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).
                inflate(R.layout.item_simple_film_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_item_simple_film_image)
        ImageView image_film;
        @Bind(R.id.tv_item_simple_film_text)
        TextView text_title;

        SimpleCardBean subj;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void update() {
            subj = mData.get(getLayoutPosition());
            GlideApp.with(mContext).load(subj.getImage()).into(image_film);
            text_title.setText(subj.getName());
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            if (callback != null) {
                callback.itemClick(mData.get(pos).getId(),
                        mData.get(pos).getImage(),
                        mData.get(pos).getIsFilm());
            }
        }
    }

    public interface OnItemClickListener {
        void itemClick(String id, String imageUrl, boolean isFilm);
    }

}
