package com.example.mylibrary;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;




import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PickFromGalleryActivity extends AppCompatActivity {

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.e("Camera2BasicFragment", "opencv is  loaded " );
        }
    }

    private static final String TAG = "PickFromGalleryActivity" ;
    // the activity result code
    int SELECT_PICTURE = 200;
    private final static int SELECT_PHOTO = 1;
    private static boolean edgeDetection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pickfromgallery);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            edgeDetection=  extras.getBoolean("edgeDetection");

            //The key argument here must match that used in the other activity
        }
        if(isStoragePermissionGranted())
        {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " );
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK &&data!=null
              ) {
            try {
                Intent intent = new Intent();
                Log.e(TAG, "onActivityResult: "+ RealPathUtil.getPath(getApplicationContext(),data.getData() ));
                intent.putExtra("filename",RealPathUtil.getPath(getApplicationContext(),data.getData()));

                if(edgeDetection) {
                    RectData rectData = findEdges(data.getData());

                    if(rectData!=null){
                        intent.putExtra("x", rectData.x);
                        intent.putExtra("y", rectData.y);
                        intent.putExtra("w", rectData.w);
                        intent.putExtra("h", rectData.h);
                        intent.putExtra("edgeDetection", true);
                    }
                    else{
                        intent.putExtra("edgeDetection", false);
                    }
                }
                setResult(201, intent);

               finish();

            } catch (IOException e) {
                e.printStackTrace();
                Intent intent = new Intent();
                //intent.putExtra("filename",null);
                setResult(201, intent);
                finish();
            }


        }
        else{
            finish();
        }

    }

    private RectData findEdges(Uri uri) throws IOException {
        float degrees = 90; //rotation degree
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);



        //Log.e(TAG,"data: "+mat.size());
        Mat mat = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(bitmap, mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(mat, mat, 146, 250, Imgproc.THRESH_BINARY);

        // find contours
        List<MatOfPoint> contours = new ArrayList<>();
        List<RotatedRect> boundingRects = new ArrayList<>();
        Imgproc.findContours(mat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // find appropriate bounding rectangles
        for (MatOfPoint contour : contours) {
            MatOfPoint2f areaPoints = new MatOfPoint2f(contour.toArray());
            RotatedRect boundingRect = Imgproc.minAreaRect(areaPoints);
            boundingRects.add(boundingRect);
        }
        RectData dd = null;
        RotatedRect documentRect = getBestRectByArea(boundingRects);
        if (documentRect != null) {
            org.opencv.core.Point rect_points[] = new org.opencv.core.Point[4];
            documentRect.points(rect_points);
            for (int i = 0; i < 4; ++i) {
                Imgproc.line(mat, rect_points[i], rect_points[(i + 1) % 4], new Scalar(0, 255, 0), 5);
            }
            dd = new RectData(documentRect.boundingRect().width, documentRect.boundingRect().height, documentRect.boundingRect().x, documentRect.boundingRect().y);
        }

        return dd;
    }

    public static RotatedRect getBestRectByArea(List<RotatedRect> boundingRects) {
        RotatedRect bestRect = null;

        if (boundingRects.size() >= 1) {
            RotatedRect boundingRect;
            org.opencv.core.Point[] vertices = new org.opencv.core.Point[4];
            Rect rect;
            double maxArea;
            int ixMaxArea = 0;

            // find best rect by area
            boundingRect = boundingRects.get(ixMaxArea);
            boundingRect.points(vertices);
            rect = Imgproc.boundingRect(new MatOfPoint(vertices));
            maxArea = rect.area();

            for (int ix = 1; ix < boundingRects.size(); ix++) {
                boundingRect = boundingRects.get(ix);
                boundingRect.points(vertices);
                rect = Imgproc.boundingRect(new MatOfPoint(vertices));

                if (rect.area() > maxArea) {
                    maxArea = rect.area();
                    ixMaxArea = ix;
                }
            }

            bestRect = boundingRects.get(ixMaxArea);
        }


        return bestRect;
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.e(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
        }
        else {
            finish();
        }
    }



    }
