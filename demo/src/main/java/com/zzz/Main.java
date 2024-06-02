package com.zzz;

import com.zzz.JCTee.JCTreeDTO;
import com.zzz.Javapoet.JavapoetDTO;
import com.zzz.Javapoet.JavapoetDTOJavapoet;

/**
 * @author zzx
 */
public class Main {
    public static void main(String[] args) {
        JCTreeDTO jcTreeDTO = new JCTreeDTO();
        jcTreeDTO.setId("123");
        System.out.println(jcTreeDTO.getId());
        JavapoetDTOJavapoet javapoetDTOJavapoet = new JavapoetDTOJavapoet();
        javapoetDTOJavapoet.test(new JavapoetDTO());
    }
}
