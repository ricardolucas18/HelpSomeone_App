package pdm.pt.ipb.estig.pdm_project;
/**
 * Class FeedActivity - This class is responsible for creating the feed activity showing all the posts that were uploaded.
 *
 * @author Ricardo Lucas - nº 15297
 * @version 23/09/2018
 */

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;

    private ValueEventListener mDBListener;

    private List<Upload> mUploads;
    private Upload upload;
    private FirebaseAuth firebaseAuth;

    private final static String FILE_PREFERENCE = "FilePreference";
    private SharedPreferences sharedPreferences;
    private final static String LANGUAGE_KEY = "language";



    /**
     * Method onCreate - This method executes when this activity it's called
     * and it's responsible for initialize variables and do the translation
     * @param savedInstanceState - instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initVariables();

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    upload = postSnapshot.getValue(Upload.class);

                    upload.setKey(postSnapshot.getKey());

                    mUploads.add(upload);

                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FeedActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Method initVariables - This method is responsible for initialize this class variables.
     *
     */
    public void initVariables(){
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        mAdapter = new ImageAdapter(FeedActivity.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(FeedActivity.this);

        mProgressCircle = findViewById(R.id.progress_circle);

        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        firebaseAuth = FirebaseAuth.getInstance();


    }

    /**
     * Method onItemClick - This method executes when user clicks on one item.
     * @param position - item position
     */
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click"+position, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method onGetLocationClick - This method executes when user clicks and chooses getlocation option.
     * @param position - item position
     */
    @Override
    public void onGetLocationClick(int position) {
        sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
        boolean isUk = value.equals("true");
        Upload selectItem = mUploads.get(position);
        //Toast.makeText(this, isUk ? R.string.feedAct_ToastGetLocation_UK+"("+selectItem.getLatitude().toString()+","+selectItem.getLongitude().toString()+")":R.string.feedAct_ToastGetLocation_PT+"("+selectItem.getLatitude().toString()+
        // ","+selectItem.getLongitude().toString()+")", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "LatLng:("+selectItem.getLatitude().toString()+","+selectItem.getLongitude().toString()+")", Toast.LENGTH_LONG).show();
    }

    /**
     * Method onDeleteClick - This method executes when user clicks and chooses delete
     * option, and if the user was the creater deletes the post if not he doesn't do anything.
     * @param position - item position
     */
    @Override
    public void onDeleteClick(int position) {

        Upload selectItem = mUploads.get(position);
        final String selectedKey = selectItem.getKey();
        sharedPreferences = getSharedPreferences(FILE_PREFERENCE, 0);
        String value = sharedPreferences.getString(LANGUAGE_KEY, "Error null");
        boolean isUk = value.equals("true");

        if (selectItem.getCreatorId().equals(firebaseAuth.getCurrentUser().getUid().toString()) == true){
            StorageReference imageRef = mStorage.getReferenceFromUrl(selectItem.getImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    //Toast.makeText(FeedActivity.this, "item deleted", Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(FeedActivity.this, isUk ? R.string.feedAct_ToastDelete_UK:R.string.feedAct_ToastDelete_PT, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(FeedActivity.this, isUk ? R.string.feedAct_ToastFailDelete_UK:R.string.feedAct_ToastFailDelete_PT, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method onDestroy - This method executes when this activity it's destroyed.
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}
