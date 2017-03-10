package com.tricktekno.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.tricktekno.myapplication.gestures.MoveGestureDetector;
import com.tricktekno.myapplication.gestures.RotateGestureDetector;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private ImageView img_scale;
    private float mScaleFactor = 0.5f;
    private float mRotationDegree = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private int mScreenHeight;
    private int mScreenWidth;
    private Matrix matrix = new Matrix();//Các lớp Matrix giữ một ma trận 3x3 để di chuyển tọa độ.
    private int mImageWidth, mImageHeight;
    private ScaleGestureDetector mScaleDetector;
    private RotateGestureDetector mRotateDetector;
    private MoveGestureDetector mMoveDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_scale = (ImageView) findViewById(R.id.img_scale);
        img_scale.setOnTouchListener(this);
        //// Lấy kích thước màn hình bằng pixel.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        //load anh mau
        Bitmap loadTempImg = BitmapFactory.decodeResource(getResources(), R.drawable.android);
        mImageHeight = loadTempImg.getHeight();
        mImageWidth = loadTempImg.getWidth();
        img_scale.setImageBitmap(loadTempImg);
        //view anh thu nho lai boi ma tran so voi anh goc
        matrix.postScale(mScaleFactor, mScaleFactor);
        img_scale.setImageMatrix(matrix);

        // Thiết lập Detectors Gesture
        mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        mRotateDetector = new RotateGestureDetector(getApplication(), new RotateListener());
        mMoveDetector = new MoveGestureDetector(getApplication(), new MoveListener());

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 1.0f));
            return true;
        }
    }

    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegree -= detector.getRotationDegreesDelta();
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            mFocusX += d.x;
            mFocusY += d.y;

            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mRotateDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);
        float scaleImageCenterX = (mImageWidth * mScaleFactor) / 2;
        float scaleImageCenterY = (mImageHeight * mScaleFactor) / 2;

        matrix.reset();//Thiết lập ma trận để tính
        matrix.postScale(mScaleFactor, mScaleFactor);//Bài concats ma trận với quy mô quy định.
        matrix.postRotate(mRotationDegree, scaleImageCenterX, scaleImageCenterY);//Postconcats ma trận với vòng xoay cảng quy định.
        matrix.postTranslate(mFocusX - scaleImageCenterX, mFocusY - scaleImageCenterY);//Postconcats ma trận với các dịch quy định.

        ImageView view = (ImageView) v;
        view.setImageMatrix(matrix);
        return true;
    }
}
