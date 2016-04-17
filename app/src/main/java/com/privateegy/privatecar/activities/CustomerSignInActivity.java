package com.privateegy.privatecar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.enums.SocialProvider;
import com.privateegy.privatecar.models.enums.UserType;
import com.privateegy.privatecar.models.responses.AccessTokenResponse;
import com.privateegy.privatecar.requests.CommonRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

import java.util.Arrays;

public class CustomerSignInActivity extends BasicBackActivity implements RequestListener<Object>,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, FacebookCallback<LoginResult> {
    private static final int LOGIN_NORMAL = 1;
    private static final int LOGIN_FACEBOOK = 2;
    private static final int LOGIN_GPLUS = 3;
    private static final int REQUEST_CODE_GOOGLE_PLUS_SIGN_IN = 9001;

    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private ProgressDialog progressDialog;

    private EditText etEmail, etPassword;
    private Button btnSignInFacebook, btnSignInGooglePlus, btnSignIn;
    private int loginType;
    private String socialUserId, socialToken, socialProvider; // used to hold social parameters to save them after successful login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signin);

        prepareLoginWithFacebook();
        prepareLoginWithGoogle();

        // init views
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnSignInFacebook = (Button) findViewById(R.id.btn_sign_in_facebook);
        btnSignInGooglePlus = (Button) findViewById(R.id.btn_sign_in_google_plus);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

        // add touch listeners
        btnSignInFacebook.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.sign_in_facebook));
        btnSignInGooglePlus.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.sign_in_google_plus));

        // add done click listener to password edit text
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Facebook login
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Google+ login
        if (requestCode == REQUEST_CODE_GOOGLE_PLUS_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in_facebook:
                loginWithFacebook();
                break;

            case R.id.btn_sign_in_google_plus:
                loginWithGoogle();
                break;

            case R.id.btn_sign_in:
                login();
                break;

            case R.id.btn_forgot_password:
                // open forget password activity
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    /**
     * method, used to send login request to the server and handle it
     */
    private void login() {
        // get inputs
        String username = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // validate inputs
        if (username.isEmpty()) {
            etEmail.setError(getString(R.string.required));
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError(getString(R.string.required));
            return;
        }

        // hide keyboard
        Utils.hideKeyboard(etEmail);

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.loading_please_wait);

        // all things is alright
        // send sign in request
        loginType = LOGIN_NORMAL;
        CommonRequests.normalLogin(this, this, username, password);
    }

    /**
     * method, used to send social login request to the server
     *
     * @param id       id of the user on the social provider
     * @param token    access token of the user to the provider
     * @param provider provider type, facebook or google plus
     */
    private void socialLogin(String id, String token, SocialProvider provider) {
        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.please_wait);

        // hold social parameters
        socialUserId = id;
        socialToken = token;
        socialProvider = provider.getValue();

        // create & send the request
        loginType = provider == SocialProvider.FACEBOOK ? LOGIN_FACEBOOK : LOGIN_GPLUS;
        CommonRequests.socialLogin(this, this, id, token, provider.getValue());
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // cast the response
        AccessTokenResponse accessTokenResponse = (AccessTokenResponse) response;

        // validate response
        if (accessTokenResponse != null) {
            // check response
            if (!Utils.isNullOrEmpty(accessTokenResponse.getAccessToken())) {
                // success
                // cache response
                User user = new User();
                user.setType(UserType.CUSTOMER);
                user.setAccessToken(accessTokenResponse.getAccessToken());
                int expiryIn = accessTokenResponse.getExpiresIn() * 1000; //in melli seconds
                user.setExpiryTimestamp(System.currentTimeMillis() + expiryIn);

                // check login type
                if (loginType == LOGIN_NORMAL) {
                    // normal login, save username & password
                    user.setUserName(Utils.getText(etEmail));
                    user.setPassword(Utils.getText(etPassword));
                } else {
                    // social login, save social parameters
                    user.setSocialUserId(socialUserId);
                    user.setSocialToken(socialToken);
                    user.setSocialProvider(socialProvider);
                }

                AppUtils.cacheUser(this, user);

                // goto home activity
                Intent intent = new Intent(this, CustomerHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // failed
                // prepare error msg
                String errorMsg = "";
                boolean wentToVerificationScreen = false;
                if (accessTokenResponse.getValidation().size() == 0) {
                    errorMsg = getString(R.string.invalid_credentials);
                } else {
                    for (int i = 0; i < accessTokenResponse.getValidation().size(); i++) {
                        String validationItem = accessTokenResponse.getValidation().get(i);

                        // check this validation item
                        if (validationItem.toLowerCase().contains("not verif")) {
                            // the user credentials is ok but he is not verified
                            // open user verification activity
                            Intent intent = new Intent(this, UserVerificationActivity.class);
                            intent.putExtra(Const.KEY_EMAIL, Utils.getText(etEmail));
                            intent.putExtra(Const.KEY_PASSWORD, Utils.getText(etPassword));
                            startActivity(intent);

                            // finish this activity
                            wentToVerificationScreen = true;
                            onBackPressed();
                            break;
                        } else {
                            // append to the error msg
                            if (i != 0) {
                                errorMsg += "\n";
                            }
                            errorMsg += accessTokenResponse.getValidation().get(i);
                        }
                    }
                }

                // check if went to the verification screen
                if (!wentToVerificationScreen) {
                    // show the error msg
                    Utils.showLongToast(this, errorMsg);
                }
            }
        } else {
            // show error msg
            Utils.showLongToast(this, R.string.unexpected_error_try_again);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // end facebook session
        signOutFacebook();

        // end google+ session
        signOutGoogle();

        // show error
        Utils.showLongToast(this, message);
        Log.e(Const.LOG_TAG, message);
    }

    // =============================== Facebook ==================================
    private void prepareLoginWithFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    private void loginWithFacebook() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            // show error toast
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // login with facebook
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    private void signOutFacebook() {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        socialLogin(loginResult.getAccessToken().getUserId(), loginResult.getAccessToken().getToken(), SocialProvider.FACEBOOK);
    }

    @Override
    public void onCancel() {
        Log.e("Facebook", "Cancelled");
    }

    @Override
    public void onError(FacebookException error) {
        if (error instanceof FacebookAuthorizationException) {
            if (AccessToken.getCurrentAccessToken() != null) { //handle logging with another account
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginWithFacebook();
                    }
                }, 1000);
            }
        } else {
            Utils.showShortToast(this, error.toString());
        }

        // end facebook session
        signOutFacebook();
    }

    // =============================== Google+ ==================================
    private void prepareLoginWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestServerAuthCode(getString(R.string.client_id))
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void loginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_PLUS_SIGN_IN);
    }

    private void signOutGoogle() {
        if (googleApiClient.isConnected()) {
            Log.e("Google+", "Logging out");
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.e("Google+", "logout: " + status.toString());
                        }
                    });
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.e("Google+", "handleGoogleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //signed in
            // send social login request to the server
            GoogleSignInAccount acct = result.getSignInAccount();
            socialLogin(acct.getId(), acct.getIdToken(), SocialProvider.GPLUS);

        } else {
            // not signed in
            Log.e("Google+", "Not signed in");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // end google plus session
        signOutGoogle();
        Log.e("Google+", connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("Google+", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        // end google plus session
        signOutGoogle();
        Log.e("Google+", "onConnectionSuspended: " + i);
    }
}
