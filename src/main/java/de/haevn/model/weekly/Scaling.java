package de.haevn.model.weekly;

import java.util.HashMap;
import java.util.Map;

public class Scaling {
    private static final Scaling INSTANCE = new Scaling();

    public static Scaling getInstance() {
        return INSTANCE;
    }

    private final Map<Integer, Double> scalingMap = new HashMap<>();

    private Scaling(){
        scalingMap.put(2, 1.0);
        scalingMap.put(3, 1.08);
        scalingMap.put(4, 1.17);
        scalingMap.put(5, 1.26);
        scalingMap.put(6, 1.36);
        scalingMap.put(7, 1.47);
        scalingMap.put(8, 1.59);
        scalingMap.put(9, 1.71);
        scalingMap.put(10, 1.85);
        scalingMap.put(11, 2.04);
        scalingMap.put(12, 2.24);
        scalingMap.put(13, 2.46);
        scalingMap.put(14, 2.71);
        scalingMap.put(15, 2.98);
        scalingMap.put(16, 3.28);
        scalingMap.put(17, 3.61);
        scalingMap.put(18, 3.97);
        scalingMap.put(19, 4.36);
        scalingMap.put(20, 4.80);
        scalingMap.put(21, 5.28);
        scalingMap.put(22, 5.81);
        scalingMap.put(23, 6.39);
        scalingMap.put(24, 7.03);
        scalingMap.put(25, 7.73);
        scalingMap.put(26, 8.50);
        scalingMap.put(27, 9.36);
        scalingMap.put(28, 10.29);
        scalingMap.put(29, 11.32);
        scalingMap.put(30, 12.45);

    }


    public double getScalarFor(int level){
        return scalingMap.getOrDefault(level, 1.0);
    }
}
