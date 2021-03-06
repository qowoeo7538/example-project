package org.lucas.example.common.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by joy on 17-2-12.
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritedAnnotation {
    String value();
}
