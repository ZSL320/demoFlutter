package com.example.zslDev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class shareImage extends FlutterActivity {
    private EventChannel.EventSink eventSink;
    private MethodChannel methodChannel;
    static final String TYPE_IMG = "image/";

    @Override
    public void configureFlutterEngine(@NonNull  FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        methodChannel=new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),"sendPhotoToFlutter");
        methodChannel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NotNull MethodChannel.Result result) {
                if(call.method.equals("sendPhoto")){
                    checkHandleShare();
                }
            }
        });
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),"getPhoto").setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                eventSink=events;
            }

            @Override
            public void onCancel(Object arguments) {

            }
        });
    }
    void sendToFlutter(String event){
        eventSink.success(event);
    }
    private void checkHandleShare() {
        Intent intent = getIntent();
        //没有intent数据
        if (intent == null) {
            Log.d("intent Date===", "intent date  is null");
            return;
        }

        String type = intent.getType();
        //没有数据类型
        if (type == null) {
            Log.d("intent Date type===", "intent type is null");
            return;
        }
        if (type.startsWith(TYPE_IMG)) {
            handleImage(intent);
        }else{
            Toast.makeText(this,"只能解析图片",Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 处理图片 得到路径
     * Processing Pictures to Get Paths
     */
    private void handleImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        String[] imgData = getRealFilePath(imageUri);
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File file = new File(getExternalCacheDir(), imgData[0] + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            sendToFlutter(file.getPath());
        }catch (Exception e)
        {
            Toast.makeText(this,"图片解析异常",Toast.LENGTH_LONG).show();
        }
    }

    private String[] getRealFilePath(final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String name = null;
        String imgPath = null;
        if (scheme == null)
            imgPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            imgPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getApplication().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        imgPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (imgPath == null) {
            imgPath = uri.getPath();
        }

        int lastPoint = Objects.requireNonNull(imgPath).lastIndexOf(".");
        int startName = Objects.requireNonNull(imgPath).lastIndexOf("/");
        name = imgPath.substring(startName + 1, lastPoint);

        return new String[]{name, imgPath};
    }

}