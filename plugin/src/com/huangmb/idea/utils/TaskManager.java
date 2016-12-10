package com.huangmb.idea.utils;

import com.google.gson.Gson;
import com.huangmb.idea.bean.BaseTask;
import com.huangmb.idea.bean.TranslateResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by huangmb on 2016/12/10.
 */
public class TaskManager {
    private boolean isSingleTask = true;
    private ExecutorService mPool;
    private Map<BaseTask, Future> mQueryTaskMap = new HashMap<>();

    private TaskManager() {
        mPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static TaskManager getInstance() {
        return QueryManagerInstance.instance;
    }

    public void setSingleTask(boolean singleTask) {
        this.isSingleTask = singleTask;
    }

    public boolean isSingleTask() {
        return isSingleTask;
    }

    public void execute(final BaseTask task, final CallBack callBack) {

        if (isSingleTask){
            shutdown();
        }
        Future future = mPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String query = HttpUtils.query(task);
                    TranslateResult result = null;
                    if (query != null) {
                        Gson gson = new Gson();
                        result = gson.fromJson(query, TranslateResult.class);
                    }
                    callBack.onSuccess(task, result);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onFailed(task, e);
                }
                mQueryTaskMap.remove(task);
            }
        });
        mQueryTaskMap.put(task, future);
    }

    public void shutdown(BaseTask task) {
        task.cancel();
        Future future = mQueryTaskMap.get(task);
        if (future != null && !future.isCancelled() && !future.isDone()) {
            future.cancel(true);
            mQueryTaskMap.remove(task);
        }
    }

    public synchronized void shutdown(){
        Iterator<Map.Entry<BaseTask, Future>> iterator = mQueryTaskMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<BaseTask,Future> entry = iterator.next();
            entry.getKey().cancel();
            Future future = entry.getValue();
            if (future != null && !future.isCancelled() && !future.isDone()) {
                future.cancel(true);
                iterator.remove();
            }
        }
    }

    public interface CallBack {
        void onSuccess(BaseTask task, TranslateResult result);

        void onFailed(BaseTask task, Exception e);
    }

    private static class QueryManagerInstance {
        private static TaskManager instance = new TaskManager();
    }
}
