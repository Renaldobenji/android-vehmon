package za.co.vehmon.application.authenticator;

import static android.R.layout.simple_dropdown_item_1line;
import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static za.co.vehmon.application.core.Constants.Auth.AUTHTOKEN_TYPE;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import butterknife.ButterKnife;
import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.R.id;
import za.co.vehmon.application.R.layout;
import za.co.vehmon.application.R.string;
import za.co.vehmon.application.core.Constants;
import za.co.vehmon.application.core.User;
import za.co.vehmon.application.core.VehmonService;
import za.co.vehmon.application.events.UnAuthorizedErrorEvent;
import za.co.vehmon.application.ui.CarouselFragment;
import za.co.vehmon.application.ui.MainActivity;
import za.co.vehmon.application.ui.TextWatcherAdapter;
import za.co.vehmon.application.util.Ln;
import za.co.vehmon.application.util.SafeAsyncTask;
import com.github.kevinsawicki.wishlist.Toaster;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

import retrofit.RetrofitError;

/**
 * Activity to authenticate the user against an API (example API on Parse.com)
 */
public class BootstrapAuthenticatorActivity extends ActionBarActivity {

    /**
     * PARAM_CONFIRM_CREDENTIALS
     */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

    /**
     * PARAM_PASSWORD
     */
    public static final String PARAM_PASSWORD = "password";

    /**
     * PARAM_USERNAME
     */
    public static final String PARAM_USERNAME = "username";

    /**
     * PARAM_AUTHTOKEN_TYPE
     */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    private static final String url = "http://vehmonmachine.cloudapp.net/";
    private static final String apkName = "Vehmon.apk";
    public ProgressDialog pd;


    private AccountManager accountManager;

    @Inject VehmonService vehmonService;
    @Inject Bus bus;

    @InjectView(id.et_email) protected AutoCompleteTextView emailText;
    @InjectView(id.et_password) protected EditText passwordText;
    @InjectView(id.b_signin) protected Button signInButton;

    private final TextWatcher watcher = validationTextWatcher();

    private SafeAsyncTask<Boolean> authenticationTask;
    private String authToken;
    private String authTokenType;

    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password to be changed on the device.
     */
    private Boolean confirmCredentials = false;

    private String email;

    private String password;


    /**
     * In this instance the token is simply the sessionId returned from Parse.com. This could be a
     * oauth token or some other type of timed token that expires/etc. We're just using the parse.com
     * sessionId to prove the example of how to utilize a token.
     */
    private String token;

    /**
     * Was the original caller asking for an entirely new account?
     */
    protected boolean requestNewAccount = false;

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        setContentView(layout.login_activity);

        Injector.inject(this);
        ButterKnife.inject(this);

