package com.genie.schedule.service.mapper;

import com.genie.schedule.service.dto.CronTriggerDTO;
import org.quartz.JobDataMap;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

@Component
public class CronTriggerMapper {

    public CronTriggerImpl toEntity(CronTriggerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();

        cronTriggerImpl.setName( dto.getName() );
        cronTriggerImpl.setGroup( dto.getGroup() );
        cronTriggerImpl.setKey( dto.getKey() );
        cronTriggerImpl.setJobName( dto.getJobName() );
        cronTriggerImpl.setJobGroup( dto.getJobGroup() );
        cronTriggerImpl.setDescription( dto.getDescription() );
        cronTriggerImpl.setCalendarName( dto.getCalendarName() );
        JobDataMap jobDataMap = dto.getJobDataMap();
        if ( jobDataMap != null ) {
            cronTriggerImpl.setJobDataMap(       new JobDataMap( jobDataMap )
            );
        }
        cronTriggerImpl.setPriority( dto.getPriority() );
        cronTriggerImpl.setMisfireInstruction( dto.getMisfireInstruction() );
        cronTriggerImpl.setFireInstanceId( dto.getFireInstanceId() );
        cronTriggerImpl.setStartTime( dto.getStartTime() );
        cronTriggerImpl.setEndTime( dto.getEndTime() );
        cronTriggerImpl.setNextFireTime( dto.getNextFireTime() );
        cronTriggerImpl.setPreviousFireTime( dto.getPreviousFireTime() );
        cronTriggerImpl.setTimeZone( dto.getTimeZone() );

        return cronTriggerImpl;
    }

    public CronTriggerDTO toDto(CronTriggerImpl entity) {
        if ( entity == null ) {
            return null;
        }

        CronTriggerDTO cronTriggerDTO = new CronTriggerDTO();

        cronTriggerDTO.setName( entity.getName() );
        cronTriggerDTO.setGroup( entity.getGroup() );
        cronTriggerDTO.setJobName( entity.getJobName() );
        cronTriggerDTO.setJobGroup( entity.getJobGroup() );
        cronTriggerDTO.setDescription( entity.getDescription() );
        JobDataMap jobDataMap = entity.getJobDataMap();
        if ( jobDataMap != null ) {
            cronTriggerDTO.setJobDataMap(       new JobDataMap( jobDataMap )
            );
        }
        cronTriggerDTO.setCalendarName( entity.getCalendarName() );
        cronTriggerDTO.setFireInstanceId( entity.getFireInstanceId() );
        cronTriggerDTO.setMisfireInstruction( entity.getMisfireInstruction() );
        cronTriggerDTO.setPriority( entity.getPriority() );
        cronTriggerDTO.setKey( entity.getKey() );
        cronTriggerDTO.setStartTime( entity.getStartTime() );
        cronTriggerDTO.setEndTime( entity.getEndTime() );
        cronTriggerDTO.setNextFireTime( entity.getNextFireTime() );
        cronTriggerDTO.setPreviousFireTime( entity.getPreviousFireTime() );
        cronTriggerDTO.setCronExpression( entity.getCronExpression() );
        cronTriggerDTO.setTimeZone( entity.getTimeZone() );

        return cronTriggerDTO;
    }
}
