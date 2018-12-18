package com.genie.schedule.service.mapper;

import com.genie.schedule.service.dto.SimpleTriggerDTO;
import org.quartz.JobDataMap;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.stereotype.Component;

@Component
public class SimpleTriggerMapper {

    public SimpleTriggerDTO toDto(SimpleTriggerImpl entity) {
        if ( entity == null ) {
            return null;
        }

        SimpleTriggerDTO simpleTriggerDTO = new SimpleTriggerDTO();

        simpleTriggerDTO.setName( entity.getName() );
        simpleTriggerDTO.setGroup( entity.getGroup() );
        simpleTriggerDTO.setJobName( entity.getJobName() );
        simpleTriggerDTO.setJobGroup( entity.getJobGroup() );
        simpleTriggerDTO.setDescription( entity.getDescription() );
        JobDataMap jobDataMap = entity.getJobDataMap();
        if ( jobDataMap != null ) {
            simpleTriggerDTO.setJobDataMap(       new JobDataMap( jobDataMap )
            );
        }
        simpleTriggerDTO.setCalendarName( entity.getCalendarName() );
        simpleTriggerDTO.setFireInstanceId( entity.getFireInstanceId() );
        simpleTriggerDTO.setMisfireInstruction( entity.getMisfireInstruction() );
        simpleTriggerDTO.setPriority( entity.getPriority() );
        simpleTriggerDTO.setKey( entity.getKey() );
        simpleTriggerDTO.setStartTime( entity.getStartTime() );
        simpleTriggerDTO.setEndTime( entity.getEndTime() );
        simpleTriggerDTO.setNextFireTime( entity.getNextFireTime() );
        simpleTriggerDTO.setPreviousFireTime( entity.getPreviousFireTime() );
        simpleTriggerDTO.setRepeatCount( entity.getRepeatCount() );
        simpleTriggerDTO.setRepeatInterval( entity.getRepeatInterval() );
        simpleTriggerDTO.setTimesTriggered( entity.getTimesTriggered() );

        return simpleTriggerDTO;
    }
}
