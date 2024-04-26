package com.test.Users.util;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@PropertySource("classpath:config.properties")
public class AgeChecker {

    @Value("${age.limit}")
    private int ageLimit;

    public boolean isUserAdult(LocalDate birthDate){
        LocalDate today = LocalDate.now();
        long age = ChronoUnit.YEARS.between(birthDate, today);

        return age >= ageLimit;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }
}
