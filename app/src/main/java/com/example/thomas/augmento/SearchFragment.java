package com.example.thomas.augmento;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SearchFragment extends Fragment implements ModelLoader.ModelLoaderCallbacks{

    View parentHolder;
    private List<StickerViewLayout>postList=new ArrayList<>();
    private RecyclerView recyclerView;
    private StickerViewLayoutAdapter stickerViewLayoutAdapter;
    private DatabaseReference postsRef;
    private FirebaseRecyclerAdapter<StickerViewLayout, StickerViewLayoutAdapter.MyViewHolder> firebaseRecyclerAdapter;


    private static final String TAG=SearchFragment.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION=3.0;

    private WritingArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ModelLoader modelLoader;
    LocalStorage ls;
    int localCountPositions=0;
    int stickerCountPosition=0;


    String[] stickerArray;
    String[] postionXArray;
    String[] postionYArray;
    String[] postionZArray;
    List<String> stickerCountList=new ArrayList<>();

    Vector3 vector3;
    Vector3 vector31;

    ArrayList<String>totalSticker=new ArrayList<>();
    ArrayList<String>totalPositionX=new ArrayList<>();
    ArrayList<String>totalPositionY=new ArrayList<>();
    ArrayList<String>totalPositionZ=new ArrayList<>();

    int i=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentHolder= inflater.inflate(R.layout.fragment_search, container, false);
        arFragment=(WritingArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);

        recyclerView=parentHolder.findViewById(R.id.recyclerView);
        stickerViewLayoutAdapter=new StickerViewLayoutAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(stickerViewLayoutAdapter);
        postsRef= FirebaseDatabase.getInstance().getReference().child("StickerPosts");



        ls=new LocalStorage(getContext());
        modelLoader = new ModelLoader(this);

        //initialize
        displayAllUsers();

        if (Integer.parseInt(ls.getStorage("PostSticker0")) == R.drawable.house) {
            modelLoader.loadModel(getContext(), R.raw.house);

        } else if (Integer.parseInt(ls.getStorage("PostSticker0")) == R.drawable.andy) {
            modelLoader.loadModel(getContext(), R.raw.andy);

        } else if (Integer.parseInt(ls.getStorage("PostSticker0")) == R.drawable.spaceman) {
            modelLoader.loadModel(getContext(), R.raw.spaceman);

        } else if (Integer.parseInt(ls.getStorage("PostSticker0")) == R.drawable.flyingsaucer) {
            modelLoader.loadModel(getContext(), R.raw.flyingsacuer);

        } else if (Integer.parseInt(ls.getStorage("PostSticker0")) == R.drawable.uphouse) {
            modelLoader.loadModel(getContext(), R.raw.uphouse);

        }

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }
                    recyclerView.setVisibility(View.VISIBLE);


                    float xValue= Float.parseFloat(ls.getStorage("PositionX0"));
                    float yValue= Float.parseFloat(ls.getStorage("PositionY0"));
                    float zValue= Float.parseFloat(ls.getStorage("PositionZ0"));

                    vector3=new Vector3(xValue, yValue, zValue);

                    AnchorNode anchorNode = new AnchorNode();
                    anchorNode.setLocalPosition(vector3);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());



                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);

                });














        return parentHolder;
    }

    private void testRun() {
        for(int i=0;i<1;i++)
        {
            Toast.makeText(getContext(),ls.getStorage("StickerCounter"+i),Toast.LENGTH_SHORT).show();
        }

    }


    private void setAllAnchors(int i) {


    }


    public void displayAllUsers()
    {
        ls.clear();

        FirebaseRecyclerOptions<StickerViewLayout> posts=
                new FirebaseRecyclerOptions.Builder<StickerViewLayout>()
                        .setQuery(postsRef, snapshot -> {
                            String date=snapshot.child("Date").getValue().toString();
                            String description=snapshot.child("Description").getValue().toString();
                            String profileImage=snapshot.child("ProfileImage").getValue().toString();
                            String time=snapshot.child("Time").getValue().toString();
                            String userID=snapshot.child("UserId").getValue().toString();
                            String username=snapshot.child("Username").getValue().toString();
                            String count=snapshot.child("Count").getValue().toString();
                            stickerArray=new String[Integer.parseInt(count)];
                            postionXArray=new String[Integer.parseInt(count)];
                            postionYArray=new String[Integer.parseInt(count)];
                            postionZArray=new String[Integer.parseInt(count)];

                            ls.addStorage("StickerCounter"+stickerCountPosition,count);
                            stickerCountPosition++;
                            for (int i=1;i<=Integer.parseInt(snapshot.child("Count").getValue().toString());i++)
                            {
                                stickerArray[i-1]=snapshot.child("PostSticker"+i).getValue().toString();
                                postionXArray[i-1]=snapshot.child("StickerPositionX"+i).getValue().toString();
                                postionYArray[i-1]=snapshot.child("StickerPositionY"+i).getValue().toString();
                                postionZArray[i-1]=snapshot.child("StickerPositionZ"+i).getValue().toString();

                                ls.addStorage("PostSticker"+localCountPositions,snapshot.child("PostSticker"+i).getValue().toString());
                                ls.addStorage("PositionX"+localCountPositions, snapshot.child("StickerPositionX"+i).getValue().toString());
                                ls.addStorage("PositionY"+localCountPositions, snapshot.child("StickerPositionY"+i).getValue().toString());
                                ls.addStorage("PositionZ"+localCountPositions, snapshot.child("StickerPositionZ"+i).getValue().toString());
                                ++localCountPositions;
                            }
                            //Log.i("Individual",stickerArray.toString());
                            totalSticker.addAll(Arrays.asList(stickerArray));
                            totalPositionX.addAll(Arrays.asList(postionXArray));
                            totalPositionY.addAll(Arrays.asList(postionYArray));
                            totalPositionZ.addAll(Arrays.asList(postionZArray));
                            Log.i("Total Sticker",totalPositionX.toString());


                            return new StickerViewLayout(date,description,profileImage,time,userID,username,stickerArray,postionXArray,postionYArray,postionZArray);
                        })
                .build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<StickerViewLayout, StickerViewLayoutAdapter.MyViewHolder>(posts) {
            @Override
            protected void onBindViewHolder(@NonNull StickerViewLayoutAdapter.MyViewHolder myViewHolder, int i, @NonNull StickerViewLayout stickerViewLayout) {
                myViewHolder.setUsername(stickerViewLayout.getUsername());
                myViewHolder.setDescription(stickerViewLayout.getDescription());
                myViewHolder.setProfileImage(getContext(),stickerViewLayout.getProfileImage());
                myViewHolder.setSticker(getContext(),stickerViewLayout.getStickerArray(),modelLoader);


            }

            @NonNull
            @Override
            public StickerViewLayoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_post_layout, parent, false);



                return new StickerViewLayoutAdapter.MyViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        firebaseRecyclerAdapter.stopListening();
    }




    @Override
    public void setRenderable(ModelRenderable modelRenderable) {
        andyRenderable = modelRenderable;
    }

    @Override
    public void onLoadException(Throwable throwable) {
        Toast toast = Toast.makeText(getContext(), "Unable to load andy renderable", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Log.e(TAG, "Unable to load andy renderable", throwable);
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
