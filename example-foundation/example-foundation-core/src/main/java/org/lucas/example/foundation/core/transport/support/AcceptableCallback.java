package org.lucas.example.foundation.core.transport.support;

/**
 * @create: 2017-12-13
 * @description:
 */
@FunctionalInterface
public interface AcceptableCallback {

    default void onAcceptable(Readable r){

    }

    /**
     * 接收就绪
     */
    void onAcceptable();
}
