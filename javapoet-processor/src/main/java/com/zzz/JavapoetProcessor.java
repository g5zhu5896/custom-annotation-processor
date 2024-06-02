package com.zzz;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * 使用javapoet 生成java代码
 *
 * @author zzx
 */
@SupportedAnnotationTypes("com.zzz.JavapoetAno")
public class JavapoetProcessor extends DefaultProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(JavapoetAno.class);
        if (elements == null || elements.size() == 0) {
            return true;
        }
        for (Element element : elements) {
            if (element.getKind() != ElementKind.FIELD) {
                messager.printMessage(Diagnostic.Kind.ERROR, "is not a FIELD", element);
            }


            TypeElement clazz = (TypeElement) element.getEnclosingElement();

            String packageName = elementUtils.getPackageOf(clazz).asType().toString();
            String className = clazz.getSimpleName().toString();
            String name = element.getSimpleName().toString();

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("test")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(ClassName.get(packageName, className), "arg")
                    .addStatement("String fieldName = $S", name)
                    .addStatement("String clazz= $S", element.asType());

            TypeSpec helloWorld = TypeSpec.classBuilder(className + "Javapoet")
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, helloWorld)
                    .build();
            try {
                javaFile.writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
