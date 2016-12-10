package com.huangmb.idea.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangmb on 2016/12/11.
 */
public class BaseTask {
    private boolean isCancel = false;

    public boolean isCancel() {
        return isCancel;
    }

    public void cancel() {
        isCancel = true;
    }

    public String getUrl() {
        return null;
    }
    public Map<String,String> getParams(){
        return new HashMap<>();
    }

    public Method getMethod() {
        return Method.GET;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
