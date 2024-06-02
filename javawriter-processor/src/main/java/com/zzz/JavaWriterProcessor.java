package com.zzz;

import com.squareup.javawriter.JavaWriter;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.*;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author zzx
 */
@SupportedAnnotationTypes("com.zzz.JavaWriterAno")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class JavaWriterProcessor extends DefaultProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            // 标识符
            boolean isClass = false;
            // 类的全限定名
            String classAllName = null;
            // 返回被注释的节点
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(JavaWriterAno.class);
            Element element = null;
            for (Element e : elements) {
                // 如果注释在类上
                if (e.getKind() == ElementKind.CLASS && e instanceof TypeElement) {
                    TypeElement t = (TypeElement) e;
                    isClass = true;
                    classAllName = t.getQualifiedName().toString();
                    element = t;
                    break;
                }
            }
            // 未在类上使用注释则直接返回，返回false停止编译
            if (!isClass) {
                return true;
            }
            // 返回类内的所有节点
            List<? extends Element> enclosedElements = element.getEnclosedElements();
            // 保存字段的集合
            Map<TypeMirror, Name> fieldMap = new HashMap<>();
            for (Element ele : enclosedElements) {
                if (ele.getKind() == ElementKind.FIELD) {
                    //字段的类型
                    TypeMirror typeMirror = ele.asType();
                    //字段的名称
                    Name simpleName = ele.getSimpleName();
                    fieldMap.put(typeMirror, simpleName);
                }
            }
            classAllName += "JavaWriter";
            // 生成一个Java源文件
            JavaFileObject sourceFile = filer.createSourceFile(classAllName);
            // 写入代码
            createSourceFile(classAllName, fieldMap, sourceFile.openWriter());
            // 手动编译
//            compile(sourceFile.toUri().getPath());
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "-----完成自动生成源代码");
        return true;
    }

    private void createSourceFile(String className, Map<TypeMirror, Name> fieldMap, Writer writer) throws IOException {
        // 生成源代码
        JavaWriter jw = new JavaWriter(writer);
        jw.emitPackage(getPackage(className));
        jw.beginType(getClassName(className), "class", EnumSet.of(Modifier.PUBLIC));
        for (Map.Entry<TypeMirror, Name> map : fieldMap.entrySet()) {
            String type = map.getKey().toString();
            String name = map.getValue().toString();
            //字段
            jw.emitField(type, name, EnumSet.of(Modifier.PRIVATE));
        }
        for (Map.Entry<TypeMirror, Name> map : fieldMap.entrySet()) {
            String type = map.getKey().toString();
            String name = map.getValue().toString();
            //getter
            jw.beginMethod(type, "get" + humpString(name), EnumSet.of(Modifier.PUBLIC))
                    .emitStatement("return " + name)
                    .endMethod();
            //setter
            jw.beginMethod("void", "set" + humpString(name), EnumSet.of(Modifier.PUBLIC), type, "arg")
                    .emitStatement("this." + name + " = arg")
                    .endMethod();
        }
        jw.endType().close();
    }

    /**
     * 编译文件
     *
     * @param path
     * @throws IOException
     */
    private void compile(String path) throws IOException {
        //拿到编译器
        JavaCompiler complier = ToolProvider.getSystemJavaCompiler();
        //文件管理者
        StandardJavaFileManager fileMgr =
                complier.getStandardFileManager(null, null, null);
        //获取文件
        Iterable units = fileMgr.getJavaFileObjects(path);
        //编译任务
        JavaCompiler.CompilationTask t = complier.getTask(null, fileMgr, null, null, null, units);
        //进行编译
        t.call();
        fileMgr.close();
    }

    /**
     * 驼峰命名
     *
     * @param name
     * @return
     */
    private String humpString(String name) {
        String result = name;
        if (name.length() == 1) {
            result = name.toUpperCase();
        }
        if (name.length() > 1) {
            result = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return result;
    }

    /**
     * 读取类名
     *
     * @param name
     * @return
     */
    private String getClassName(String name) {
        String result = name;
        if (name.contains(".")) {
            result = name.substring(name.lastIndexOf(".") + 1);
        }
        return result;
    }

    /**
     * 读取包名
     *
     * @param name
     * @return
     */
    private String getPackage(String name) {
        String result = name;
        if (name.contains(".")) {
            result = name.substring(0, name.lastIndexOf("."));
        } else {
            result = "";
        }
        return result;
    }
}