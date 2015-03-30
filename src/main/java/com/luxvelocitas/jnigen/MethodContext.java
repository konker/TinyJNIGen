package com.luxvelocitas.jnigen;

import java.util.List;

/**
 *
 */
public class MethodContext {
    public String name;
    public String toString;
    public Class returnType;
    public String returnTypeMapped;
    public List<ParameterContext> parameterTypes;
    public boolean isStatic;
    public String callMethod;
    public String classPath;
    public String JNISignature;
    public boolean isVoid;
    public String mangle;
    public String returnTypeIdentityValue;
    public boolean returnTypeIsString;

    public String toString() {
        return toString;
    }
}
