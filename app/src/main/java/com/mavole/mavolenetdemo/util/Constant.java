package com.mavole.mavolenetdemo.util;

/**
 * Author by Andy
 * Date on 2018/12/13.
 */
public class Constant {

    public final static String URL_BASE = "https://www.baidu.com/";
    //获取天气json
    public final static String URL_WEATHER= "https://www.apiopen.top/weatherApi";

    //注册接口
//    接口说明(如需上传头像，请使用POST)：
//    key=00d91e8e0cca2b76f515926a36db68f5        应用Key
//    phone=13594347817                        用户名/ID  唯一
//    passwd=123654                      密码
//    image=File文件        头像
//    name=peakchao    昵称/名字
//    text= 这是我的签名。    签名/备注
//    other=这是我的备注信息1 开发者需要使用的其他字段
//    other2=这是我的备注信息2 开发者需要使用的其他字段

    //返回值：

//    {
//        "code": 200,
//            "msg": "成功!",
//            "data": {
//        "key": "00d91e8e0cca2b76f515926a36db68f5",
//                "phone": "13594347817",
//                "name": "peakchao",
//                "passwd": "123456",
//                "text": "这是我的签名。",
//                "img": "https://res.apiopen.top/2018031817571995.key.png",
//                "other": "这是我的备注信息1",
//                "other2": "这是我的备注信息2",
//                "createTime": "2018-03-18 17:57:20"
//        }
//    }


    public final static String URL_Register= "https://www.apiopen.top/createUser";
    //登陆测试

//    key=00d91e8e0cca2b76f515926a36db68f5 应用Key
//    phone=13594347817 用户名/ID 唯一
//    passwd=123654 密码

//    {
//        "code": 200,
//            "msg": "成功!",
//            "data": {
//        "key": "00d91e8e0cca2b76f515926a36db68f5",
//                "phone": "13594347817",
//                "name": "peakchao",
//                "passwd": "123456",
//                "text": "这是我的签名。",
//                "img": "https://res.apiopen.top/2018031817571995.key.png",
//                "other": "这是我的备注信息1",
//                "other2": "这是我的备注信息2",
//                "createTime": "2018-03-18 17:57:20"
//    }
//    }

    public final static String URL_LOGIN= "https://www.apiopen.top/login";
    //下载测试
    public final static String URL_DOWNLOAD= "http://url.cn/8iJtRQ";
}
