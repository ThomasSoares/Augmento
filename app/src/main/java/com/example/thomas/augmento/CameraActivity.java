package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CameraActivity extends AppCompatActivity implements ModelLoader.ModelLoaderCallbacks{


    private static final String TAG=CameraActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION=3.0;

    private WritingArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ModelLoader modelLoader;
    private View view;
    private View fragment;


    private ImageButton augmentButton, cameraButton, videoButton;
    public ImageView stickerImageView;
    int count;
    Uri photoURI;
    File photoFile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkIsSupportedDeviceOrFinish(this))
        {
            return;
        }
        LocalStorage storeSticker=new LocalStorage(getApplicationContext());
        storeSticker.clear();
        storeSticker.addStorage("StickerCount","0");
        storeSticker.addStorage("StickerID",String.valueOf(R.drawable.house));
        setContentView(R.layout.activity_camera);
        fragment=findViewById(R.id.ux_fragment);



        LocalStorage shareSticker=new LocalStorage(getApplicationContext());
        if(shareSticker.getStorage("StickerCount").equalsIgnoreCase("0"))
        {
            count=0;
        }
        else
        {
            count= Integer.parseInt(shareSticker.getStorage("StickerCount"));
        }

        arFragment=(WritingArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        modelLoader=new ModelLoader(this);
        modelLoader.loadModel(this, Integer.valueOf(storeSticker.getStorage("StickerID")));

        cameraButton=findViewById(R.id.cameraButton);
        videoButton=findViewById(R.id.videoButton);
        stickerImageView=findViewById(R.id.stickerImageView);
        augmentButton=findViewById(R.id.augmentButton);
        view=findViewById(R.id.linearLayout);

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();

                    AnchorNode anchorNode = new AnchorNode(anchor);
                    String pos=anchor.getPose().toString();

                    String [] arr=pos.split("[:,\\]\\[]");
                    List<Float> positionsList=new ArrayList<>();
                    positionsList.add(Float.valueOf(arr[3]));
                    positionsList.add(Float.valueOf(arr[5]));
                    positionsList.add(Float.valueOf(arr[7]));

                    Toast.makeText(getApplicationContext(),arr[0],Toast.LENGTH_SHORT).show();

                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();



                    ++count;
                    shareSticker.addStorage("StickerCount",String.valueOf(count));
                    shareSticker.addStorage("Sticker"+count, storeSticker.getStorage("StickerGifID"));
                    shareSticker.addStorage("PositionX"+count, String.valueOf(positionsList.get(0)));
                    shareSticker.addStorage("PositionY"+count, String.valueOf(positionsList.get(1)));
                    shareSticker.addStorage("PositionZ"+count, String.valueOf(positionsList.get(2)));
                });

        stickerImageView.setOnClickListener(v->{
            Fragment fragment=new StickersFragment();
            loadFragment(fragment);
        });

        cameraButton.setOnClickListener(v->{
            takePhoto();
        });


        augmentButton.setOnClickListener(v->{
            Intent intent=new Intent(getApplicationContext(),ShareActivity.class);
            startActivity(intent);
        });
    }



    private String generateFilename()
    {
        String date=new SimpleDateFormat("yyyyMMddHHmmss",java.util.Locale.getDefault()).format(new Date());

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+"Augmento/"+date+"_augmento.jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    public void takePhoto()
    {
        final String filename=generateFilename();

        ArSceneView arSceneView=arFragment.getArSceneView();
        final Bitmap bitmap=Bitmap.createBitmap(arSceneView.getWidth(), arSceneView.getHeight(), Bitmap.Config.ARGB_8888);

        final HandlerThread handlerThread=new HandlerThread("PixelCopier");
        handlerThread.start();

        PixelCopy.request(arSceneView, bitmap, (copyResult ->
        {
            if(copyResult==PixelCopy.SUCCESS)
            {
                try
                {
                    saveBitmapToDisk(bitmap,filename);
                }
                catch (IOException e)
                {
                    Toast toast=Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG);
                    toast.show();
                    return ;
                }



                photoFile=new File(filename);

                CropImage.activity(Uri.fromFile(photoFile))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(3,4)
                        .start(this);

            }
            else
            {
                Log.d("CameraAR","Failed to copyPixels"+copyResult);
                Toast toast = Toast.makeText(getApplicationContext(), "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }

            handlerThread.quitSafely();

        }), new Handler(handlerThread.getLooper()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                Intent intent=new Intent(getApplicationContext(), AugmentActivity.class);
                intent.putExtra("URI",resultUri.toString());
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Error Image cant be cropped", Toast.LENGTH_SHORT).show();
            }
        }
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
