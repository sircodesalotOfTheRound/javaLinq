package com.javalinq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class QList<T> implements QIterable<T> {
    List<T> list = new ArrayList<>();

    @Override
    public Iterator<T> iterator() { return this.list.iterator(); }

    public void add(T item) {
        this.list.add(item);
    }
}
