package com.genie.flink;

import org.apache.flink.api.common.JobID;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SocketWindowWordCountTest {

    @Test
    public void test() throws Exception {
        Configuration config = new Configuration();
        config.setString(JobManagerOptions.ADDRESS, "10.200.8.11");
        config.setInteger(JobManagerOptions.PORT, 8081);

        RestClusterClient client = new RestClusterClient(config, "test" );

        CompletableFuture<Collection<JobStatusMessage>> jobDetailsFuture  =  client.listJobs();
        Collection<JobStatusMessage> jobDetails = jobDetailsFuture.get();

        System.out.println(jobDetails.size());


        client.getClusterStatus();

        JobGraph jobGraph = new JobGraph("testjob");
        Path path = new Path();
        jobGraph.addJar(path);
        JobID jobId = jobGraph.getJobID();
        client.submitJob(jobGraph);
    }
}
