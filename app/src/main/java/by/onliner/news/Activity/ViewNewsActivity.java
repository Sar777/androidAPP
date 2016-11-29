package by.onliner.news.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import by.onliner.news.Adapters.NewsContentAdapter;
import by.onliner.news.App;
import by.onliner.news.Fragments.Tabs.TabBase;
import by.onliner.news.Loaders.AsyncNewsCommentLoader;
import by.onliner.news.Loaders.AsyncNewsContentLoader;
import by.onliner.news.Managers.FavoritesNewsMgr;
import by.onliner.news.R;
import by.onliner.news.Structures.Comments.Comment;
import by.onliner.news.Structures.News.News;
import by.onliner.news.Structures.News.ViewsObjects.ViewObject;

/**
 * Просмотр отдельной новости
 */
public class ViewNewsActivity extends AppCompatActivity implements View.OnClickListener, LoaderCallbacks<ArrayList<ViewObject>> {

    public static String INTENT_URL_TAG = "URL";
    public static String INTENT_COMMENTS_TAG = "COMMENTS";
    public static String INTENT_PROJECT_TAG = "PROJECT";
    public static String INTENT_NEWS_ID_TAG = "NEWS_ID";

    private static final int LOADER_CONTENT_ID = 1;
    private static final int LOADER_COMMENTS_ID = 2;

    @NonNull
    private News mNews;

    // Adapters
    private NewsContentAdapter mNewsContentAdapter;

    // Views
    private TextView mTitle;
    private Button mButtonComment;
    private Button mButtonRepeat;
    private ProgressBar mProgressBar;
    private ViewGroup mBaseLayout;
    private ViewGroup mRepeatGroup;
    private RecyclerView mRecyclerContent;

    // Menu
    private MenuItem mItemFavorite;
    private MenuItem mItemRemoveFavorite;

    private LinkedHashMap<String, Comment> mComments;

    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_news_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mComments = new LinkedHashMap<>();

        // Views
        mBaseLayout = (ViewGroup)findViewById(R.id.l_view_news_content);
        mBaseLayout.setVisibility(View.GONE);

        mProgressBar = (ProgressBar)findViewById(R.id.pb_news_list_loading);
        mProgressBar.setVisibility(View.VISIBLE);

        mRepeatGroup = (ViewGroup)findViewById(R.id.l_view_news_repeat);

        mButtonComment = (Button)findViewById(R.id.bt_view_news_comments);
        mButtonComment.setEnabled(false);
        mButtonComment.setOnClickListener(this);

        mButtonRepeat = (Button)findViewById(R.id.btn_load_repeat);
        mButtonRepeat.setOnClickListener(this);

        mRecyclerContent = (RecyclerView)findViewById(R.id.recycler_news_content);
        LinearLayoutManager verticalLinearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerContent.setLayoutManager(verticalLinearLayout);

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        Intent intent = getIntent();
        String url = intent.getStringExtra(TabBase.INTENT_URL_TAG);
        String project = intent.getStringExtra(TabBase.INTENT_PROJECT_TAG);
        String title = intent.getStringExtra(TabBase.INTENT_TITLE_TAG);

        mTitle = (TextView) findViewById(R.id.tv_view_news_title);
        mTitle.setText(title);

        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("PROJECT", project);
        bundle.putString("TITLE", title);
        getLoaderManager().initLoader(LOADER_CONTENT_ID, bundle, this).forceLoad();
    }

    @Override
    public Loader<ArrayList<ViewObject>> onCreateLoader(int id, Bundle args) {
        Loader<ArrayList<ViewObject>> loader = null;
        if (id == LOADER_CONTENT_ID) {
            mRepeatGroup.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

            loader = new AsyncNewsContentLoader(this, args);
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ViewObject>> loader, ArrayList<ViewObject> views) {
        if (loader.getId() == LOADER_CONTENT_ID) {
            if (views == null) {
                mRepeatGroup.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                return;
            }

            mNewsContentAdapter = new NewsContentAdapter(this, views);
            mRecyclerContent.setAdapter(mNewsContentAdapter);

            // Запуск обработки комментариев
            mNews = ((AsyncNewsContentLoader)loader).getNews();
            loadingComments(mNews);

            // Избранная новость
            if (FavoritesNewsMgr.getInstance().isFavorite(mNews.getAttributes().getId())) {
                mItemFavorite.setVisible(false);
                mItemRemoveFavorite.setVisible(true);
            }
            else
                mItemFavorite.setVisible(true);

            // Показ главного окна
            mBaseLayout.setAlpha(0f);
            mBaseLayout.setVisibility(View.VISIBLE);

            mBaseLayout.animate().alpha(1f).setDuration(mShortAnimationDuration).setListener(null);

            mProgressBar.animate().alpha(0f).setDuration(mShortAnimationDuration).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(View.GONE);
                };
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ViewObject>> loader) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_view_news_comments: {
                Intent intent = new Intent(App.getContext(), CommentsActivity.class);
                intent.putExtra(INTENT_URL_TAG, mNews.getAttributes().getUrl());
                intent.putExtra(INTENT_PROJECT_TAG,  mNews.getAttributes().getProject());
                intent.putExtra(INTENT_NEWS_ID_TAG,  mNews.getAttributes().getId());
                intent.putExtra(INTENT_COMMENTS_TAG, new ArrayList<>(mComments.values()));
                startActivity(intent);
                break;
            }
            case R.id.btn_load_repeat: {
                Bundle bundle = new Bundle();
                bundle.putString("URL", mNews.getAttributes().getUrl());
                getLoaderManager().restartLoader(LOADER_CONTENT_ID, bundle, this).forceLoad();
                break;
            }
            default:
                break;
        }
    }

    private void loadingComments(News news) {
        Bundle bundle = new Bundle();
        bundle.putString("Html", news.getContent());
        bundle.putString("Project", news.getAttributes().getProject());
        bundle.putString("PostId", news.getAttributes().getId().toString());
        getLoaderManager().restartLoader(LOADER_COMMENTS_ID, bundle, new LoaderCallbacks<LinkedHashMap<String, Comment>>() {
            @Override
            public Loader<LinkedHashMap<String, Comment>> onCreateLoader(int i, Bundle bundle) {
                return new AsyncNewsCommentLoader(App.getContext(), bundle);
            }

            @Override
            public void onLoadFinished(Loader<LinkedHashMap<String, Comment>> loader, LinkedHashMap<String, Comment> result) {
                mComments = result;
                mButtonComment.animate().translationY(-mButtonComment.getHeight()).setDuration(500).setInterpolator(new LinearInterpolator()).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mButtonComment.setEnabled(true);
                    }
                }).start();
            }

            @Override
            public void onLoaderReset(Loader<LinkedHashMap<String, Comment>> loader) { }
        }).forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_news_menu, menu);

        mItemFavorite = menu.findItem(R.id.action_favorites);
        mItemRemoveFavorite = menu.findItem(R.id.action_remove_favorites);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                FavoritesNewsMgr.getInstance().saveFavorite(mNews);
                mItemFavorite.setVisible(false);
                mItemRemoveFavorite.setVisible(true);
                Toast.makeText(this, R.string.add_favorite_news, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_remove_favorites:
                FavoritesNewsMgr.getInstance().deleteFavorite(mNews);
                mItemFavorite.setVisible(true);
                mItemRemoveFavorite.setVisible(false);
                Toast.makeText(this, R.string.remove_favorite_news, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }
}
