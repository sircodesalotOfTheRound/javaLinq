package com.javalinq;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class QList<T> extends QIterable<T> {
    List<T> list = new ArrayList<>();

    @Override
    public Iterator<T> iterator() { return this.list.iterator(); }
}
