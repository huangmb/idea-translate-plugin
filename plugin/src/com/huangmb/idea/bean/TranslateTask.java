package com.huangmb.idea.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangmb on 2016/12/10.
 */
public class TranslateTask extends BaseTask {
    private String query;

    public TranslateTask(String query) {
        this.query = query;
    }

    public String getUrl() {
        return YouDaoConst.HOST + YouDaoConst.QUERY_PATH;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        map.put("keyfrom", YouDaoConst.KEY_FROM);
        map.put("key", YouDaoConst.API_KEY);
        map.put("type", "data");
        map.put("doctype", "json");
        map.put("version", YouDaoConst.VERSION);
        map.put("q", query);
        return map;
    }

    @Override
    public int hashCode() {
        return getUrl().hashCode() * 31 + query.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (obj instanceof TranslateTask){
            TranslateTask task = (TranslateTask) obj;
            if (query != null && query.equals(task.query)){
                return true;
            }
        }
        return false;
    }
}
