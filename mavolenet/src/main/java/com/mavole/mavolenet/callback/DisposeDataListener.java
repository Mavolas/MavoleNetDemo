package com.mavole.mavolenet.callback;

import com.google.gson.internal.$Gson$Types;
import com.mavole.mavolenet.exception.CommonHttpException;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisposeDataListener<T> {

    public static Type mType;

    public DisposeDataListener(){
        mType = getSuperclassTypeParameter(this.getClass());
    }

    public void onSuccess(T t){}

    public void onFailure(CommonHttpException e){}

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

}
