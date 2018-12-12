package com.mavole.mavolenet.callback;

import com.mavole.mavolenet.exception.CommonHttpException;

/**
 * Author by Andy
 * Date on 2018/12/12.
 */
public interface DownloadListener {

    void onStart();//下载开始

    void onProgress(Integer progress, Integer total, Integer downed);//下载进度

    void onFinish(String path);//下载完成

    void onFailure(CommonHttpException exception); //下载失败

    void onEnd();//下载结束

}
