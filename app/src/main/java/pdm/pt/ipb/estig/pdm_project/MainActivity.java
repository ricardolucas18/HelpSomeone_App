package pdm.pt.ipb.estig.pdm_project;

/**
 * Class MainActivity - This class is responsible when the users starts the app.
 *
 * @author Ricardo Lucas - nº 15297
 * @version 23/09/2018
 */


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private final static String FILE_PREFERENCE = "FilePreference";
    private SharedPreferences sharedPreferences;
    private static final int REQUEST_PERMISSION = 2;
    private final static String LANGUAGE_KEY = "language";
    private TextView txtInitSessionTitle;
    private TextView txtEmailTitle;
    private EditText editEmailTxt;
    private TextView txtPasswordTitle;
    private EditText editPasswordTxt;
    private Button btnRegister;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private final static String PERMISSIONS_KEY = "permissions";


    /**
     * Method onCreate - This method executes when this activity it's called
     * and it's responsible for initialize variables and do the translation, checks if the user is already logged,
     * if not shows the interface with loggin and option to register.
     * @param savedInstanceState - instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        this.initVariables();
        this.initializeSwitchBtn();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        if(firebaseAuth.getCurrentUser()!= null){
            loginSuccessful();
        }
    }

    /**
     * Method initializeSwitchBtn - This method it's responsible for initialize the switch button.
     *
     */
    public void initializeSwitchBtn(){

        Switch switchBtn = (Switch) findViewById(R.id.switchBtnId);

        this.checkTranslation(switchBtn);

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isUk) {
                translation(isUk);

                String value = "" + isUk;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LANGUAGE_KEY, value);
                editor.commit();

            }
        });
    }

    /**
     * Method loginUser - This method it's responsible for logging the user checking the fields
     * and only logs the users if the fields are correct.
     * @param view - view
     */
    public void loginUser(View view){
        sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
        boolean isUk = value.equals("true");
        if(this.editEmailTxt.getText().toString().isEmpty() == true || this.editPasswordTxt.getText().toString().isEmpty() == true){
            Toast.makeText(MainActivity.this, isUk ? R.string.mainAct_ToastFields_UK:R.string.mainAct_ToastFields_PT, Toast.LENGTH_LONG).show();
        }
        else{
            this.firebaseAuth.signInWithEmailAndPassword(this.editEmailTxt.getText().toString(), this.editPasswordTxt.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if( task.isSuccessful()){
                        loginSuccessful();
                    }else{
                        sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
                        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
                        boolean isUk = value.equals("true");
                        Toast.makeText(MainActivity.this, isUk ? R.string.mainAct_ToastErrorLog_UK:R.string.mainAct_ToastErrorLog_PT, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /**
     * Method checkTranslation - This method it's responsible for checking translation.
     * @param switchBtn - Switch button.
     */
    public void checkTranslation(Switch switchBtn) {
        if(this.sharedPreferences.contains(LANGUAGE_KEY))
        {
            String text = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
            if(text.equals("true"))
            {
                switchBtn.setChecked(true);
                this.translation(true);
            }
            else
            {
                switchBtn.setChecked(false);
                this.translation(false);
            }
        }
    }

    /**
     * Method initVariables - This method is responsible for initialize this class variables.
     *
     */
    public void initVariables(){
        this.txtInitSessionTitle = (TextView)findViewById(R.id.txtInitSessionTitleId);
        this.txtEmailTitle = (TextView)findViewById(R.id.txtEmailTitleId);
        this.editEmailTxt = (EditText)findViewById(R.id.txtEmailEditId);
        this.txtPasswordTitle = (TextView)findViewById(R.id.txtPasswordTitleId);
        this.editPasswordTxt = (EditText)findViewById(R.id.txtPasswordEditId);
        this.btnRegister = (Button)findViewById(R.id.btnBackId);
        this.btnLogin = (Button)findViewById(R.id.btnLoginId);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Method translation - This method is responsible for the activity translation, text views,
     * button text etc...
     * This method it's called in OnCreate method to make the translation in the begining
     * @param isUk - this value indicates a value that is storage on Shared Preferences
     *             that defines the language. if isUk is true the language is portuguese, if isUk it's
     *             false the language is english.
     */
    public void translation(boolean isUk) {

        this.txtInitSessionTitle.setText(isUk ? R.string.mainAct_InitSessionTitle_UK : R.string.mainAct_InitSessionTitle_PT);
        this.txtEmailTitle.setText(isUk ? R.string.mainAct_EmailTitle_UK : R.string.mainAct_EmailTitle_PT);
        this.editEmailTxt.setHint(isUk ? R.string.mainAct_EmailBoxHint_UK : R.string.mainAct_EmailBoxHint_PT);
        this.txtPasswordTitle.setText(isUk ? R.string.mainAct_PasswordTitle_UK : R.string.mainAct_PasswordTitle_PT);
        this.editPasswordTxt.setHint(isUk ? R.string.mainAct_PasswordBoxHint_UK : R.string.mainAct_PasswordBoxHint_PT);
        this.btnRegister.setText(isUk ? R.string.mainAct_btnRegister_UK : R.string.mainAct_btnRegister_PT);
        this.btnLogin.setText(isUk ? R.string.mainAct_btnLogin_UK : R.string.mainAct_btnLogin_PT);

    }

    /**
     * Method btnRegisterClick - This method it's executed when the user clicks on register button and opens the register activity.
     * @param view - view.
     */
    public void btnRegisterClick(View view) {
        this.startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    /**
     * Method loginSuccessful - This method it's executed when the user are logged or made the log in successfully.
     */
    public void loginSuccessful() {
        sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
        boolean isUk = value.equals("true");
        Toast.makeText(MainActivity.this, isUk ? R.string.mainAct_ToastSuccessLog_UK:R.string.mainAct_ToastSuccessLog_PT, Toast.LENGTH_LONG).show();
        this.startActivity(new Intent(MainActivity.this, MenuActivity.class));
        this.finish();
    }
}
