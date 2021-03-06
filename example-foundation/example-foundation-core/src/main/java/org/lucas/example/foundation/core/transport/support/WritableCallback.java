package org.lucas.example.foundation.core.transport.support;

/**
 * @create: 2017-12-13
 * @description:
 */
@FunctionalInterface
public interface WritableCallback<T> {
    /**
     * 写入就绪
     */
    T onWritable();
}
