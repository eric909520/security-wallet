package org.secwallet.core.util.iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionWrapperIterator<T extends Collection> implements Iterator<T> {
    private int index;
    private int size;

    private int batchCount = 50;
    private T collection;

    private Class<T> collectionClass;

    private List<T> list;

    public CollectionWrapperIterator(T collection) throws IllegalAccessException, InstantiationException {
        this(collection, 30);
    }

    public CollectionWrapperIterator(T collection, Integer batchCount) throws InstantiationException, IllegalAccessException {
        this.collection = collection;

        int size = size(collection);
        this.batchCount = batchCount == null ? this.batchCount : (batchCount > 0 ? batchCount : this.batchCount);

        if (size > 0) {
            this.collectionClass = size > 0 ? (Class<T>) collection.getClass() : null;

            this.list = new ArrayList(size / batchCount + 5);

            this.wrapper();
        }
    }

    private void wrapper() throws IllegalAccessException, InstantiationException {
        T set = this.collectionClass.newInstance();
        Iterator iterator = this.collection.iterator();

        while (iterator.hasNext()) {
            if (size(set) < batchCount) {
                set.add(iterator.next());
            } else {
                this.list.add(set);
                set = this.collectionClass.newInstance();
            }
        }
        if (size(set) > 0) this.list.add(set);

        this.index = 0;
        this.size = size(this.list);
    }

    @Override
    public boolean hasNext() {
        return this.index < this.size;
    }

    @Override
    public T next() {
        return this.list.get(this.index++);
    }

    private int size(Collection collection) {
        return collection == null ? 0 : collection.size();
    }
}