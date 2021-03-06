package by.onliner.news.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import by.onliner.news.App;
import by.onliner.news.common.Config;
import by.onliner.news.customViews.QuoteTextView;
import by.onliner.news.enums.ViewNewsType;
import by.onliner.news.listeners.OnFullScreenImageListener;
import by.onliner.news.R;
import by.onliner.news.structures.news.viewsObjects.H2ViewObject;
import by.onliner.news.structures.news.viewsObjects.ImageSliderViewObject;
import by.onliner.news.structures.news.viewsObjects.ImageViewObject;
import by.onliner.news.structures.news.viewsObjects.QuoteViewObject;
import by.onliner.news.structures.news.viewsObjects.SpanViewObject;
import by.onliner.news.structures.news.viewsObjects.SpeechViewObject;
import by.onliner.news.structures.news.viewsObjects.TitleViewObject;
import by.onliner.news.structures.news.viewsObjects.ULViewObject;
import by.onliner.news.structures.news.viewsObjects.ViewObject;
import by.onliner.news.structures.news.viewsObjects.VoteViewObject;
import by.onliner.news.structures.news.viewsObjects.YoutubeViewObject;

import static by.onliner.news.enums.ViewNewsType.values;

/**
 * Адаптер для RecyclerView отвечающий за просмотр содержимого новости
 */
