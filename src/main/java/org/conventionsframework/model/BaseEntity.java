package org.conventionsframework.model;

import java.io.Serializable;

/**
 * User: Rafael M. Pestano
 */
public interface BaseEntity<T extends Serializable> {

    T getId();
}
