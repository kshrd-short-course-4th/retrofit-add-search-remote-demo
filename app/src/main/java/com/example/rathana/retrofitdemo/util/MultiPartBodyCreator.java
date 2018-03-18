package com.example.rathana.retrofitdemo.util;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ratha on 11/30/2017.
 */

public class MultiPartBodyCreator {
    public  static MultipartBody.Part createPart(Context context, String param, Uri uri){
        File file=new File(uri.getPath());
        RequestBody requestBody=getRequestBody(context,file,uri);
        //Log.e("create MultiPartBody ->","work");
        MultipartBody.Part part=MultipartBody.Part.createFormData(param, file.getName(),requestBody);
        //Log.e("create MultiPartBody ->","work");
        return part;
    }

    protected static RequestBody getRequestBody(Context context,File file , Uri uri){
        if(null !=uri && null!=file){
            //Log.e("create requestBody->","work");
            return RequestBody.create(MediaType.parse("multipart/form-data"),file);
        }

        return null;
    }

}
