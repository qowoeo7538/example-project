package org.lucas.example.foundation.basic.kata.function.support;

import java.util.Iterator;

/**
 * @create: 2018-01-09
 * @description:
 */
@FunctionalInterface
public interface ItResult<E extends Iterator<K>, K> {

    E setIterator();

    default K skip(int i) {
        Iterator<K> iterator = setIterator();
        for (; i > 1 && iterator.hasNext(); i -= 1) {
            iterator.next();
        }
        return iterator.next();
    }

}
