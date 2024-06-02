package com.zzz.JCTee;

/**
 * @author zzx
 */
public interface Parent {
    default void setId(String id) {
    }


    default String getId() {
        return "";
    }

}
