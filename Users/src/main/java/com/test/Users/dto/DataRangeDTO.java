package com.test.Users.dto;

import java.time.LocalDate;

public class DataRangeDTO {

    private LocalDate from;
    private LocalDate to;

    public DataRangeDTO(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
