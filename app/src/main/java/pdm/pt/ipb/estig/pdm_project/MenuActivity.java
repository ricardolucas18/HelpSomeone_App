package pdm.pt.ipb.estig.pdm_project;
/**
 * Class MenuActivity - This class is responsible for showing the main menu.
 *
 * @author Ricardo Lucas - nº 15297
 * @version 23/09/2018
 */
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    private final static String FILE_PREFERENCE = "FilePreference";
    private SharedPreferences sharedPreferences;
    private final static String LANGUAGE_KEY = "language";
    private TextView txtMenuTitle;
    private Button btnFeed;
    private Button btnAddFeed;
    //private Button btnGallery;
    private Button btnGPS;
    private Button btnMyAccount;
    private Button btnAboutUs;
    private Button btnLogOut;
    private FirebaseAuth firebaseAuth;
    private static final int PICK_IMAGE_REQUEST = 1;


    private static final String TAG = "MenuActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    /**
     * Method onCreate - This method executes when this activity it's called
     * and it's responsible for initialize variables and do the translation
     * @param savedInstanceState - instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.initVariables();
        this.translation();

    }

    /**
     * Method initVariables - This method is responsible for initialize this class variables.
     *
     */
    public void initVariables() {

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.txtMenuTitle = (TextView) findViewById(R.id.txtMenuTitleId);
        this.btnFeed = (Button) findViewById(R.id.btnFeedId);
        this.btnAddFeed = (Button) findViewById(R.id.btnAddFeedId);
        this.btnAboutUs = (Button) findViewById(R.id.btnAboutUsId);
        this.btnLogOut = (Button) findViewById(R.id.btnLogOutId);
    }

    /**
     * Method translation - This method is responsible for the activity translation, text views,
     * button text etc...
     * This method it's called in OnCreate method to make the translation in the begining
     */
    public void translation() {
        this.sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        if (sharedPreferences.contains(LANGUAGE_KEY)) {
            String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
            boolean isUK = value.equals("true");

            this.txtMenuTitle.setText(isUK ? R.string.menuAct_MainMenuTitle_UK : R.string.menuAct_MainMenuTitle_PT);
            this.btnFeed.setText(isUK ? R.string.menuAct_btnFeed_UK : R.string.menuAct_btnFeed_PT);
            this.btnAddFeed.setText(isUK ? R.string.menuAct_btnAddFeed_UK : R.string.menuAct_btnAddFeed_PT);
            this.btnAboutUs.setText(isUK ? R.string.menuAct_btnAboutUs_UK : R.string.menuAct_btnAboutUs_PT);
            this.btnLogOut.setText(isUK ? R.string.menuAct_btnLogOut_UK : R.string.menuAct_btnLogOut_PT);

        }
    }

    /**
     * Method userLogOut - This method it's responsible for log out the user.
     * @param view - view
     */
    public void userLogOut(View view) {
        this.firebaseAuth.signOut();
        sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
        boolean isUk = value.equals("true");
        Toast.makeText(MenuActivity.this, isUk ? R.string.menuAct_ToastLogOut_UK:R.string.menuAct_ToastLogOut_PT, Toast.LENGTH_LONG).show();
        this.startActivity(new Intent(MenuActivity.this, MainActivity.class));
        this.finish();
    }


    /**
     * Method btnAboutClicked - This method it's executes if the user clicks on About us button.
     * @param view - view
     */
    public void btnAboutClicked(View view) {
        this.startActivity(new Intent(MenuActivity.this, AboutActivity.class));
    }

    /**
     * Method btnAddFeedClicked - This method it's executes if the user clicks on Add Feed button.
     * @param view - view
     */
    public void btnAddFeedClicked(View view) {

        this.startActivity(new Intent(MenuActivity.this, AddFeedActivity.class));
    }

    /**
     * Method btnFeed - This method it's executes if the user clicks on Feed button.
     * @param view - view
     */
    public void btnFeed(View view){
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

}
