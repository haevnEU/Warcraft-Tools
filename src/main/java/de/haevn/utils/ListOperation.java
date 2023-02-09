package de.haevn.utils;

import java.util.Comparator;
import java.util.List;

public final class ListOperation {
    private ListOperation() {
    }

    public static <T> boolean isContentEqual(List<T> first, List<T> second, Comparator<T> comparator) {

        if (null == first || null == second || first.size() != second.size()) {
            return false;
        }

        int idMatch = 0;
        for (int i = 0; i < first.size(); i++) {
            if (comparator.compare(first.get(i), second.get(i)) == 0) {
                idMatch++;
            }
        }

        return idMatch == 4;
    }
}
