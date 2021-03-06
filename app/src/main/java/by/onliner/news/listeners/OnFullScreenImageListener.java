package by.onliner.news.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;

import by.onliner.news.activity.FullScreenImageActivity;
import by.onliner.news.App;

/**
 * Обработка откытия полного размера изображения в новом окне
 */
public class OnFullScreenImageListener implements View.OnClickListener {
    public static String INTENT_FULL_SCREEN_URL = "URL";

    private Activity mActivity;
    private ArrayList<String> mImageUrls;

    public OnFullScreenImageListener(Activity activity, ArrayList<String> imageUrls) {
        this.mActivity = activity;
        this.mImageUrls = new ArrayList<>(imageUrls);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(App.getContext(), FullScreenImageActivity.class);
        intent.putStringArrayListExtra(INTENT_FULL_SCREEN_URL, mImageUrls);
        mActivity.startActivity(intent);
    }
}
