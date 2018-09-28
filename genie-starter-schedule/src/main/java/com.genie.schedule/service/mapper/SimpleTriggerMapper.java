package com.genie.schedule.service.mapper;

import com.genie.schedule.service.dto.SimpleTriggerDTO;
import org.mapstruct.Mapper;
import org.quartz.impl.triggers.SimpleTriggerImpl;

/**
 * Created by meng013 on 2017/11/7.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SimpleTriggerMapper  {

    SimpleTriggerDTO toDto(SimpleTriggerImpl entity);

}
