package pdm.pt.ipb.estig.pdm_project;

/**
 * Class RegisterActivity - This class is responsible for showing the register activity.
 *
 * @author Ricardo Lucas - nº 15297
 * @version 23/09/2018
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private final static String FILE_PREFERENCE = "FilePreference";
    private SharedPreferences sharedPreferences;
    private final static String LANGUAGE_KEY = "language";
    private final static int MIN_PASSWORD_CHAR = 5;

    private FirebaseAuth firebaseAuth;
    private TextView txtRegisterTitle;
    private TextView txtEmailTitle;
    private EditText editEmailTxt;
    private TextView txtPasswordTitle;
    private EditText editPasswordTxt;
    private TextView txtSecondPasswordTitle;
    private EditText editSecondPasswordTxt;
    private Button btnBack;
    private Button btnRegister;

    private boolean verification;

    /**
     * Method onCreate - This method executes when this activity it's called
     * and it's responsible for initialize variables and do the translation
     * @param savedInstanceState - instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.initVariables();
        this.translation();
    }

    /**
     * Method initVariables - This method is responsible for initialize this class variables.
     *
     */
    public void initVariables(){
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.txtRegisterTitle = (TextView)findViewById(R.id.txtInitRegisterTitleId);
        this.txtEmailTitle = (TextView)findViewById(R.id.txtEmailTitleId);
        this.editEmailTxt = (EditText)findViewById(R.id.txtEmailEditId);
        this.txtPasswordTitle = (TextView)findViewById(R.id.txtPasswordTitleId);
        this.editPasswordTxt = (EditText)findViewById(R.id.txtPasswordEditId);
        this.txtSecondPasswordTitle = (TextView)findViewById(R.id.txtSecondPasswordTitleId);
        this.editSecondPasswordTxt = (EditText)findViewById(R.id.txtSecondPasswordEditId);
        this.btnBack = (Button)findViewById(R.id.btnBackId);
        this.btnRegister = (Button)findViewById(R.id.btnRegisterAccountId);
    }

    /**
     * Method btnBackClick - This method it's executes if the user clicks on Back button.
     * @param view - view
     */
    public void btnBackClick(View view)
    {
        this.startActivity(new Intent( RegisterActivity.this, MainActivity.class));
        this.finish();
    }

    /**
     * Method registerSucessfull - This method it's executes if the user makes the register successfully.
     *
     */
    public void registerSucessfull()
    {
        sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
        boolean isUk = value.equals("true");
        Toast.makeText(RegisterActivity.this, isUk ? R.string.regisAct_ToastAccCreated_UK:R.string.regisAct_ToastAccCreated_PT, Toast.LENGTH_LONG).show();
        this.startActivity(new Intent( RegisterActivity.this, MainActivity.class));
        this.finish();
    }


    /**
     * Method translation - This method is responsible for the activity translation, text views,
     * button text etc...
     * This method it's called in OnCreate method to make the translation in the begining
     */
    public void translation()
    {
        this.sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        if(sharedPreferences.contains(LANGUAGE_KEY))
        {
            String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
            boolean isUK = value.equals("true");

            this.txtRegisterTitle.setText(isUK ? R.string.regisAct_InitRegisterTitle_UK : R.string.regisAct_InitRegisterTitle_PT);
            this.txtEmailTitle.setText(isUK ? R.string.regisAct_EmailTitle_UK : R.string.regisAct_EmailTitle_PT);
            this.editEmailTxt.setHint(isUK ? R.string.regisAct_EmailBoxHint_UK : R.string.regisAct_EmailBoxHint_PT);
            this.txtPasswordTitle.setText(isUK ? R.string.regisAct_PasswordTitle_UK : R.string.regisAct_PasswordTitle_PT);
            this.editPasswordTxt.setHint(isUK ? R.string.regisAct_PasswordBoxHint_UK : R.string.regisAct_PasswordBoxHint_PT);
            this.txtSecondPasswordTitle.setText(isUK ? R.string.regisAct_SecondPasswordTitle_UK : R.string.regisAct_SecondPasswordTitle_PT);
            this.editSecondPasswordTxt.setHint(isUK ? R.string.regisAct_SecondPasswordBoxHint_UK: R.string.regisAct_SecondPasswordBoxHint_PT);
            this.btnBack.setText(isUK ? R.string.regisAct_btnBack_UK : R.string.regisAct_btnBack_PT);
            this.btnRegister.setText(isUK ? R.string.regisAct_btnRegisterAccount_UK : R.string.regisAct_btnRegisterAccount_PT);

        }
    }

    /**
     * Method createAccountClick - This method it's executes if the user clicks on register button, and checks the
     * fields and if it's all correct to make the regist on firebase, then creates a user with email and password
     * with the fields that user typed.
     * @param view - view
     */
    public void createAccountClick(View view){

        if(this.fieldsVerification() == true ){

            this.firebaseAuth.createUserWithEmailAndPassword( this.editEmailTxt.getText().toString(), this.editPasswordTxt.getText().toString())
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if( task.isSuccessful()){
                                registerSucessfull();
                            }else{
                                sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
                                String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
                                boolean isUk = value.equals("true");
                                Toast.makeText(RegisterActivity.this, isUk ? R.string.regisAct_ToastErrorRegis_UK:R.string.regisAct_ToastErrorRegis_PT, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    /**
     * Method fieldsVerification - This method verifies if the password fields are correct, if they have a minimum of 6 digits and if they are equal.
     * @return verification - true if fields arre correct false if not.
     */
    public boolean fieldsVerification(){
        this.verification = false;
        sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
        boolean isUk = value.equals("true");
        if(this.editEmailTxt.getText().toString().isEmpty() == true || this.editPasswordTxt.getText().toString().isEmpty() == true || this.editSecondPasswordTxt.getText().toString().isEmpty() == true){
            Toast.makeText(RegisterActivity.this, isUk ? R.string.regisAct_ToastFields_UK:R.string.regisAct_ToastFields_PT, Toast.LENGTH_LONG).show();
        }
        else{
            if(this.editPasswordTxt.getText().toString().equals(this.editSecondPasswordTxt.getText().toString()))
            {
                if(this.editPasswordTxt.getText().toString().length() > MIN_PASSWORD_CHAR) {
                    this.verification = true;
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, isUk ? R.string.regisAct_ToastPwDig_UK:R.string.regisAct_ToastPwDig_PT, Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(RegisterActivity.this, isUk ? R.string.regisAct_ToastPwSame_UK:R.string.regisAct_ToastPwSame_PT, Toast.LENGTH_LONG).show();
            }
        }
        return verification;
    }

}
