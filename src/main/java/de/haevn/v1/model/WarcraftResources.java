package de.haevn.v1.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WarcraftResources {
    private List<Resource> mythicplus = new ArrayList<>();
    private List<Resource> raid = new ArrayList<>();
    private List<Resource> other = new ArrayList<>();

    @Data
    public static class Resource {
        private String name;
        private String url;
    }
}
