package com.example.godcode.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.godcode.ui.activity.ClipImageActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/7/11.
 */

public class BitmapUtil {
//    String[] PERMISSIONS = {
//            "android.permission.READ_EXTERNAL_STORAGE",
//            "android.permission.WRITE_EXTERNAL_STORAGE" };
//    //检测是否有写的权限
//    int permission = ContextCompat.checkSelfPermission(this,
//            "android.permission.WRITE_EXTERNAL_STORAGE");
//if (permission != PackageManager.PERMISSION_GRANTED) {
//        // 没有写的权限，去申请写的权限，会弹出对话框
//        ActivityCompat.requestPermissions(this, PERMISSIONS,1);
//    }

    private BitmapUtil() {
    }

    private static BitmapUtil defauleInstance;

    public static BitmapUtil getInstance() {
        BitmapUtil bitmapUtil = defauleInstance;
        if (defauleInstance == null) {
            synchronized (BitmapUtil.class) {
                bitmapUtil = new BitmapUtil();
                defauleInstance = bitmapUtil;
            }
        }
        return bitmapUtil;
    }


    public void saveBitmap(Bitmap bitmap, String bitName, Context context) {
        String fileName;
        File file;
        fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }
            Toast.makeText(context, "二维码已经保存到手机相册中", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }


    public boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void fromImg(Activity activity) {
        // 使用意图直接调用手机相册
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 打开手机相册,设置请求码
        activity.startActivityForResult(intent, 0);
    }

    public void resizeImage(Uri uri, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, 2);
    }

    public String showResizeImage(Intent data, Activity activity, ImageView imageView) {
        Bundle extras = data.getExtras();
        String path = null;
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            path = activity.getFilesDir().getPath() + File.separator + System.currentTimeMillis() + "headImage.jpg";
            BitmapUtil.getInstance().saveImage(photo, path);
            Drawable drawable = new BitmapDrawable(photo);
            imageView.setImageDrawable(drawable);
        }

        return path;
    }


    public Bitmap drawTextToBitmap(Bitmap bit, String mText) {
        try {
            Bitmap bitmap = bit;
            Bitmap.Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
            paint.setDither(true); //获取跟清晰的图像采样
            paint.setFilterBitmap(true);//过滤一些
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);//阴影制作半径，x偏移量，y偏移量，阴影颜色
            float textWidth = paint.measureText(mText);
            canvas.drawText(mText, bitmap.getWidth()/2-textWidth/2, bitmap.getHeight()-30, paint);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }



    }
