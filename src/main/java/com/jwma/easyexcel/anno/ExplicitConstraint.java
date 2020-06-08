package com.jwma.easyexcel.anno;

import java.lang.annotation.*;

/**
 * @author luolh
 * @version 1.0
 * @since 2020/6/2 11:38
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExplicitConstraint {
    //定义固定下拉内容
    String[] dropDowns()default {};
}
