package com.zvosframework.schedule.core.handler.impl;

import com.zvosframework.schedule.core.handler.IJobHandler;

import java.lang.reflect.Method;

/**
 * @author xuxueli 2019-12-11 21:12:18
 */
public class MethodJobHandler extends IJobHandler {

    private final Object target;
    private final Method method;
    private Method initMethod;
    private Method destroyMethod;

    public MethodJobHandler(Object target, Method method, Method initMethod, Method destroyMethod) {
        this.target = target;
        this.method = method;

        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }

    @Override
    public void execute() throws Exception {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length > 0) {
            // get job param
            String jobParam = com.zvosframework.schedule.core.context.XxlJobContext.getXxlJobContext().getJobParam();
            Object[] args = new Object[paramTypes.length];
            
            // convert param to method arguments
            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> paramType = paramTypes[i];
                if (paramType == String.class) {
                    args[i] = jobParam;
                } else if (paramType == Integer.class || paramType == int.class) {
                    args[i] = jobParam != null ? Integer.parseInt(jobParam) : 0;
                } else if (paramType == Long.class || paramType == long.class) {
                    args[i] = jobParam != null ? Long.parseLong(jobParam) : 0L;
                } else if (paramType == Boolean.class || paramType == boolean.class) {
                    args[i] = jobParam != null ? Boolean.parseBoolean(jobParam) : false;
                } else if (paramType == Double.class || paramType == double.class) {
                    args[i] = jobParam != null ? Double.parseDouble(jobParam) : 0.0;
                } else if (paramType == Float.class || paramType == float.class) {
                    args[i] = jobParam != null ? Float.parseFloat(jobParam) : 0.0f;
                } else {
                    // for other types, try to use JSON conversion
                    try {
                        args[i] = com.xxl.tool.json.GsonTool.fromJson(jobParam, paramType);
                    } catch (Exception e) {
                        // if JSON conversion fails, use default value
                        args[i] = getDefaultValue(paramType);
                    }
                }
            }
            
            method.invoke(target, args);
        } else {
            method.invoke(target);
        }
    }
    
    /**
     * Get default value for primitive types
     */
    private Object getDefaultValue(Class<?> type) {
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == boolean.class) return false;
        if (type == double.class) return 0.0;
        if (type == float.class) return 0.0f;
        if (type == byte.class) return (byte) 0;
        if (type == char.class) return '\0';
        if (type == short.class) return (short) 0;
        return null; // for reference types
    }

    @Override
    public void init() throws Exception {
        if(initMethod != null) {
            initMethod.invoke(target);
        }
    }

    @Override
    public void destroy() throws Exception {
        if(destroyMethod != null) {
            destroyMethod.invoke(target);
        }
    }

    @Override
    public String toString() {
        return super.toString()+"["+ target.getClass() + "#" + method.getName() +"]";
    }
}
