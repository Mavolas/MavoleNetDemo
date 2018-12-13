package com.mavole.mavolenet.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.mavole.mavolenet.callback.DownloadListener;
import com.mavole.mavolenet.common.RequestConstant;
import com.mavole.mavolenet.configure.MavoHttpConfigure;
import com.mavole.mavolenet.exception.CommonHttpException;
import com.mavole.mavolenet.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import okhttp3.ResponseBody;

/**
 * Created by 宋棋安
 * on 2018/9/26.
 */
public class SaveFileTask extends AsyncTask<Object,Integer,File> {

    private final DownloadListener mListener;

    public SaveFileTask(DownloadListener listener) {
        this.mListener = listener;
    }

    @Override
    protected File doInBackground(Object... params) {

        final ResponseBody body = (ResponseBody) params[0];
        String downloadDir = (String) params[1];
        String fullName = (String) params[2];

        if ( downloadDir == null || downloadDir.equals( "" ) ){
            downloadDir = RequestConstant.DOWNLOAD_DIR;
        }

        if ( fullName == null || fullName.equals( "" ) || !fullName.contains(".") ){
            fullName = RequestConstant.DOWNLOAD_APK_FULLNAME;
        }

        return writeResponseBodyToDisk(body, downloadDir, fullName);

    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute( file );
        if ( mListener != null && file == null ){
            mListener.onFailure(new CommonHttpException(RequestConstant.FIEL_DOWNLOAD_ERROR, "file download fail, please check url"));
        }
        if ( mListener != null && file != null ){
            mListener.onFinish( file.getPath() );
        }
        if ( mListener != null ){
            mListener.onEnd();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Integer progress = values[0];
        Integer total = values[1];
        Integer downed = values[2];
        mListener.onProgress(progress, total, downed);
    }

    private void autoInstallApk(File file){
        if ( FileUtil.getExtension( file.getPath()).equals( "apk" ) ){

            final Intent install = new Intent(  );
            install.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            install.setAction( Intent.ACTION_VIEW );
            install.setDataAndType( Uri.fromFile( file ),"application/vnd.android.package-archive" );
            MavoHttpConfigure.getApplicationContext().startActivity( install );

        }

    }

    private File writeResponseBodyToDisk(ResponseBody body, String download_dir, String fullName) {

        final File file = FileUtil.createFile(download_dir, fullName);

        int totalByte; // 总大小
        int downByte;  // 已下载
        int progress;  // 记录进度条数量

        InputStream is =null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {

            // 获取文件大小
            long fileSize = body.contentLength();

            long fileSizeDownloaded = 0;

            is = body.byteStream();
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            // byte转Kbyte
            BigDecimal bd1024 = new BigDecimal(1024);
            totalByte = new BigDecimal(fileSize).divide(bd1024, BigDecimal.ROUND_HALF_UP).setScale(0).intValue();

            byte data[] = new byte[1024 * 4];

            // 方便以后可以添加取消事件
            while (true) {
                int read = bis.read(data);
                if (read == -1) {
                    break;
                }
                bos.write(data, 0, read);
                fileSizeDownloaded += read;
                // 计算进度
                progress = (int) (((float) (fileSizeDownloaded * 100.0 / fileSize)));
                downByte = new BigDecimal(fileSizeDownloaded).divide(bd1024, BigDecimal.ROUND_HALF_UP).setScale(0).intValue();
                // 子线程中，借助handler更新界面
                publishProgress(progress,totalByte,downByte);
            }

            bos.flush();
            fos.flush();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null){
                    is.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
