package com.lishuaihua.servicemanager.util

import java.lang.reflect.InvocationTargetException

object RefIectUtil {
    fun invokeMethod(
        target: Any?, clazz: Class<*>, methodName: String?, paramTypes: Array<Class<*>?>,
        paramValues: Array<Any?>
    ): Any? {
        try {
            val method = clazz.getDeclaredMethod(methodName, *paramTypes)
            if (!method.isAccessible) {
                method.isAccessible = true
            }
            return method.invoke(target, *paramValues)
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

    fun getFieldObject(target: Any?, clazz: Class<*>, fieldName: String?): Any? {
        try {
            val field = clazz.getDeclaredField(fieldName)
            if (!field.isAccessible) {
                field.isAccessible = true
            }
            return field[target]
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}