package by.onliner.news.Managers;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import by.onliner.news.App;
import by.onliner.news.Listeners.LikeCommentListener;
import by.onliner.news.Listeners.ResponseListener;
import by.onliner.news.Parser.JSON.JSONLikesParser;
import by.onliner.news.Structures.Comments.Like;
import cz.msebera.android.httpclient.Header;

/**
 * Менеджер обработки лайков
 */
public class LikeMgr {
    private static LikeMgr ourInstance = new LikeMgr();

    private LikeMgr() {
    }

    public static LikeMgr getInstance() {
        return ourInstance;
    }

    public void getAsyncLikes(String url, final ResponseListener<ArrayList<Like>> listener) {
        App.getAsyncHttpClient().get(App.getContext(), url, new RequestParams(), new AsyncHttpResponseHandler() {
            ResponseListener<ArrayList<Like>> asyncListener = listener;
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                asyncListener.onResponse(new JSONLikesParser().parse(new String(responseBody)));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                asyncListener.onResponse(null);
            }
        });
    }

    public void asyncLikeComment(String commentId, String project, final LikeCommentListener likeCommentListener) {
        App.getAsyncHttpClient().post(App.getContext(), getLikeUrl(commentId, project), new RequestParams(), new AsyncHttpResponseHandler() {
            LikeCommentListener listener =  likeCommentListener;
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                listener.OnResponse(statusCode, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.OnResponse(statusCode, new String(responseBody));
            }
        });
    }

    public String getLikeUrl(String commentId, String project) {
        return "https://" + project + ".onliner.by/sdapi/news.api/" + project + "/comments/" + commentId + "/like";
    }
}
