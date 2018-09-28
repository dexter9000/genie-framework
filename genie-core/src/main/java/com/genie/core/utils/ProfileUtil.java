package com.genie.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Created by meng013 on 2017/9/21.
 */
public class ProfileUtil {

    public static boolean haveProfile(Environment env, String profile){
        String profiles = env.getProperty("spring.profiles.active");
        if(StringUtils.isEmpty(profiles)){
            return false;
        }
        final String target = profile.toLowerCase();
        return Arrays.stream(profiles.toLowerCase().split(","))
            .anyMatch(p -> p.trim().equals(target));
    }
}
