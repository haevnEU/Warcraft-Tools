package de.haevn.model.recording;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class RecordEntry {
    private String name;
    private Date recordDate;
    private String logLink;
    private String videoLink;
    private String tags;

    @Override
    public String toString() {
        final String date = recordDate.getDay() + "/" + recordDate.getMonth() + "/" + recordDate.getYear();
        return name + " " + date;
    }
}
