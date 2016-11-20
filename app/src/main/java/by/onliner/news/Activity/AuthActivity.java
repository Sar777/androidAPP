package by.onliner.news.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import by.onliner.news.App;
import by.onliner.news.Listeners.Auth.OnLoginCompleteListener;
import by.onliner.news.Listeners.Auth.OnLoginValidateAccount;
import by.onliner.news.Listeners.Credentials.OnCredentialsRefreshListener;
import by.onliner.news.Listeners.User.OnUserUpdateListener;
import by.onliner.news.Managers.AuthMgr;
import by.onliner.news.R;
import by.onliner.news.Structures.Credentials.Credentials;
import by.onliner.news.Structures.User.User;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    // View
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private ViewGroup mAuthGroup;
    private Button mButtonAuth;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_auth);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEditTextUsername = (EditText) findViewById(R.id.et_auth_username);
        mEditTextPassword = (EditText) findViewById(R.id.et_auth_password);

        mButtonAuth = (Button) findViewById(R.id.bt_auth_account);
        mButtonAuth.setOnClickListener(this);

        mAuthGroup = (ViewGroup) findViewById(R.id.l_auth_group);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_auth_progress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_auth_account: {
                authAccount();
                break;
            }
            default:
                break;
        }
    }

    public void authAccount() {
        final String username = mEditTextUsername.getText().toString();
        final String password = mEditTextPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            showSnackbar(R.string.auth_fail_empty_password_username);
            return;
        }

        mAuthGroup.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        AuthMgr.getInstance().validateAccount("", "", new OnLoginValidateAccount() {
            @Override
            public void onValidate(boolean success) {
                if (!success) {
                    mAuthGroup.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    showSnackbar(R.string.auth_fail_password_username);
                    return;
                }

                AuthMgr.getInstance().loginAccount("", "", new OnLoginCompleteListener() {
                    @Override
                    public void onLoginStatus(boolean success) {
                        if (!success) {
                            mAuthGroup.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                            showSnackbar(R.string.auth_fail_unknown);
                            return;
                        }

                        Log.e("ORION", "loginAccount");

                        Credentials.getCredintials("", "", new OnCredentialsRefreshListener() {
                            @Override
                            public void onRefresh(Credentials credentials) {
                                App.setCredentials(credentials);
                                Log.e("ORION", "setCredentials");
                                User.getUser(credentials.getAccessToken(), new OnUserUpdateListener() {
                                    @Override
                                    public void onUpdate(User user) {
                                        App.setLoggedUser(user);
                                        Log.e("ORION", "GetUser");
                                    }
                                });
                            }
                        });

                        onBackPressed();
                    }
                });
            }
        });
    }

    private void showSnackbar(int resId) {
        Snackbar.make(findViewById(R.id.l_auth_base), resId, Snackbar.LENGTH_LONG).show();
    }
}
