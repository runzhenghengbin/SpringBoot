package com.kevin.task.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;


/**
 * @author: kevin
 * @Date: 2020/1/22
 */

public class SimpleJobListener  implements ElasticJobListener {
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
       // log.info("-----------------执行任务之前：{}", shardingContexts);
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        //log.info("-----------------执行任务之前：{}", shardingContexts);

    }
}
