package it.gdhi.utils;

import java.util.List;
import java.util.function.Function;

public class ListUtils {
    public static <T> T findFirst(List<T> list, Function<T, Boolean> perdicate) {
        return list.stream().filter(perdicate::apply).findFirst().get();
    }
}
