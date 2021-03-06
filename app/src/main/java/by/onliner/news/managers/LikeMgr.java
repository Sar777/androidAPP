package by.onliner.news.managers;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

import by.onliner.news.listeners.OnLikeCommentListener;
import by.onliner.news.services.likes.LikeCommentResponse;
import by.onliner.news.services.likes.LikeService;
import by.onliner.news.services.likes.LikesObjectListResponse;
import by.onliner.news.services.ServiceFactory;
import by.onliner.news.structures.comments.Like;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public HashMap<String, Like> getLikes(@NonNull final String project, @NonNull final String postId) {
        LikeService service = ServiceFactory.createRetrofitService(LikeService.class, String.format("https://%s.onliner.by", project));

        HashMap<String, Like> likes = null;
        try {
            Response<LikesObjectListResponse> response = service.getLikes(project, postId).execute();
            if (!response.isSuccessful())
                return null;

            likes = response.body().getLikes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return likes;
    }

    public void likeComment(@NonNull String comment_id, @NonNull String project, final OnLikeCommentListener listener) {
        LikeService service = ServiceFactory.createRetrofitService(LikeService.class, String.format("https://%s.onliner.by", project));

        service.likeComment(project, comment_id).enqueue(new Callback<LikeCommentResponse>() {
            @Override
            public void onResponse(Call<LikeCommentResponse> call, Response<LikeCommentResponse> response) {
                LikeCommentResponse likeCommentResponse = null;
                if (!response.isSuccessful() && response.raw().code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    // Хак
                    String responsJson = null;
                    try {
                        responsJson = response.errorBody().string();
                        if (responsJson.contains("user"))
                            responsJson = new JSONObject(responsJson).get("errors").toString();

                        likeCommentResponse = new Gson().fromJson(responsJson, LikeCommentResponse.class);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    likeCommentResponse = response.body();

                listener.OnResponse(response.raw().code(), likeCommentResponse);
            }

            @Override
            public void onFailure(Call<LikeCommentResponse> call, Throwable t) {
                t.printStackTrace();
                listener.OnResponse(HttpURLConnection.HTTP_BAD_REQUEST, null);
            }
        });
    }

    public void deslikeComment(@NonNull String comment_id, @NonNull String project, final OnLikeCommentListener listener) {
        LikeService service = ServiceFactory.createRetrofitService(LikeService.class, String.format("https://%s.onliner.by", project));

        service.deslikeComment(project, comment_id).enqueue(new Callback<LikeCommentResponse>() {
            @Override
            public void onResponse(Call<LikeCommentResponse> call, Response<LikeCommentResponse> response) {
                LikeCommentResponse likeCommentResponse = null;
                try {
                    if (!response.isSuccessful())
                        likeCommentResponse = new Gson().fromJson(response.errorBody().string(), LikeCommentResponse.class);
                    else
                        likeCommentResponse = response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                listener.OnResponse(response.raw().code(), likeCommentResponse);
            }

            @Override
            public void onFailure(Call<LikeCommentResponse> call, Throwable t) {
                t.printStackTrace();
                listener.OnResponse(HttpURLConnection.HTTP_BAD_REQUEST, null);
            }
        });
    }
}

