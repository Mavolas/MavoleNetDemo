package com.mavole.mavolenetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mavole.mavolenet.RestClient;
import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.exception.CommonHttpException;
import com.mavole.mavolenet.model.ResponseCls;
import com.mavole.mavolenetdemo.bean.FileInfo;
import com.mavole.mavolenetdemo.bean.UserBean;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class MainActivity extends AppCompatActivity {


    String filePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void RequestGet(View view){

//        RequestParams params=new RequestParams();
//        params.put("userid", "123");
//
//        MavoHttpClient.newBuilder()
//                .addParams(params)
//                .setContext(MainActivity.this)
//                .post()
//                .url("getUserInfo")
//                .build()
//                .enqueue(new DisposeDataListener<ResponseCls<UserBean>>() {
//
//                    @Override
//                    public void onSuccess(ResponseCls<UserBean> response) {
//
//                        Toast.makeText(MainActivity.this, response.getData().userId,Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onFailure(CommonHttpException e) {
//                        super.onFailure(e);
//
//                        Toast.makeText(MainActivity.this, e.getEmsg().toString(),Toast.LENGTH_SHORT).show();
//
//                    }
//
//                });

        RestClient.builder("http://")
                .addQueryParams("userId","123")
                .addQueryParams("password", "234")
                .get()
                .enqueue(new DisposeDataListener<ResponseCls<FileInfo>>(){

                    @Override
                    public void onSuccess(ResponseCls<FileInfo> fileInfoResponseCls) {
                        super.onSuccess(fileInfoResponseCls);
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
                        super.onFailure(e);
                    }

                });


    }

    public void FileChoose(View view){

        RxGalleryFinalApi instance = RxGalleryFinalApi.getInstance(MainActivity.this);

        //设置自定义的参数
        instance
                .setType(RxGalleryFinalApi.SelectRXType.TYPE_IMAGE, RxGalleryFinalApi.SelectRXType.TYPE_SELECT_RADIO)
                .setImageRadioResultEvent(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {

                        Toast.makeText( MainActivity.this, "图片选择" +imageRadioResultEvent.getResult().getThumbnailBigPath() ,Toast.LENGTH_SHORT).show();

                        filePath = imageRadioResultEvent.getResult().getThumbnailBigPath();

                        //ToastUtil.showToastShort( "图片选择" +imageRadioResultEvent.getResult().getThumbnailBigPath());
                        //Glide.with(getActivity()).load("file://" +imageRadioResultEvent.getResult().getThumbnailSmallPath() ).into(simpleDraweeView);

                    }
                }).open();


    }

    public void FileUpload(View view) throws FileNotFoundException {

        //final HashMap<String, File> files = new HashMap<String, File>();
        File file = new File(filePath);

//        RequestParams params=new RequestParams();
//        params.put("userId", "123");
//        params.put("password", "123");
//        params.put("ID", "123");
//        params.put(StringUtils.substringAfterLast(filePath, "/"), file);
//
//        MavoHttpClient.newBuilder()
//                .addParams(params)
//                .setContext(MainActivity.this)
//                .postWithFiles()
//                .url("File/FileUpload")
//                .build()
//                .enqueue(new DisposeDataListener<List<FileInfo>>() {
//
//                    @Override
//                    public void onSuccess(List<FileInfo> response) {
//
//                        Toast.makeText(MainActivity.this, response.get(0).downlaodPathFile,Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onFailure(CommonHttpException e) {
//                        super.onFailure(e);
//
//                        Toast.makeText(MainActivity.this, "失败了"+e.getEmsg(),Toast.LENGTH_SHORT).show();
//
//                    }
//
//                });


    }

    public void FileUploads(View view){

        RxGalleryFinalApi instance = RxGalleryFinalApi.getInstance(MainActivity.this);
        //设置自定义的参数
        instance
                .setType(RxGalleryFinalApi.SelectRXType.TYPE_IMAGE, RxGalleryFinalApi.SelectRXType.TYPE_SELECT_RADIO)
                .setImageRadioResultEvent(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {

                        Toast.makeText( MainActivity.this, "图片选择" +imageRadioResultEvent.getResult().getThumbnailBigPath() ,Toast.LENGTH_SHORT).show();

                        //ToastUtil.showToastShort( "图片选择" +imageRadioResultEvent.getResult().getThumbnailBigPath());
                        //Glide.with(getActivity()).load("file://" +imageRadioResultEvent.getResult().getThumbnailSmallPath() ).into(simpleDraweeView);

                    }
                }).open();

    }

}
