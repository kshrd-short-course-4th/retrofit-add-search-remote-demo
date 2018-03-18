package com.example.rathana.retrofitdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rathana.retrofitdemo.data.remote.ServiceGenerator;
import com.example.rathana.retrofitdemo.data.remote.service.ArticleService;
import com.example.rathana.retrofitdemo.entity.Image;
import com.example.rathana.retrofitdemo.util.MultiPartBodyCreator;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends AppCompatActivity {
    String fileName;
    ImageButton btnChoose;
    ImageView photo;
    final static  int PICKER_CODE=0;
    ArticleService articleService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        btnChoose=findViewById(R.id.btnChoose);
        photo=findViewById(R.id.image);
        articleService= ServiceGenerator.generateService(ArticleService.class);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    PICKER_CODE);
        }
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,PICKER_CODE);
            }
        });
    }

    private static final String TAG = "UploadImageActivity";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICKER_CODE && resultCode==UploadImageActivity.RESULT_OK){
            Uri uri=data.getData();
            try{
                String[] filePathColumn={MediaStore.Images.Media.DATA};
                Cursor cursor =getContentResolver().query(
                        uri,filePathColumn,null,null,null
                );
                cursor.moveToFirst();
                int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                fileName=cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmap= BitmapFactory.decodeFile(fileName);
                photo.setImageBitmap(bitmap);
                Log.e(TAG, "onActivityResult: "+fileName );
                upload(fileName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            return;
        }
    }

    public void upload(String fileName){
        Uri uri = Uri.parse(fileName);
        Log.e("file->",fileName);
        MultipartBody.Part part= MultiPartBodyCreator.createPart(this,"FILE",uri);
        Call<Image> call= articleService.uploadImage(part);
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                Toast.makeText(UploadImageActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onResponse: "+ response.body().getData());
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {

            }
        });
    }
}


