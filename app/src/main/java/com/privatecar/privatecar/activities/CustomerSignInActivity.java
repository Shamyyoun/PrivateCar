package com.privatecar.privatecar.activities;

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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
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
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class CustomerSignInActivity extends BasicBackActivity implements RequestListener<AccessTokenResponse>,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        prepareLoginWithFacebook();
        prepareSigninWithGoogle();

        // init views
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnSignInFacebook = (Button) findViewById(R.id.btn_sign_in_facebook);
        btnSignInGooglePlus = (Button) findViewById(R.id.btn_sign_in_google_plus);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

        // add touch listeners
        btnSignInFacebook.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.sign_in_facebook));
        btnSignInGooglePlus.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.sign_in_google_plus));
        btnSignIn.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.petroleum_rounded_corners_shape));

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

//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d("Google+", "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleGoogleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleGoogleSignInResult(googleSignInResult);
//                }
//            });
//        }


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

        //Facebook signIn
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //Google+ signIn
        if (requestCode == REQUEST_CODE_GOOGLE_PLUS_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in_facebook:
                logInFacebook();
                break;
            case R.id.btn_sign_in_google_plus:
                loginGoogle();
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

    @Override
    public void onSuccess(AccessTokenResponse response, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // validate response
        if (response != null) {
            // check response
            if (response.getAccessToken() != null && !response.getAccessToken().isEmpty()) {
                // success
                // cache response
                User user = new User();
                user.setType(UserType.CUSTOMER);
                user.setUserName(Utils.getText(etEmail));
                user.setPassword(Utils.getText(etPassword));
                user.setAccessToken(response.getAccessToken());
                int expiryIn = response.getExpiresIn() * 1000; //in melli seconds
                user.setExpiryTimestamp(System.currentTimeMillis() + expiryIn);
                AppUtils.cacheUser(this, user);

                // goto home activity
                Intent intent = new Intent(this, CustomerHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // failed
                // prepare error msg
                String errorMsg = "";
                if (response.getValidation().size() == 0) {
                    errorMsg = getString(R.string.invalid_credentials);
                } else {
                    for (int i = 0; i < response.getValidation().size(); i++) {
                        if (i != 0) {
                            errorMsg += "\n";
                        }
                        errorMsg += response.getValidation().get(i);
                    }
                }

                // show the error msg
                Utils.showLongToast(this, errorMsg);
            }
        } else {
            // show error dialog
            Utils.showLongToast(this, R.string.unexpected_error_try_again);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // show error
        Utils.showLongToast(this, message);
        Log.e(Const.LOG_TAG, message);
    }

    // =============================== Facebook ==================================

    private void prepareLoginWithFacebook() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult result) {
                        showProgressDialog();

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name,last_name,email");
                        new GraphRequest(
                                result.getAccessToken(), "/me",
                                parameters, HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        hideProgressDialog();

                                        Log.e(Const.LOG_TAG, response.getJSONObject().toString());

                                        JSONObject jsonResponse = response.getJSONObject();
                                        try {
                                            String id = jsonResponse.getString("id");
                                            String firstName = jsonResponse.getString("first_name");
                                            String lastName = jsonResponse.getString("last_name");
                                            String name = jsonResponse.getString("name");
                                            String email = jsonResponse.getString("email");
                                            String token = result.getAccessToken().getToken();

                                            Log.e(Const.LOG_TAG, id);
                                            Log.e(Const.LOG_TAG, firstName);
                                            Log.e(Const.LOG_TAG, lastName);
                                            Log.e(Const.LOG_TAG, name);
                                            Log.e(Const.LOG_TAG, email);
                                            Log.e(Const.LOG_TAG, token);

                                            //TODO: fill in fields with retrieved data

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).executeAsync();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        hideProgressDialog();

                        if (error instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) { //handle logging with another account
                                LoginManager.getInstance().logOut();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        logInFacebook();
                                    }
                                }, 2000);
                            }
                        } else {
                            Utils.showLongToast(getApplicationContext(), error.toString());
                        }
                    }

                    @Override
                    public void onCancel() {
                        hideProgressDialog();

                        Utils.showLongToast(getApplicationContext(), "Login cancelled");
                    }
                });
    }

    private void logInFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }


    // =============================== Google+ ==================================

    private void prepareSigninWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void loginGoogle() {
        showProgressDialog();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_PLUS_SIGN_IN);
    }

    private void signOut() {
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

    private void revokeAccess() {
        if (googleApiClient.isConnected()) {
            Log.e("Google+", "Revoke");
            Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.e("Google+", "revoke: " + status.toString());
                        }
                    });
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        hideProgressDialog();

        Log.e("Google+", "handleGoogleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) { //signed in
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("Google+", "Id: " + acct.getId());
            Log.e("Google+", "DisplayName: " + acct.getDisplayName());
            Log.e("Google+", "Email: " + acct.getEmail());
            Log.e("Google+", "ServerAuthCode: " + acct.getServerAuthCode()); //used to get access token
            Log.e("Google+", "AccessToken: " + acct.getIdToken());

            //TODO: fill in fields with retrieved data

        } else { //not signed in
            // Signed out, show unauthenticated UI.
            Log.e("Google+", "Not signed in");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Google+", connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("Google+", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("Google+", "onConnectionSuspended: " + i);
    }

    // =====================================================================================

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
}
