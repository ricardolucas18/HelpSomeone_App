package pdm.pt.ipb.estig.pdm_project;

/**
 * Class AboutActivity - This class just shows a simple text about the app
 *
 * @author Ricardo Lucas - nº 15297
 * @version 23/09/2018
 */

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private final static String FILE_PREFERENCE = "FilePreference";
    private SharedPreferences sharedPreferences;
    private final static String LANGUAGE_KEY = "language";
    private TextView txtAboutTitle;
    private TextView txtAboutText;

    /**
     * Method onCreate - This method executes when this activity it's called
     * and it's responsible for initialize variables and do the translation
     * @param savedInstanceState - instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.initVariables();
        this.translation();
    }

    /**
     * Method initVariables - This method is responsible for initialize this class variables.
     *
     */
    public void initVariables(){
        this.txtAboutTitle = (TextView) findViewById(R.id.txtAboutTitleId);
        this.txtAboutText = (TextView) findViewById(R.id.txtAboutTextId);
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

            this.txtAboutTitle.setText(isUK ? R.string.aboutAct_txtAboutTitle_UK : R.string.aboutAct_txtAboutTitle_PT);
            this.txtAboutText.setText(isUK ? R.string.aboutAct_txtMainText_UK : R.string.aboutAct_txtMainText_PT);


        }
    }
}
