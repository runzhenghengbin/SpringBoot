package com.kevin.task.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.kevin.task.entity.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: kevin
 * @Date: 2020/1/30
 */
public class MyDataflowJob implements DataflowJob<Foo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDataflowJob.class);

    @Override
    public List<Foo> fetchData(final ShardingContext shardingContext) {
        System.err.println("--------------@@@@@@@@@@ 抓取数据集合...--------------");
        List<Foo> list = new ArrayList<Foo>();
        list.add(new Foo("001", "张三"));
        list.add(new Foo("002", "李四"));
        return list;
    }

    @Override
    public void processData(final ShardingContext shardingContext, final List<Foo> data) {
        System.err.println("--------------@@@@@@@@@ 处理数据集合...--------------");
    }
}
