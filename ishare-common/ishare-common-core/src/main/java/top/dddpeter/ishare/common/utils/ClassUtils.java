/**
 * ClassUtil.java
 * Created at 2019-12-06
 * Created by dddpeter
 * Copyright (C) 2019 hqins.com, All rights reserved.
 */
package top.dddpeter.ishare.common.utils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 描述 : ClassUtil
 *
 * @author dddpeter
 */
public class ClassUtils {

    /**
     * 描述 : 私有化构造函数
     */
    private ClassUtils() {

    }

    /**
     * 描述 : 获取在指定包下某个class的所有非抽象子类
     *
     * @param parentClass 父类
     * @param packagePath 指定包
     * @param <E>         泛型类
     * @return 列表
     * @throws ClassNotFoundException 异常
     */
    public static <E> List<Class<E>> getSubClasses(final Class<E> parentClass,
                                                   final String packagePath) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(parentClass));
        Set<BeanDefinition> components = provider.findCandidateComponents(packagePath);
        List<Class<E>> subClasses = new ArrayList<>();
        for (BeanDefinition component : components) {
            @SuppressWarnings("unchecked")
            Class<E> cls = (Class<E>) Class.forName(component.getBeanClassName());
            if (Modifier.isAbstract(cls.getModifiers())) {
                continue;
            }
            subClasses.add(cls);
        }
        return subClasses;
    }

}
