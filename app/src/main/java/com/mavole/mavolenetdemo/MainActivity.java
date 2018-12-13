package com.mavole.mavolenetdemo;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.mavole.mavolenetdemo.bean.FileInfo;
import com.mavole.mavolenetdemo.bean.ResClass;
import com.mavole.mavolenetdemo.bean.UserBean;
import com.mavole.mavolenetdemo.bean.WeatherResult;
import com.mavole.mavolenetdemo.util.Constant;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

    public void RequestGet(View view){
        RestClient.builder(Constant.URL_WEATHER)
//                .addQueryParams("userId","test")
//                .addQueryParams("password", "test")
//                .addQueryParams("flag", "3")
                .get()
                .enqueue(new DisposeDataListener<WeatherResult>(){

                    @Override
                    public void onSuccess(WeatherResult userResponseCls) {
                        Log.d(TAG, userResponseCls.getData().toString());
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
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
                        Log.d(TAG, userResponseCls.getData().toString());
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
                        Log.d(TAG, e.getEmsg().toString());
                    }

                });

    }

    public void Post(View view){

        Map<String, String> paramMap = new HashMap<>(1);
        paramMap.put("key", "00d91e8e0cca2b76f515926a36db68f5");
        paramMap.put("phone", "13594347817");
        paramMap.put("passwd", "123456");

        RestClient.builder(Constant.URL_LOGIN)
                .addPostParams("post", paramMap)
                .post()
                .enqueue(new DisposeDataListener<WeatherResult>(){

                    @Override
                    public void onSuccess(WeatherResult userResponseCls) {
                        Log.d(TAG, userResponseCls.getData().toString());
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
                        Log.d(TAG, e.getEmsg().toString());
                    }
                });
    }

    public void PostWithFiles(View view){

        FileBean fileBean = new FileBean();
        fileBean.fileName = "我是文件名";
        fileBean.file = new File(filePath);
        fileBean.key = "我是KEY";

        RestClient.builder(Constant.URL_Register)
                .addQueryParams("key","00d91e8e0cca2b76f515926a36db68f5 ")
                .addQueryParams("phone", "13594347817222")
                .addQueryParams("passwd", "8989")
                .addPostParams("我是KEY" ,fileBean)
                .postWithFiles()
                .enqueue(new DisposeDataListener<ResClass<UserBean>>(){

                    @Override
                    public void onSuccess(ResClass<UserBean> userResponseCls) {
                        Log.d(TAG, userResponseCls.getData().toString());
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
                        Log.d(TAG, e.getEmsg().toString());
                    }

                });


    }

    public void FileUpload(View view) throws FileNotFoundException {

        //final HashMap<String, File> files = new HashMap<String, File>();
        File file = new File(filePath);

        RestClient.builder("")
                .addQueryParams("userId", "123")
                .addQueryParams("password" ,"123")
                .addPostParams("test" ,file)
                .postWithFiles()
                .enqueue(new DisposeDataListener<List<FileInfo>>() {

                    @Override
                    public void onSuccess(List<FileInfo> response) {
                        Toast.makeText(MainActivity.this, response.get(0).downlaodPathFile,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(CommonHttpException e) {
                        Toast.makeText(MainActivity.this, "失败了"+e.getEmsg(),Toast.LENGTH_SHORT).show();

                    }

                });


    }

    //下载
    public void Download(View view){

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("正在下载...");
        dialog.setProgressStyle(dialog.STYLE_HORIZONTAL);

        RestClient.builder(Constant.URL_DOWNLOAD )
                .downloadConfig("123","123.apk")
                .download(new DownloadListener() {
                    @Override
                    public void onStart() {
                        dialog.show();
                        Log.d(TAG, "开始");
                    }

                    @Override
                    public void onProgress(Integer progress, Integer total, Integer downed) {
                        dialog.setProgress(progress);
                        Log.d(TAG, "下载了："+progress+"%" +" 总共："+ total +" 下载："+ downed);
                    }

                    @Override
                    public void onFinish(String path) {
                        Log.d(TAG, "完成：" + path);
                    }

                    @Override
                    public void onFailure(CommonHttpException exception) {
                        Log.d(TAG, "失败：" +exception.getEmsg());
                    }

                    @Override
                    public void onEnd() {
                        Log.d(TAG, "结束");
                        dialog.dismiss();
                    }
                });

    }

    //选择本地图片 地址 赋值给 filePath
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

}
