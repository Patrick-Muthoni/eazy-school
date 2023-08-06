package com.eazybytes.eazyschool.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Component("eazySchoolProps")
@Getter
@Setter
//@PropertySource(value = "classpath:application.properties")
@ConfigurationProperties(prefix = "eazyschool")
@Validated
public class EazySchoolProps {

    @Min(value = 5, message = "Minimum page size is 5")
    @Max(value = 25, message = "Maximum page size is 25")
    private int pageSize;
    private Map<String, String> contact;
    private List<String> branches;
}
