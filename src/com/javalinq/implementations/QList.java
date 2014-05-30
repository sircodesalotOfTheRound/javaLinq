package com.javalinq.implementations;


import com.javalinq.interfaces.QIterable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class QList<T> implements QIterable<T> {
    List<T> list = new ArrayList<>();

    public QList() {
    }

    public QList(Iterable<T> items) {
        for (T item : items) this.add(item);
    }

    @Override
    public Iterator<T> iterator() {
        return this.list.iterator();
    }

    public void add(T item) {
        this.list.add(item);
    }

    public int size() {
        return list.size();
    }

    @Override
    public long count() {
        return list.size();
    }
}
