package com.genie.schedule.service;

import com.genie.schedule.config.QuartzProperties;
import com.genie.schedule.service.dto.JobInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;

@RunWith(SpringRunner.class)
public class OtherTest {

    @Test
    public void other() {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setArgsNames(new String[] {""});
        jobInfo.setArgsValues(new String[] {""});

        QuartzProperties quartzProperties = new QuartzProperties();
        quartzProperties.setProperties(new Properties());
        quartzProperties.setTimezone("");
        quartzProperties.getTimezone();
    }
}
