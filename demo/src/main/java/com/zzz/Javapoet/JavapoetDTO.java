package com.zzz.Javapoet;

import com.zzz.JavapoetAno;

public class JavapoetDTO {
    private String name;

    @JavapoetAno
    private String id;

    public String setName(String name) {
        return name;
    }
}
