package com.mavole.mavolenetdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mavole.mavolenet.RestClient;
import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.callback.DownloadListener;
import com.mavole.mavolenet.exception.CommonHttpException;
import com.mavole.mavolenet.model.FileBean;
import com.mavole.mavolenet.model.ResponseCls;
import com.mavole.mavolenetdemo.bean.UserBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class MainActivity extends AppCompatActivity {


    private static String TAG = MainActivity.class.getSimpleName();

    String filePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Log.d(TAG, timeout +"timeout");
    }

    public void RequestGet(View view){


        RestClient.builder("DataSynch/getUserFlag")
                .addQueryParams("userId","test")
                .addQueryParams("password", "test")
                .addQueryParams("flag", "3")
                .get()
                .enqueue(new DisposeDataListener<ResponseCls<UserBean>>(){

                    @Override
                    public void onSuccess(ResponseCls<UserBean> userResponseCls) {
                        super.onSuccess(userResponseCls);
                        Log.d(TAG, userResponseCls.getData().toString());
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
                        super.onFailure(e);
                        Log.d(TAG, e.getEmsg().toString());
                    }

                });

    }


    public void PostJson(View view){

        RestClient.builder("DataSynch/Download")
                .addQueryParams("userId","test")
                .addQueryParams("password", "test")
                .addPostParams("lastUpdateTimeMaster", new Date())
                .addPostParams("lastUpdateTimeTrans", new Date())
                .postJson()
                .enqueue(new DisposeDataListener<ResponseCls<UserBean>>(){

                    @Override
                    public void onSuccess(ResponseCls<UserBean> userResponseCls) {
                        //super.onSuccess(userResponseCls);

                        Log.d(TAG, userResponseCls.getData().toString());
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
                        super.onFailure(e);
                        Log.d(TAG, e.getEmsg().toString());
                    }

                });

    }

    public void PostWithFiles(View view){

        FileBean fileBean = new FileBean();
        fileBean.fileName = "我是文件名";
        fileBean.file = new File(filePath);
        fileBean.key = "我是KEY";





        RestClient.builder("DataSynch/upLoadFiles")
                .addQueryParams("userId","test")
                .addQueryParams("password", "test")
                .addQueryParams("flag", "3")
                .addPostParams("我是KEY" ,fileBean)
                .postWithFiles()
                .enqueue(new DisposeDataListener<ResponseCls<UserBean>>(){

                    @Override
                    public void onSuccess(ResponseCls<UserBean> userResponseCls) {
                        super.onSuccess(userResponseCls);
                        Log.d(TAG, userResponseCls.getData().toString());
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
                        super.onFailure(e);
                        Log.d(TAG, e.getEmsg().toString());
                    }

                });


        RestClient.builder("DataSynch/upLoadFiles")
                .addQueryParams("userId","test")
                .addQueryParams("userId","test")
                .downloadConfig()
                .download(new DownloadListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onProgress(Integer progress, Integer total, Integer downed) {

                    }

                    @Override
                    public void onFinish(String path) {

                    }

                    @Override
                    public void onFailure(CommonHttpException exception) {

                    }

                    @Override
                    public void onEnd() {

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
