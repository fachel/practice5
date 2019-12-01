package ru.mirea.belov.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    final String TAG = MainActivity.class.getSimpleName();
    private ImageView imageView;
    private static final int MULTIPLE_REQUEST = 0;
    private boolean isWork = false;
    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    FileOutputStream fileoutputstream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        bytearrayoutputstream = new ByteArrayOutputStream();
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MULTIPLE_REQUEST && resultCode == RESULT_OK) {
// извлекаем изображение
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnailBitmap);

            //сохранение изображения
            thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
            file = new File( Environment.getExternalStorageDirectory() + "/SampleImage.png");
            try
            {
                file.createNewFile();
                fileoutputstream = new FileOutputStream(file);
                fileoutputstream.write(bytearrayoutputstream.toByteArray());
                fileoutputstream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
            //сохранение изображения
        }
    }

    public void imageViewOnClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, MULTIPLE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// permission granted
                isWork = true;
            } else {
                isWork = false;
            }
        }
    }
}
