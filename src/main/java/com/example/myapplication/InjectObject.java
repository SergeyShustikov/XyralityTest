package com.example.myapplication;

/**
 * @autor Sergey Shustikov
 * (pandarium.shustikov@gmail.com)
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Sergey Shustikov (sergey.shustikov@youshido.com) at 2015.
 */
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface InjectObject
{

}
