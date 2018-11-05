package com.mavole.mavolenet.callback;

import com.google.gson.internal.$Gson$Types;
import com.mavole.mavolenet.exception.ZirukHttpException;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class DisposeDataListener<T> {

    public Type mType;

    public DisposeDataListener(){

        mType = getSuperclassTypeParameter(this.getClass());

    }

    public void onSuccess(T t){}

    public void onFailure(ZirukHttpException e){}

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {

            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

}
