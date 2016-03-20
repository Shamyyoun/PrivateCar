package com.privatecar.privatecar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import com.privatecar.privatecar.adapters.CountryAdapter;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.CountriesUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class CustomerSignupActivity extends BasicBackActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, RequestListener<GeneralResponse> {

    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private ProgressDialog progressDialog;

    private Button btnSignUpFacebook, btnSignUpGooglePlus;
    private EditText etFirstName, etLastName, etEmail, etPassword, etMobile;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signup);

        prepareLoginWithFacebook();
        prepareSigninWithGoogle();

        btnSignUpFacebook = (Button) findViewById(R.id.btn_sign_up_facebook);
        btnSignUpGooglePlus = (Button) findViewById(R.id.btn_sign_up_google_plus);

        btnSignUpFacebook.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.sign_up_facebook));
        btnSignUpGooglePlus.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.sign_up_google_plus));

        spinner = (Spinner) findViewById(R.id.spinner_countries);
        CountryAdapter adapter = new CountryAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(Const.EGYPT_INDEX); //set egypt the default

        etFirstName = (EditText) findViewById(R.id.et_first_name);
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isEmpty(s)) {
                    etFirstName.setError(getString(R.string.required));
                } else {
                    etFirstName.setError(null);
                }
            }
        });

        etLastName = (EditText) findViewById(R.id.et_last_name);
        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isEmpty(s)) {
                    etLastName.setError(getString(R.string.required));
                } else {
                    etLastName.setError(null);
                }
            }
        });


        etEmail = (EditText) findViewById(R.id.et_email);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Utils.isValidEmail(s.toString())) {
                    etEmail.setError(getString(R.string.not_valid_email));
                } else {
                    etEmail.setError(null);
                }
            }
        });


        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < Const.MIN_PASSWORD_LENGTH) {
                    etPassword.setError(getString(R.string.short_password));
                } else {
                    etPassword.setError(null);
                }
            }
        });


        etMobile = (EditText) findViewById(R.id.et_mobile);
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (spinner.getSelectedItemPosition() == Const.EGYPT_INDEX) {
                    if (!Utils.isValidEgyptianMobileNumber("0" + s.toString())) {
                        etMobile.setError(getString(R.string.not_valid_mobile));
                    } else {
                        etMobile.setError(null);
                    }
                }
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
        if (requestCode == Const.REQUEST_GOOGLE_PLUS_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up_facebook:
                logInFacebook();
                break;
            case R.id.btn_sign_up_google_plus:
                signInGoogle();
                break;
            case R.id.btn_sign_up:
                customerSignup();
                break;
        }
    }

    private boolean customerValidation() {
        boolean valid = true;

        if (Utils.isEmpty(etFirstName)) {
            etFirstName.setError(getString(R.string.required));
            valid = false;
        } else {
            etFirstName.setError(null);
        }

        if (Utils.isEmpty(etLastName)) {
            etLastName.setError(getString(R.string.required));
            valid = false;
        } else {
            etLastName.setError(null);
        }

        if (!Utils.isValidEmail(Utils.getText(etEmail))) {
            etEmail.setError(getString(R.string.not_valid_email));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (Utils.isEmpty(etPassword)) {
            etPassword.setError(getString(R.string.required));
            valid = false;
        } else if (Utils.getText(etPassword).length() < Const.MIN_PASSWORD_LENGTH) {
            etPassword.setError(getString(R.string.short_password));
            valid = false;
        } else {
            etPassword.setError(null);
        }

        if (spinner.getSelectedItemPosition() == Const.EGYPT_INDEX) {
            if (!Utils.isValidEgyptianMobileNumber("0" + Utils.getText(etMobile))) {
                etMobile.setError(getString(R.string.not_valid_mobile));
                valid = false;
            } else {
                etMobile.setError(null);
            }
        }

        return valid;
    }

    private void customerSignup() {
        if (!customerValidation()) return;

        String code = new CountriesUtils().getCountryCodes()[spinner.getSelectedItemPosition()];
        showProgressDialog(R.string.registering);
        Utils.hideKeyboard(etMobile);
        CustomerRequests.regCustomerEmailPassword(this, this, Utils.getText(etFirstName), Utils.getText(etLastName), Utils.getText(etEmail), Utils.getText(etPassword), code + Utils.getText(etMobile));

    }

    // =============================== Facebook ==================================

    private void prepareLoginWithFacebook() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult result) {
                        showProgressDialog(R.string.loading);

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


                                            Intent intent = new Intent(CustomerSignupActivity.this, CustomerSocialSignupActivity.class);
                                            intent.putExtra(Const.KEY_FIRST_NAME, firstName);
                                            intent.putExtra(Const.KEY_LAST_NAME, lastName);
                                            intent.putExtra(Const.KEY_EMAIL, email);
                                            intent.putExtra(Const.KEY_ID, id);
                                            intent.putExtra(Const.KEY_TOKEN, token);
                                            intent.putExtra(Const.KEY_PROVIDER, "facebook");
                                            startActivity(intent);

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
                        Utils.LogE("Login cancelled");
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

    private void signInGoogle() {
        showProgressDialog(R.string.loading);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, Const.REQUEST_GOOGLE_PLUS_SIGN_IN);
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
        if (result.isSuccess() && result.getSignInAccount() != null) { //signed in
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e("Google+", "Id: " + acct.getId());
            Log.e("Google+", "DisplayName: " + acct.getDisplayName());
            Log.e("Google+", "Email: " + acct.getEmail());
            Log.e("Google+", "ServerAuthCode: " + acct.getServerAuthCode()); //used to get access token
            Log.e("Google+", "AccessToken: " + acct.getIdToken());

            Intent intent = new Intent(CustomerSignupActivity.this, CustomerSocialSignupActivity.class);
            if (acct.getDisplayName() != null) {
                String[] namesArr = acct.getDisplayName().split(" ");
                String firstName = namesArr.length > 0 ? namesArr[0] : "";
                String lastName = namesArr.length > 1 ? namesArr[1] : "";
                intent.putExtra(Const.KEY_FIRST_NAME, firstName);
                intent.putExtra(Const.KEY_LAST_NAME, lastName);
                intent.putExtra(Const.KEY_EMAIL, acct.getEmail());
                intent.putExtra(Const.KEY_ID, acct.getId());
                intent.putExtra(Const.KEY_TOKEN, acct.getIdToken());
                intent.putExtra(Const.KEY_PROVIDER, "google");
                startActivity(intent);
            }


        } else

        { //not signed in
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

    private void showProgressDialog(int messageResId) {
        progressDialog = DialogUtils.showProgressDialog(this, messageResId, true);
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        hideProgressDialog();

        if (response.isSuccess()) {
            // open user verification activity
            Intent intent = new Intent(this, UserVerificationActivity.class);
            intent.putExtra(Const.KEY_EMAIL, Utils.getText(etEmail));
            intent.putExtra(Const.KEY_PASSWORD, Utils.getText(etPassword));
            startActivity(intent);
            this.onBackPressed();
        } else {
            if (response.getValidation() != null) {
                String validation = "";
                for (int i = 0; i < response.getValidation().size(); i++) {
                    if (i == 0)
                        validation += response.getValidation().get(i);
                    else
                        validation += "\n" + response.getValidation().get(i);
                }

                DialogUtils.showAlertDialog(this, validation, null);
            }
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        hideProgressDialog();
        Utils.showLongToast(this, message);
    }
}