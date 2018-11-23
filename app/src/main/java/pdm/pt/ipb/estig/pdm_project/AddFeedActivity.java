package pdm.pt.ipb.estig.pdm_project;

/**
 * Class AddFeedActivity - This class is responsible for adding a new post to firebase, using
 * camera or galery, the user can use google maps to see his exact position.
 *
 * @author Ricardo Lucas - nº 15297
 * @version 23/09/2018
 */

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddFeedActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESQUEST_MAP = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private final static String FILE_PREFERENCE = "FilePreference";
    private SharedPreferences sharedPreferences;
    private final static String LANGUAGE_KEY = "language";
    //private Button btnCamera;
    private Button btnGallery;
    private TextView txtTitle;
    private EditText editTitleTxt;
    private TextView txtDescription;
    private EditText editDescriptionTxt;
    private TextView txtCity;
    private EditText editCityTxt;
    private TextView txtCountry;
    private EditText editCountryTxt;
    private TextView txtLatitude;
    private EditText editLatitudeTxt;
    private TextView txtLongitude;
    private EditText editLongitudeTxt;
    private Button btnGPS;
    private Button btnConfirm;
    private ImageView imageChoosen;
    private FirebaseAuth firebaseAuth;
    private String photoPath;
    private File photoFile = null;
    private Uri mImageUri;
    private Uri photoUri;
    private ProgressBar mProgressBar;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private Calendar calender;
    private MapsActivity googleMap;

    private static final String TAG = "AddFeedActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    /**
     * Method onCreate - This method executes when this activity it's called
     * and it's responsible for initialize variables and do the translation
     * @param savedInstanceState - instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

        //teclado não mexer na layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        this.initVariables();

        this.translation();

        this.alertPhoto();

        this.btnGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openGallery();
            }
        });

        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
                    String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
                    boolean isUk = value.equals("true");
                    Toast.makeText(AddFeedActivity.this, isUk ? R.string.addAct_ToastUploadProgress_UK:R.string.addAct_ToastUploadProgress_PT, Toast.LENGTH_LONG).show();
                }else{
                    uploadFile();
                }
            }
        });

    }

    /**
     * Method initVariables - This method is responsible for initialize this class variables.
     *
     */
    public void initVariables() {

        //this.btnCamera = (Button) findViewById(R.id.btnCameraId);
        this.btnGallery = (Button) findViewById(R.id.btnGalleryId);
        this.txtTitle = (TextView) findViewById(R.id.txtTitleId);
        this.editTitleTxt = (EditText) findViewById(R.id.txtTitleEditId);
        this.txtDescription = (TextView) findViewById(R.id.txtDescriptionId);
        this.editDescriptionTxt = (EditText) findViewById(R.id.txtDescriptionEditId);
        this.txtCity = (TextView) findViewById(R.id.txtCityId);
        this.editCityTxt = (EditText) findViewById(R.id.txtCityEditId);
        this.txtCountry = (TextView) findViewById(R.id.txtCountryId);
        this.editCountryTxt = (EditText) findViewById(R.id.txtCountryEditId);
        this.txtLatitude = (TextView) findViewById(R.id.txtLatitudeId);
        this.editLatitudeTxt = (EditText) findViewById(R.id.txtLatitudeEditId);
        this.txtLongitude = (TextView) findViewById(R.id.txtLongitudeId);
        this.editLongitudeTxt = (EditText) findViewById(R.id.txtLongitudeEditId);
        this.btnGPS = (Button) findViewById(R.id.btnGPSId);
        this.btnConfirm = (Button) findViewById(R.id.btnConfirmId);
        this.imageChoosen = (ImageView) findViewById(R.id.image_view);
        this.mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);


        this.firebaseAuth = FirebaseAuth.getInstance();

        this.mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        this.calender = Calendar.getInstance();

    }

    /**
     * Method translation - This method is responsible for the activity translation, text views,
     * button text etc...
     * This method it's called in OnCreate method to make the translation in the begining
     * @param isUk - this value indicates a value that is storage on Shared Preferences
     *             that defines the language. if isUk is true the language is portuguese, if isUk it's
     *             false the language is english.
     */
    public void translation() {
        this.sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        if (sharedPreferences.contains(LANGUAGE_KEY)) {
            String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
            boolean isUK = value.equals("true");

            //this.btnCamera.setText(isUK ? R.string.addAct_btnCamera_UK : R.string.addAct_btnCamera_PT);
            this.btnGallery.setText(isUK ? R.string.addAct_btnGallery_UK : R.string.addAct_btnGallery_PT);
            this.txtTitle.setText(isUK ? R.string.addAct_txtTitle_UK : R.string.addAct_txtTitle_PT);

            this.editTitleTxt.setHint(isUK ? R.string.addAct_TitleBoxHint_UK : R.string.addAct_TitleBoxHint_PT);
            this.txtDescription.setText(isUK ? R.string.addAct_txtDescription_UK : R.string.addAct_txtDescription_PT);
            this.editDescriptionTxt.setHint(isUK ? R.string.addAct_DescriptionBoxHint_UK : R.string.addAct_DescriptionBoxHint_PT);
            this.txtCity.setText(isUK ? R.string.addAct_txtCity_UK : R.string.addAct_txtCity_PT);

            this.editCityTxt.setHint(isUK ? R.string.addAct_CityBoxHint_UK : R.string.addAct_CityBoxHint_PT);
            this.txtCountry.setText(isUK ? R.string.addAct_txtCountry_UK : R.string.addAct_txtCountry_PT);
            this.editCountryTxt.setHint(isUK ? R.string.addAct_CountryBoxHint_UK : R.string.addAct_CountryBoxHint_PT);
            this.txtLatitude.setText(isUK ? R.string.addAct_txtLatitude_UK : R.string.addAct_txtLatitude_PT);

            this.editLatitudeTxt.setHint(isUK ? R.string.addAct_LatitudeBoxHint_UK : R.string.addAct_LatitudeBoxHint_PT);
            this.txtLongitude.setText(isUK ? R.string.addAct_txtLongitude_UK : R.string.addAct_txtLongitude_PT);

            this.editLongitudeTxt.setHint(isUK ? R.string.addAct_LongitudeBoxHint_UK : R.string.addAct_LongitudeBoxHint_PT);

            this.btnGPS.setText(isUK ? R.string.addAct_btnGPS_UK : R.string.addAct_btnGPS_PT);

            this.btnConfirm.setText(isUK ? R.string.addAct_btnConfirm_UK : R.string.addAct_btnConfirm_PT);


        }
    }

    /**
     * Method alertPhoto - This method shows a alert to see if the user wants to take a picture with camera or already has it on galery
     * This method it's part of Requirement 4.
     */
    private void alertPhoto() {
        this.sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
        boolean isUk = value.equals("true");


        AlertDialog.Builder dialog = new AlertDialog.Builder(AddFeedActivity.this);
        dialog.setTitle(isUk ? R.string.addAct_alertTitle_UK : R.string.addAct_alertTitle_PT);
        dialog.setMessage(isUk ? R.string.addAct_alertText_UK : R.string.addAct_alertText_PT);
        dialog.setCancelable(false);
        dialog.setNegativeButton(isUk ? R.string.addAct_alertCamera_UK : R.string.addAct_alertCamera_PT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dispatchTakePictureIntent();
                galleryAddPic();
            }
        });
        dialog.setPositiveButton(isUk ? R.string.addAct_alertGallery_UK : R.string.addAct_alertGallery_PT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openGallery();
            }
        });

        dialog.create();
        dialog.show();

    }

    /**
     * This method is the result after open the gallery or camera or the map.
     * @param requestCode - request code
     * @param resultCode - result code
     * @param data - data intent
     * This method it's part of Requirement 3 and 4.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(imageChoosen);
            imageChoosen.setImageURI(mImageUri);


        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            openGallery();
        }
        if (requestCode == RESQUEST_MAP) {
            if(resultCode == RESULT_OK) {

                this.editCityTxt.setText(data.getStringExtra("editTextCity"));
                this.editCountryTxt.setText(data.getStringExtra("editTextCountry"));

                //String latitude = String.valueOf(googleMap.getLatitude());
                this.editLatitudeTxt.setText(data.getStringExtra("editTextLat"));

                //String longitude = String.valueOf(googleMap.longitude);
                this.editLongitudeTxt.setText(data.getStringExtra("editTextLng"));

            }
        }

    }


    /**
     * Method createImageFile - This method is responsible for creating an image file, with a unique file name, to save the image after the user use the camera
     * @return image - returns file ofr image.
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //Log.i("TESTE","ENTROU1"+ storageDir);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir);
        // Save a file: path for use with ACTION_VIEW intents
        this.photoPath = image.getAbsolutePath();
        //Log.i("TESTE","ENTROU134"+ this.photoPath);
        return image;
    }

    /**
     * Method dispatchTakePictureIntent - This method is responsible for creating an intent to take the
     * picture and saving on the file int the method createImageFile() that it's called.
     *
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            //File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                this.photoUri = FileProvider.getUriForFile(this,
                        "pdm.pt.ipb.estig.pdm_project.fileprovider",
                        photoFile);


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                //Log.i("TESTE","ENTROU2");
            }
        }
    }

    /**
     * Method openGallery - This method is responsible for opening gallery.
     *
     */
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Method openGallery - This method is responsible for adding the new picture to the gallery.
     *
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(this.photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * Method getFileExtension - This method returns the file extension
     * @param uri - image identifier to get extension.
     * @return extension.
     */
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /**
     * Method uploadFile - This method is responsible for uploading the file with the data in firebase storage and firebase database.
     *
     */
    private void uploadFile(){
        if(mImageUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
                            String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
                            boolean isUk = value.equals("true");
                            String currentDate = DateFormat.getDateInstance().format(calender.getTime());
                            Toast.makeText(AddFeedActivity.this, isUk ? R.string.addAct_ToastUpload_UK:R.string.addAct_ToastUpload_PT, Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(editTitleTxt.getText().toString().trim(), editDescriptionTxt.getText().toString().trim(), editCityTxt.getText().toString().trim(), editCountryTxt.getText().toString().trim(), editLatitudeTxt.getText().toString().trim(), editLongitudeTxt.getText().toString().trim(), firebaseAuth.getCurrentUser().getUid(), currentDate, taskSnapshot.getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddFeedActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int)progress);
                        }
                    });
        }else{
            Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Method isServicesOK - This method checks if google maps services(version) on the users phone are ok.
     * @return true or false, true if the service is ok, false if not.
     */
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesInstalled: checking google services version");

        int availabe = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AddFeedActivity.this);

        if (availabe == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            return true;

        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabe)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServiceOK: an error occured, we can fix it for you");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AddFeedActivity.this, availabe, ERROR_DIALOG_REQUEST);
            dialog.show();

        } else {
            Toast.makeText(AddFeedActivity.this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Method btnGpsClicked - This method it's executed if the user click on the get location button, then opens the map.
     *@param view - view.
     */
    public void btnGpsClicked(View view) {
        if (isServicesOK()) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivityForResult(intent, RESQUEST_MAP);
        }
    }
}