package de.haevn.model.recording;

import lombok.Data;

import java.util.Date;

@Data
public class RecordEntry {
    private String name;
    private Date recordDate;
    private String logLink;
    private String videoLink;
    private String tags;

    @Override
    public String toString(){
        return name + " " + recordDate;
    }
}
