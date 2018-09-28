package com.genie.schedule.service.mapper;

import com.genie.schedule.service.dto.CronTriggerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * Created by meng013 on 2017/11/7.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CronTriggerMapper {

    @Mappings(@Mapping(target = "cronExpression", ignore = true))
    CronTriggerImpl toEntity(CronTriggerDTO dto);

    CronTriggerDTO toDto(CronTriggerImpl entity);
}
