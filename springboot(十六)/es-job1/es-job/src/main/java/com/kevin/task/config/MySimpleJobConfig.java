package com.kevin.task.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.kevin.task.listener.SimpleJobListener;
import com.kevin.task.task.MySimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: kevin
 * @Date: 2020/1/22
 */
@Configuration
public class MySimpleJobConfig {
    @Autowired
    private ZookeeperRegistryCenter registryCenter;

    @Autowired
    private JobEventConfiguration jobEventConfiguration;

    /**
     * 	具体真正的定时任务执行逻辑
     * @return
     */
    @Bean
    public SimpleJob simpleJob() {
        return new MySimpleJob();
    }

    /**
     * @param simpleJob
     * @return
     */
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(final SimpleJob simpleJob,
                                           @Value("${simpleJob.cron}") final String cron,
                                           @Value("${simpleJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${simpleJob.shardingItemParameters}") final String shardingItemParameters,
                                           @Value("${simpleJob.jobParameter}") final String jobParameter,
                                           @Value("${simpleJob.failover}") final boolean failover,
                                           @Value("${simpleJob.monitorExecution}") final boolean monitorExecution,
                                           @Value("${simpleJob.monitorPort}") final int monitorPort,
                                           @Value("${simpleJob.maxTimeDiffSeconds}") final int maxTimeDiffSeconds,
                                           @Value("${simpleJob.jobShardingStrategyClass}") final String jobShardingStrategyClass) {

        return new SpringJobScheduler(simpleJob,
                registryCenter,
                getLiteJobConfiguration(simpleJob.getClass(),
                        cron,
                        shardingTotalCount,
                        shardingItemParameters,
                        jobParameter,
                        failover,
                        monitorExecution,
                        monitorPort,
                        maxTimeDiffSeconds,
                        jobShardingStrategyClass),
                jobEventConfiguration,
                new SimpleJobListener());

    }


    private LiteJobConfiguration getLiteJobConfiguration(Class<? extends SimpleJob> jobClass, String cron,
                                                         int shardingTotalCount, String shardingItemParameters, String jobParameter, boolean failover,
                                                         boolean monitorExecution, int monitorPort, int maxTimeDiffSeconds, String jobShardingStrategyClass) {

        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration
                .newBuilder(jobClass.getName(), cron, shardingTotalCount)
                .misfire(true)
                .failover(failover)
                .jobParameter(jobParameter)
                .shardingItemParameters(shardingItemParameters)
                .build();

        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, jobClass.getCanonicalName());

        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration)
                .jobShardingStrategyClass(jobShardingStrategyClass)
                .monitorExecution(monitorExecution)
                .monitorPort(monitorPort)
                .maxTimeDiffSeconds(maxTimeDiffSeconds)
                .overwrite(true)
                .build();

        return liteJobConfiguration;
    }






}

