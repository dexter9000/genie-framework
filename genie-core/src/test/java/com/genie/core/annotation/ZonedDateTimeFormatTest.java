package com.genie.core.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.genie.core.entity.Program;
import com.genie.core.utils.DateUtil;
import org.junit.Test;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ZonedDateTimeFormatTest {

    @Test
    public void test() throws IOException {
        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
        String str = "{\"name\":\"0\"," +
            "\"startTime\":\"2018-07-29T16:00:00.000Z\"," +
            "\"endTime\":\"2018-08-31T16:00:00.000Z\"}";
        Program dto = om.readValue(str, Program.class);
        System.out.println(dto);

        DateUtil.parseZonedDateTime("2018-07-29T16:00:00.000Z", "UTC");
        assertThat(dto).hasFieldOrPropertyWithValue("startTime", ZonedDateTime.of(2018,7,29,16,0,0,0, ZoneId.ofOffset("UTC",ZoneOffset.UTC)));
    }

}
