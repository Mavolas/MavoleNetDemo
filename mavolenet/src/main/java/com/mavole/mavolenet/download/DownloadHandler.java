package com.mavole.mavolenet.download;

import android.os.AsyncTask;

import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.callback.DownloadListener;
import com.mavole.mavolenet.common.RequestConstant;
import com.mavole.mavolenet.common.RestCreator;
import com.mavole.mavolenet.exception.CommonHttpException;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 宋棋安
 * on 2018/9/26.
 */
public class DownloadHandler {

    private final String mUrl;
    private final String mFullName;
    private final String DOWNLOAD_DIR;
    private final DownloadListener mListener;

    public DownloadHandler(String url, String download_dir, String fullName, final DownloadListener listener) {
        this.mUrl = url;
        this.mFullName = fullName;
        this.DOWNLOAD_DIR = download_dir;
        this.mListener = listener;

    }
    public final void HandleDownload(){

        if ( mListener != null ){
            mListener.onStart();
        }
        RestCreator.getRestService().download( mUrl )
                .enqueue( new Callback <ResponseBody>( ) {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if ( response.isSuccessful() ){
                            final ResponseBody responseBody = response.body();

                            if (responseBody !=null && responseBody.contentLength() > 0) {
                                final SaveFileTask task = new SaveFileTask(mListener);
                                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, responseBody, DOWNLOAD_DIR, mFullName);
                                //这里一定要注意判断，可能文件下载不全
                                if (task.isCancelled()) {
                                    if (mListener != null) {
                                        mListener.onEnd();
                                    }
                                }
                            }else {
                                mListener.onFailure( new CommonHttpException( response.code(), "file content is null!" ) );
                                mListener.onEnd();
                            }
                        }else {
                            if ( mListener != null ){
                                mListener.onFailure( new CommonHttpException( response.code(), response.message() ) );
                                mListener.onEnd();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call <ResponseBody> call, Throwable t) {
                        if ( mListener != null ){
                            mListener.onFailure(new CommonHttpException(RequestConstant.NETWORK_ERROR, t.getMessage()));
                            mListener.onEnd();
                        }
                    }
                } );

    }
}
