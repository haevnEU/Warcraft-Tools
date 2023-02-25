package de.haevn.model.recording;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.format(recordDate);
        return name + " " + df.format(recordDate);
    }
}
