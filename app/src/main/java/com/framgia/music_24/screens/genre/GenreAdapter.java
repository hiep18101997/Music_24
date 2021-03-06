package com.framgia.music_24.screens.genre;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.music_24.R;
import com.framgia.music_24.data.model.Track;
import com.framgia.music_24.utils.StringUtils;
import java.util.List;

import static com.framgia.music_24.screens.play.PlayMusicFragment.PLAY_FAVORITE;

/**
 * Created by CuD HniM on 18/08/26.
 */
public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Track> mTracks;
    private OnClickListener mListener;

    GenreAdapter(Context context, List<Track> tracks, OnClickListener OnClickListener) {
        mContext = context;
        mTracks = tracks;
        mListener = OnClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }
        if (viewType == VIEW_TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.item_genre, viewGroup, false);
            return new GenreViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.item_loading, viewGroup, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_LOADING = 1;
        return mTracks.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof GenreViewHolder) {
            GenreViewHolder genreViewHolder = (GenreViewHolder) viewHolder;
            genreViewHolder.bindData(mContext, mTracks, mListener);
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
            loadingViewHolder.mProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public interface OnClickListener {
        void OnItemClick(List<Track> tracks, int position);

        void OnFavoriteClick(List<Track> tracks, int position);
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageViewAva;
        private ImageView mImageViewFavorite;
        private TextView mTextViewName;
        private TextView mTextViewSinger;
        private TextView mTextViewCreated;
        private TextView mTextViewDuration;
        private List<Track> mTracks;
        private ConstraintLayout mLayout;
        private OnClickListener mListener;

        GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            mImageViewAva = itemView.findViewById(R.id.imageview_avatar);
            mImageViewFavorite = itemView.findViewById(R.id.imageview_favorite_genre);
            mTextViewCreated = itemView.findViewById(R.id.textview_created);
            mTextViewDuration = itemView.findViewById(R.id.textview_time);
            mTextViewName = itemView.findViewById(R.id.textview_name);
            mTextViewSinger = itemView.findViewById(R.id.textview_singer);
            mLayout = itemView.findViewById(R.id.layout_item_genre);
            mLayout.setOnClickListener(this);
            mImageViewFavorite.setOnClickListener(this);
        }

        private void bindData(Context context, List<Track> tracks,
                OnClickListener OnClickListener) {
            mTracks = tracks;
            mTextViewName.setText(tracks.get(getAdapterPosition()).getTitle());
            mTextViewSinger.setText(tracks.get(getAdapterPosition()).getUser().getUsername());
            mTextViewCreated.setText(
                    StringUtils.convertTime(tracks.get(getAdapterPosition()).getCreatedAt()));
            mTextViewDuration.setText(StringUtils.convertMilisecToMinute(
                    tracks.get(getAdapterPosition()).getFullDuration()));
            loadImage(context, tracks.get(getAdapterPosition()));
            loadFavorite(tracks.get(getAdapterPosition()));
            mListener = OnClickListener;
        }

        private void loadFavorite(Track track) {
            if (track.getFavorite() == PLAY_FAVORITE) {
                mImageViewFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                mImageViewFavorite.setImageResource(R.drawable.ic_un_favorite);
            }
        }

        private void loadImage(Context context, Track track) {
            Glide.with(context)
                    .load(track.getArtworkUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_place_holder)
                            .error(R.drawable.ic_load_image_error))
                    .into(mImageViewAva);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                switch (view.getId()) {
                    case R.id.imageview_favorite_genre:
                        mListener.OnFavoriteClick(mTracks, getAdapterPosition());
                        break;

                    default:
                        mListener.OnItemClick(mTracks, getAdapterPosition());
                }
            }
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;

        private LoadingViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
