package com.tbaumeist.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/*
 * A list that will allow an access past the end to wrap around to the beginning again
 */
public class CircleList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public void add(int paramInt, T paramE) {
        super.add(convertIndex(paramInt), paramE);
    }

    @Override
    public T get(int paramInt) {
        return super.get(convertIndex(paramInt));
    }

    @Override
    public ListIterator<T> listIterator(int paramInt) {
        return super.listIterator(convertIndex(paramInt));
    }

    @Override
    public T remove(int paramInt) {
        return super.remove(convertIndex(paramInt));
    }

    @Override
    public T set(int paramInt, T paramE) {
        return super.set(convertIndex(paramInt), paramE);
    }

    @Override
    public List<T> subList(int paramInt1, int paramInt2) {
        return super.subList(convertIndex(paramInt1), convertIndex(paramInt2));
    }

    /*
     * Does all the work of translating index into regular list index
     */
    private int convertIndex(int index) {
        while (index < 0)
            index += size();

        return index % size();
    }
}
