package com.mavole.mavolenetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mavole.mavolenet.ZirukHttpClient;
import com.mavole.mavolenet.exception.ZirukHttpException;
import com.mavole.mavolenet.request.RequestParams;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void LoadData(boolean loadingMore){

        RequestParams params=new RequestParams();

        params.put("userid", CurrentUserInfoUtils.GetUserName(mContext));
        params.put("password",CurrentUserInfoUtils.getPassword(mContext));
        params.put("currentUser",CurrentUserInfoUtils.getGUID(mContext));
        params.put("SreenType",mHomeSend.ScreenType);
        params.put("depart",mFilterBean.getDepartment());
        params.put("storageType", mFilterBean.getStatus());
        params.put("Name",mFilterBean.getBidingName());
        params.put("Num",mFilterBean.getBidingCode());

        if (loadingMore){
            params.put("pageIndex",String.valueOf(pageIndex+1));
        }else {
            params.put("pageIndex",String.valueOf(0));
        }

        params.put("pageSize","10");

//        if (mFilterBean.getDayFrom() != null)
//            params.put("from",DateFormatUtils.format(mFilterBean.getDayFrom(), "yyyy/MM/dd"));
//        if (mFilterBean.getDayTo() != null)
//            params.put("to",DateFormatUtils.format(mFilterBean.getDayTo(), "yyyy/MM/dd"));


        ZirukHttpClient.newBuilder()
                .addParams(params)
                .setContext(mContext)
                .post()
                .url("/Purchase/Index")
                .build()
                .enqueue(new DisposeDataListener<ResponseCls<List<PurchaseInfo>>>() {

                    @Override
                    public void onSuccess(ResponseCls<List<PurchaseInfo>> response) {

                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadMore();

                        if ("0".equals(response.getRequestStatus()))
                        {
                            if (response.getData() == null || response.getData().size()<=0) {
                                ToastUtil.showToastShort("未检索到数据！");
                            }
                            else {

                                mList.addAll(response.getData());
                                myAdapter.notifyDataSetChanged();
                                pageIndex = response.pageInfo.PageIndex;

                            }
                        }
                        else if ("1".equals(response.getRequestStatus())) {
                            ToastUtil.showToastShort("账号密码不正确，请退出系统重新登录！");
                        }
                        else {
                            ToastUtil.showToastShort("系统错误，请与管理员联系！");
                        }
                    }

                    @Override
                    public void onFailure(ZirukHttpException e) {
                        super.onFailure(e);

                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadMore();

                        ToastUtil.showToastShort("无法连接至服务器！");
                    }

                });
    }

}