        passwordText.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
                if (event != null && ACTION_DOWN == event.getAction()
                        && keyCode == KEYCODE_ENTER && signInButton.isEnabled()) {
                    handleLogin(signInButton);
                    return true;
                }
                return false;
            }
        });

        passwordText.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(final TextView v, final int actionId,
                                          final KeyEvent event) {
                if (actionId == IME_ACTION_DONE && signInButton.isEnabled()) {
                    handleLogin(signInButton);
                    return true;
                }
                return false;
            }
        });

        emailText.addTextChangedListener(watcher);
        passwordText.addTextChangedListener(watcher);
    }

    private TextWatcher validationTextWatcher() {
        return new TextWatcherAdapter() {
            public void afterTextChanged(final Editable gitDirEditText) {
                updateUIWithValidation();
            }

        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
        updateUIWithValidation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    private void updateUIWithValidation() {
        final boolean populated = populated(emailText) && populated(passwordText);
        signInButton.setEnabled(populated);
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(string.message_signing_in));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(final DialogInterface dialog) {
                if (authenticationTask != null) {
                    authenticationTask.cancel(true);
                }
            }
        });
        return dialog;
    }

    @Subscribe
    public void onUnAuthorizedErrorEvent(UnAuthorizedErrorEvent unAuthorizedErrorEvent) {
        // Could not authorize for some reason.
        Toaster.showLong(BootstrapAuthenticatorActivity.this, R.string.message_bad_credentials);
    }

    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication.
     * <p/>
     * Specified by android:onClick="handleLogin" in the layout xml
     *
     * @param view
     */
    public void handleLogin(final View view) {
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        showProgress();

        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                final String query = String.format("%s=%s&%s=%s",
                        PARAM_USERNAME, email, PARAM_PASSWORD, password);

                User loginResponse = vehmonService.Authenticate(email, password);

                if (!loginResponse.isSuccessful())
                    throw new Exception(loginResponse.errorMessage);

                final BootstrapApplication globalApplication = (BootstrapApplication)getApplicationContext();
                globalApplication.setUser(loginResponse);
                storeUserInPref(loginResponse.getUsername());
                token = loginResponse.getSessionToken();

                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                // Retrofit Errors are handled inside of the {
                if(!(e instanceof RetrofitError)) {
                    final Throwable cause = e.getCause() != null ? e.getCause() : e;
                    if(cause != null) {
                        Toaster.showLong(BootstrapAuthenticatorActivity.this, cause.getMessage());
                    }
                }
                else
                    Toaster.showLong(BootstrapAuthenticatorActivity.this, e.getMessage());
            }

            @Override
            public void onSuccess(final Boolean authSuccess) {
                onAuthenticationResult(authSuccess);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
                authenticationTask = null;
            }
        };
        authenticationTask.execute();
    }
    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     */

    protected void finishLogin() {
        authToken = token;

        SharedPreferences sharedPref = getSharedPreferences(Constants.VehmonSharedPrefs.name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("AuthToken",token);
        editor.commit();
        initScreen();
        finish();
    }

    private void initScreen() {
        navigateToMain();
    }

    private void navigateToMain() {
        final Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void storeUserInPref(String username)
    {
        SharedPreferences sharedPref = getSharedPreferences(Constants.VehmonSharedPrefs.name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username",username);
        editor.commit();
    }

    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        dismissDialog(0);
    }

    /**
     * Show progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        showDialog(0);
    }

    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param result
     */
    public void onAuthenticationResult(final boolean result) {
        if (result) {
            finishLogin();
        } else {
            Ln.d("onAuthenticationResult: failed to authenticate");
            if (requestNewAccount) {
                Toaster.showLong(BootstrapAuthenticatorActivity.this,
                        string.message_auth_failed_new_account);
            } else {
                Toaster.showLong(BootstrapAuthenticatorActivity.this,
                        string.message_auth_failed);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.update_application:
                pd = ProgressDialog.show(BootstrapAuthenticatorActivity.this,"Update","Downloading Application",true);
                new AppdateApplicationAsync().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class AppdateApplicationAsync extends AsyncTask<Void, String, String>
    {
        protected String doInBackground(Void... params) {

            //Get Agents in background
            String errorMessage = "";
            try
            {
                if (!DownloadOnSDcard())
                {
                    errorMessage = "Error downloading application.";
                }
            }
            catch(Exception e)
            {
                errorMessage = e.getMessage();
            }
            return errorMessage;
        }

        public boolean DownloadOnSDcard()
        {
            FileOutputStream fos = null;
            InputStream is = null;
            HttpURLConnection c = null;
            try{
                URL urls = new URL(url+apkName); // My URL

                c = (HttpURLConnection) urls.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(false);
                c.connect(); // Connection Complete here.!

                String PATH = Environment.getExternalStorageDirectory() + "/download/";
                File file = new File(PATH); // PATH = /mnt/sdcard/download/
                if (!file.exists()) {
                    file.mkdirs();
                }

                //If file exists delete it, previously downloaded
                File f = new File(Environment.getExternalStorageDirectory() + "/download/" + apkName);
                if (f.exists()) {
                    f.delete();
                }

                File outputFile = new File(file,apkName);
                fos = new FileOutputStream(outputFile);

                is = c.getInputStream(); // Get from Server and Catch In Input Stream Object.

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1); // Write In FileOutputStream.
                }

                return true;
            }
            catch (IOException e)
            {
                return false;
            }
            finally
            {
                try
                {
                    if (fos != null)
                        fos.close();

                    if (is != null)
                        is.close();

                    if (c != null)
                        c.disconnect();
                }catch(Exception ex)
                {
                    return false;
                }

            }
        }

        protected void onProgressUpdate(String... progress) {
            //pd = ProgressDialog.show(BootstrapAuthenticatorActivity.this,"Update",progress[0],true);
        }

        protected void onPreExecute() {
            //pd = ProgressDialog.show(BootstrapAuthenticatorActivity.this,"Update","Confirming Update...",true);
        }

        protected void onPostExecute(String result) {
            pd.dismiss();
            if (!result.equals(""))
            {
                Toast.makeText(getBaseContext(), "Update Info: " + result, Toast.LENGTH_LONG).show();
            }
            else
            {
                //This is where i need to kick off install of application
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + apkName)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //also try this
                startActivityForResult(intent, 1001);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case(1001):{
                pd.dismiss();
                finish();
                break;
            }
        }
    }
}
