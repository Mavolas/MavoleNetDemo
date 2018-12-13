package com.mavole.mavolenet.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mavole.mavolenet.R;
import com.mavole.mavolenet.configure.ConfigType;
import com.mavole.mavolenet.configure.MavoHttpConfigure;

/**
 * Author by Andy
 * Date on 2018/12/13.
 */
public class MavoImage {

    private static String BASE_URL = (String) MavoHttpConfigure.getConfigurations().get( ConfigType.API_HOST.name());

    private static RequestOptions getPlaceholder = new RequestOptions()
            .placeholder(R.drawable.pic_loading)
            .fallback(R.drawable.pic_load_error)
            //.diskCacheStrategy(DiskCacheStrategy.NONE); //禁用掉Glide的硬盘缓存功能了
            //.skipMemoryCache(true) //禁用内存缓存
            .error(R.drawable.pic_load_fail)
            .fitCenter();
    private static RequestOptions getThumbnail = new RequestOptions()
            .override(200, 200);


    public static void loadImage(Context context, String url, ImageView imageView){
        url = FormatUrl(url);
        Glide.with(context)
                .load(url)
                .apply(getPlaceholder)
                .into(imageView);

    }

    public static void loadImage(String url, ImageView imageView){
        url = FormatUrl(url);
        Context context = MavoHttpConfigure.getApplicationContext();
        Glide.with(context)
                .load(url)
                .apply(getPlaceholder)
                .into(imageView);
    }

    //把原图加载成 200*200 的缩略图
    public static void loadThumbImage(Context context, String url, ImageView imageView){
        url = FormatUrl(url);
        Glide.with(context)
                .load(url)
                .apply(getPlaceholder)
                .apply(getThumbnail)
                .into(imageView);
    }


    private static String FormatUrl(String url){
        //格式化Base_url 去掉api 后缀
        if (BASE_URL.endsWith("api/")){
            BASE_URL = BASE_URL.replace("api/", "");
        }

        //根据情况返回正确的uri
        if (url.contains("http://") || url.contains("https://")){
            return url;
        }else if ( url.startsWith("/") && !url.startsWith("/storage") && !url.startsWith("/data") ){
            return BASE_URL + url.substring(1,url.length());
        }else if ( url.startsWith("/storage") || url.startsWith("/data")){  //本机内存卡图片
            return url;
        }else {
            return BASE_URL + url;
        }

    }

}
