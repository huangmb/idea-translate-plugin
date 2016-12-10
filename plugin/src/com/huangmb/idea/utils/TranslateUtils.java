package com.huangmb.idea.utils;

import com.huangmb.idea.bean.TranslateTask;

/**
 * Created by huangmb on 2016/12/11.
 */
public class TranslateUtils {

    private static final String NAME = "TranslateUtils";
    private static TranslateTask lastTask;

    public static void query(String query, TaskManager.CallBack callBack){
        TaskManager manager = TaskManager.getInstance();
        if (lastTask != null){
            manager.shutdown(lastTask);
        }
        TranslateTask task = new TranslateTask(query);
        lastTask = task;
        manager.execute(task, callBack);
    }
}