public class NewsContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;
    private ArrayList<ViewObject> mResource;

    public NewsContentAdapter(Activity activity, ArrayList<ViewObject> mResource) {
        this.mActivity = activity;
        this.mResource = mResource;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewNewsType viewNewsType = values()[viewType];
        switch (viewNewsType) {
            case TYPE_VIEW_SPAN:
                return new NewsContentAdapter.SpanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_span, parent, false));
            case TYPE_VIEW_TITLE:
                return new NewsContentAdapter.TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_title, parent, false));
            case TYPE_VIEW_SPEECH:
                return new NewsContentAdapter.SpeechViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_speech, parent, false));
            case TYPE_VIEW_QUOTE:
                return new NewsContentAdapter.QuoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_quote, parent, false));
            case TYPE_VIEW_HR:
                return new NewsContentAdapter.HRViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_hr, parent, false));
            case TYPE_VIEW_H2:
                return new NewsContentAdapter.H2ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_h2, parent, false));
            case TYPE_VIEW_UL:
                return new NewsContentAdapter.ULViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_ul, parent, false));
            case TYPE_VIEW_VOTE:
                return new NewsContentAdapter.VoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_vote, parent, false));
            case TYPE_VIEW_IMAGE:
                return new NewsContentAdapter.ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_image, parent, false));
            case TYPE_VIEW_IMAGE_SLIDER:
                return new NewsContentAdapter.ImageSliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_image_slider, parent, false));
            case TYPE_VIEW_YOUTUBE_PLAYER:
                return new NewsContentAdapter.YoutubeVideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_view_news_youtube_player, parent, false));
            default:
                break;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewNewsType viewNewsType = values()[holder.getItemViewType()];
        switch (viewNewsType) {
            case TYPE_VIEW_SPAN:
                ((SpanViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_TITLE:
                ((TitleViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_SPEECH:
                ((SpeechViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_QUOTE:
                ((QuoteViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_H2:
                ((H2ViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_UL:
                ((ULViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_VOTE:
                ((VoteViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_IMAGE:
                ((ImageViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_IMAGE_SLIDER:
                ((ImageSliderViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_YOUTUBE_PLAYER:
                ((YoutubeVideoViewHolder)holder).bind(holder, position);
                break;
            case TYPE_VIEW_HR:
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mResource.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mResource.get(position).getType().ordinal();
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        TitleViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.tv_content_news_title);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final TitleViewObject titleViewObject = (TitleViewObject) mResource.get(position);
            mTextView.setText(titleViewObject.getText());
        }
    }

    private class SpanViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        SpanViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.tv_content_news_span);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final SpanViewObject spanView = (SpanViewObject)mResource.get(position);
            mTextView.setText(spanView.getText());
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTextViewDescription;
        private ProgressBar mProgressBarRLoading;
        private ViewGroup mLayoutRepeatGroup;
        private Button mButtonLoadingRepeat;

        private String mUrl;

        ImageViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.img_view_news_image);
            mTextViewDescription = (TextView) itemView.findViewById(R.id.tv_view_news_img_description);
            mProgressBarRLoading = (ProgressBar) itemView.findViewById(R.id.pb_view_news_loading);
            mLayoutRepeatGroup = (ViewGroup) itemView.findViewById(R.id.l_view_news_repeat);
            mButtonLoadingRepeat = (Button) itemView.findViewById(R.id.bt_view_news_repeat_loading);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final ImageViewObject imageViewObject = (ImageViewObject) mResource.get(position);

            mUrl = imageViewObject.getUrl();

            loadingImage();

            if (!imageViewObject.getDescription().isEmpty()) {
                mTextViewDescription.setVisibility(View.VISIBLE);
                mTextViewDescription.setText(imageViewObject.getDescription());
            }
            else
                mTextViewDescription.setVisibility(View.GONE);

            mImageView.setOnClickListener(new OnFullScreenImageListener(mActivity, new ArrayList<>(Arrays.asList(mUrl))));
            mButtonLoadingRepeat.setOnClickListener(this);
        }

        private void loadingImage() {
            mLayoutRepeatGroup.setVisibility(View.GONE);
            mProgressBarRLoading.setVisibility(View.VISIBLE);

            Picasso.with(App.getContext()).
                    load(mUrl).
                    resize(0, 300).
                    into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                           mProgressBarRLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            mLayoutRepeatGroup.setVisibility(View.VISIBLE);
                            mProgressBarRLoading.setVisibility(View.GONE);
                        }
                    });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_view_news_repeat_loading:
                    loadingImage();
                    break;
                default:
                    break;
            }
        }
    }

    private class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView mRecyclerViewImages;
        private TextView mTextViewDescription;
        private ViewGroup mLinearLayoutIndicator;

        ImageSliderViewHolder(View itemView) {
            super(itemView);

            mRecyclerViewImages = (RecyclerView) itemView.findViewById(R.id.recycler_view_news_image_slider);
            mTextViewDescription = (TextView) itemView.findViewById(R.id.tv_view_news_img_description);
            mLinearLayoutIndicator = (ViewGroup) itemView.findViewById(R.id.l_view_news_indicator_images);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final ImageSliderViewObject imageSliderViewObject = (ImageSliderViewObject) mResource.get(position);

            if (!imageSliderViewObject.getDescription().isEmpty()) {
                mTextViewDescription.setVisibility(View.VISIBLE);
                mTextViewDescription.setText(imageSliderViewObject.getDescription());
            }
            else
                mTextViewDescription.setVisibility(View.GONE);

            mLinearLayoutIndicator.removeAllViews();
            for (int i = 0; i < imageSliderViewObject.getImageURLs().size(); ++i) {
                ImageView imageView = new ImageView(mActivity);
                imageView.setMinimumWidth(i == 0 ? 35 : 20);
                imageView.setMinimumHeight(i == 0 ? 35 : 20);
                imageView.setImageResource(R.drawable.i_slider_circle_indicator);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 25, 0);
                imageView.setLayoutParams(layoutParams);

                mLinearLayoutIndicator.addView(imageView);
            }

            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerViewImages.setLayoutManager(horizontalLayoutManager);

            mRecyclerViewImages.setOnClickListener(new OnFullScreenImageListener(mActivity, new ArrayList<>(imageSliderViewObject.getImageURLs())));

            mRecyclerViewImages.setAdapter(new HorizontalRecyclerImageSliderAdapter(mActivity, imageSliderViewObject.getImageURLs()));
            mRecyclerViewImages.addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager)mRecyclerViewImages.getLayoutManager();

                    for (int i = 0; i < mLinearLayoutIndicator.getChildCount(); ++i)
                    {
                        ImageView imageView = (ImageView)mLinearLayoutIndicator.getChildAt(i);
                        if (i == linearLayoutManager.findFirstVisibleItemPosition()) {
                            imageView.setMinimumWidth(35);
                            imageView.setMinimumHeight(35);
                        }
                        else  {
                            imageView.setMinimumWidth(20);
                            imageView.setMinimumHeight(20);
                        }
                    }
                }
            });
        }
    }

    private class SpeechViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        SpeechViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.tv_content_news_speech);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final SpeechViewObject speechViewObject = (SpeechViewObject)mResource.get(position);
            mTextView.setText(speechViewObject.getText());
        }
    }

    private class QuoteViewHolder extends RecyclerView.ViewHolder {
        private QuoteTextView mQuoteTextView;

        QuoteViewHolder(View itemView) {
            super(itemView);

            mQuoteTextView = (QuoteTextView) itemView.findViewById(R.id.tv_content_news_quote);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final QuoteViewObject quoteViewObject = (QuoteViewObject) mResource.get(position);
            mQuoteTextView.setText(quoteViewObject.getText());
        }
    }

    private class H2ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        H2ViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.tv_content_news_h2);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final H2ViewObject h2ViewObject = (H2ViewObject) mResource.get(position);
            mTextView.setText(h2ViewObject.getText());
        }
    }

    private class ULViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        ULViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.tv_content_news_ul);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final ULViewObject ulViewObject = (ULViewObject) mResource.get(position);
            mTextView.setText(ulViewObject.getText());
        }
    }

    private class VoteViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewState;
        private ViewGroup mLayoutOptions;

        VoteViewHolder(View itemView) {
            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.tv_view_news_vote_title);
            mTextViewState = (TextView) itemView.findViewById(R.id.tv_view_news_vote_stat);
            mLayoutOptions = (ViewGroup) itemView.findViewById(R.id.l_view_news_vote_options);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final VoteViewObject voteViewObject = (VoteViewObject) mResource.get(position);

            mTextViewTitle.setText(voteViewObject.getTitle());
            mTextViewState.setText(voteViewObject.getState());
            mLayoutOptions.removeAllViews();

            for (String option : voteViewObject.getOptions()) {

                RadioButton radioButton = new RadioButton(mLayoutOptions.getContext());
                radioButton.setTextColor(Color.BLACK);
                radioButton.setTextSize(13);
                radioButton.setText(option);
                mLayoutOptions.addView(radioButton);
            }
        }
    }

    private class YoutubeVideoViewHolder extends RecyclerView.ViewHolder implements YouTubeThumbnailView.OnInitializedListener {
        private YouTubeThumbnailView mYouTubeThumbnailView;
        private ImageView mImageViewPlay;

        private String mYoutubeVideoId;

        YoutubeVideoViewHolder(View itemView) {
            super(itemView);

            mYouTubeThumbnailView = (YouTubeThumbnailView)itemView.findViewById(R.id.youtube_view_news_thumbnail);
            mImageViewPlay = (ImageView)itemView.findViewById(R.id.bt_view_news_youtube_player);
        }

        void bind(final RecyclerView.ViewHolder holder, final int position) {
            final YoutubeViewObject youtubeViewObject = (YoutubeViewObject) mResource.get(position);

            mYoutubeVideoId = youtubeViewObject.getVideoId();
            mYouTubeThumbnailView.initialize(Config.DEVELOPER_YOUTUBE_KEY, this);
        }

        @Override
        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
            youTubeThumbnailLoader.setVideo(mYoutubeVideoId);
            youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    youTubeThumbnailView.setVisibility(View.VISIBLE);

                    mImageViewPlay.setVisibility(View.VISIBLE);
                    mImageViewPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = YouTubeStandalonePlayer.createVideoIntent(mActivity, Config.DEVELOPER_YOUTUBE_KEY, mYoutubeVideoId);
                            mActivity.startActivity(intent);
                        }
                    });
                }

                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) { }
            });
        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {}
    }

    private class HRViewHolder extends RecyclerView.ViewHolder {
        HRViewHolder(View itemView) {
            super(itemView);
        }
    }

    public ArrayList<ViewObject> getResource() {
        return mResource;
    }
}
