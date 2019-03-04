package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView backArrowImageView;
    private Button shareButton;
    private EditText descriptionEditText;
    private ProgressBar shareProgressBar;

    private List<Sticker> stickerList=new ArrayList<>();
    private StickerAdapter mAdapter;

    private StorageReference postImagesReference;
    private DatabaseReference userRef, postRef;
    private FirebaseAuth mAuth;
    String currentUserID;

    private String saveCurrentDate;
    private String saveCurrentTime;
    private String postRandomName;
    private String downloadUrl;

    String description;

    public void initialize()
    {
        backArrowImageView=findViewById(R.id.backArrowImageView);
        recyclerView=findViewById(R.id.recyclerView);
        shareButton=findViewById(R.id.shareButton);
        descriptionEditText=findViewById(R.id.descriptionEditText);
        shareProgressBar=findViewById(R.id.shareProgressBar);

        mAdapter=new StickerAdapter(stickerList);
        FlexboxLayoutManager mLayoutManager=new FlexboxLayoutManager(getApplicationContext());
        mLayoutManager.setFlexDirection(FlexDirection.ROW);
        mLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        postImagesReference = FirebaseStorage.getInstance().getReference();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        postRef=FirebaseDatabase.getInstance().getReference().child("StickerPosts");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        downloadUrl=null;
    }

    public void listeners()
    {
        backArrowImageView.setOnClickListener(v -> {
            LocalStorage storage=new LocalStorage(getApplicationContext());
            storage.clear();
            finish();
        });

        shareButton.setOnClickListener(v->{
            hideKeyboard();
            validatePostInfo();
        });

    }

    public void hideKeyboard()
    {
        //FUNCTION THAT HIDES THE KEYBOARD

        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText())
        {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

    }

    public void validatePostInfo()
    {
        shareButton.setVisibility(View.INVISIBLE);
        shareProgressBar.setVisibility(View.VISIBLE);
        LocalStorage localStorage=new LocalStorage(getApplicationContext());
        description=descriptionEditText.getText().toString();
        if(TextUtils.isEmpty(description))
        {
            descriptionEditText.setError("Cannot be empty");

            shareButton.setVisibility(View.VISIBLE);
            shareProgressBar.setVisibility(View.GONE);
        }
        else if(Integer.parseInt(localStorage.getStorage("StickerCount"))==0)
        {
            shareButton.setVisibility(View.VISIBLE);
            shareProgressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Please Select and Place a Sticker!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //storingFileToFirebaseStorage();
            savingPostInfoToDatabase();
        }
    }

    public void storingFileToFirebaseStorage()
    {


        final StorageReference filePath=postImagesReference.child("Post Images").child(postRandomName+".txt");

    }

    public void savingPostInfoToDatabase()
    {
        LocalStorage localStorage=new LocalStorage(getApplicationContext());
        Calendar callForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate=currentDate.format(callForDate.getTime());

        Calendar callForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime=currentTime.format(callForTime.getTime());

        postRandomName=currentUserID+saveCurrentDate+saveCurrentTime;

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String username=dataSnapshot.child("Username").getValue().toString();
                    String userProfileImage=dataSnapshot.child("ProfileImage").getValue().toString();

                    HashMap postsMap=new HashMap();
                    postsMap.put("UserId",currentUserID);
                    postsMap.put("Date",saveCurrentDate);
                    postsMap.put("Time",saveCurrentTime);
                    postsMap.put("Description",description);


                    for (int i=1;i<=Integer.parseInt(localStorage.getStorage("StickerCount"));i++)
                    {
                        postsMap.put("PostSticker"+i,localStorage.getStorage("Sticker"+i));
                        postsMap.put("StickerPositionX"+i,localStorage.getStorage("PositionX"+i));
                        postsMap.put("StickerPositionY"+i,localStorage.getStorage("PositionY"+i));
                        postsMap.put("StickerPositionZ"+i,localStorage.getStorage("PositionZ"+i));
                    }
                    postsMap.put("ProfileImage",userProfileImage);
                    postsMap.put("Username",username);

                    postRef.child(postRandomName).updateChildren(postsMap).addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {

                            shareButton.setVisibility(View.VISIBLE);
                            shareProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Post is updated successfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            Toast.makeText(getApplicationContext(),"Error occured while updating",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void prepareStickerData()
    {
        LocalStorage stickerStorage=new LocalStorage(getApplicationContext());
        Sticker sticker;

        for(int i=1;i<=Integer.parseInt(stickerStorage.getStorage("StickerCount"));i++)
        {
            sticker=new Sticker(Integer.parseInt(stickerStorage.getStorage("Sticker"+i)));
            stickerList.add(sticker);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initialize();
        listeners();

        prepareStickerData();
    }
}
