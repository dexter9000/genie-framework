package com.genie.schedule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@ConfigurationProperties(prefix = "quartz")
public class QuartzProperties {

    private boolean enabled = false;

    private String timezone;

    private Properties properties;

    private Schedule[] schedules = new Schedule[0];

    private boolean storageResult = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Schedule[] getSchedules() {
        return schedules;
    }

    public void setSchedules(Schedule[] schedules) {
        this.schedules = schedules;
    }

    public boolean isStorageResult() {
        return storageResult;
    }

    public void setStorageResult(boolean storageResult) {
        this.storageResult = storageResult;
    }

    public static class Schedule {
        private String name;
        private String group;
        private String cron;
        private Class className;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Class getClassName() {
            return className;
        }

        public String getCron() {
            return cron;
        }

        public String getGroup() {
            return group;
        }

        public void setClassName(Class className) {
            this.className = className;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }

}
