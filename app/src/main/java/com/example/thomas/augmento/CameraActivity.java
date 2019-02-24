package com.example.thomas.augmento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

public class CameraActivity extends AppCompatActivity implements ModelLoader.ModelLoaderCallbacks{

    private static final String TAG=CameraActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION=3.0;

    private WritingArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ModelLoader modelLoader;

    private ImageButton augmentButton, cameraButton, videoButton;
    public ImageView stickerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkIsSupportedDeviceOrFinish(this))
        {
            return;
        }
        LocalStorage storeSticker=new LocalStorage(getApplicationContext());
        storeSticker.addStorage("StickerID",String.valueOf(R.drawable.house));
        setContentView(R.layout.activity_camera);

        arFragment=(WritingArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        modelLoader=new ModelLoader(this);
        modelLoader.loadModel(this, Integer.valueOf(storeSticker.getStorage("StickerID")));

        cameraButton=findViewById(R.id.cameraButton);
        videoButton=findViewById(R.id.videoButton);
        stickerImageView=findViewById(R.id.stickerImageView);

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();

                    String position= String.valueOf(anchor.getPose());
                    Log.i("Position",position);
                });

        stickerImageView.setOnClickListener(v->{
            Fragment fragment=new StickersFragment();
            loadFragment(fragment);
        });
    }

    @Override
    public void setRenderable(ModelRenderable modelRenderable) {
        andyRenderable=modelRenderable;
    }

    @Override
    public void onLoadException(Throwable throwable) {
        Toast toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Log.e(TAG, "Unable to load andy renderable", throwable);
    }

    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ux_fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
