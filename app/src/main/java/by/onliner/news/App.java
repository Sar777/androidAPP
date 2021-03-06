package by.onliner.news;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.net.InetAddress;

import by.onliner.news.database.DBHelper;
import by.onliner.news.structures.credentials.Credentials;
import by.onliner.news.structures.user.User;

public class App extends Application {
    private static Context mContext;

    // Cookie
    private static ClearableCookieJar mClearableCookieJar;

    private static Credentials mCredentials;
    private static User mLoggedUser;

    private static DBHelper mDBHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        mClearableCookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));

        mDBHelper = new DBHelper(this);

        mCredentials = Credentials.create();
        mLoggedUser = User.create();
    }

    @NonNull
    public static Context getContext(){
        return mContext;
    }

    @NonNull
    public static ClearableCookieJar getClearableCookieJar() {
        return mClearableCookieJar;
    }

    @NonNull
    public static DBHelper getDBHelper() {
        return mDBHelper;
    }

    public static Credentials getCredentials() {
        return mCredentials;
    }

    public static void setCredentials(Credentials credentials) {
        credentials.saveToDB();
        mCredentials = credentials;
    }

    public static User getLoggedUser() {
        return mLoggedUser;
    }

    public static void setLoggedUser(User user) {
        user.saveToDB();
        mLoggedUser = user;
    }

    public static void logoutUser() {
        if (mLoggedUser != null)
            mLoggedUser.delete();

        if (mCredentials != null)
            mCredentials.delete();

        mLoggedUser = null;
        mCredentials = null;

        mClearableCookieJar.clear();
    }

    public static boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress inetAddress = InetAddress.getByName("google.com");
            return !inetAddress.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}