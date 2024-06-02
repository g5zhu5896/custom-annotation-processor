package com.zzz.StrGen;

import com.zzz.StrGenAno;

public class StrGenDTO {
    private String name;

    private String id;

    @StrGenAno
    public String setName(String name) {
        return name;
    }
}
