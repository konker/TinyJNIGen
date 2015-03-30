package com.luxvelocitas.jnigen;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.objectweb.asm.Type;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 *
 */
public class JNIGen {
    public static enum OutputType { C, CXX };

    public static final String USAGE = "JNIGen [options] <classes>";
    private static final String TEMPLATE_DIR = "templates";
    private static final String INCLUDE_DIR = "include";

    private static final Map<String, String> sTypeMap;
    private static final Map<String, String> sReturnTypeMap;
    private static final Map<String, String> sIdentityValueMap;
    private static final Map<String, String> sMangleMap;
    private static final Map<String, String> sCallMethodMap;
    private static final Map<String, String> sStaticCallMethodMap;

    static {
        // -------------------------------------------------
        // Type map
        sTypeMap = new HashMap<String, String>();
        sTypeMap.put("class java.lang.String", "const char*");
        sTypeMap.put("class java.lang.Char", "char");
        sTypeMap.put("class java.lang.Byte", "char");
        sTypeMap.put("class java.lang.Short", "int");
        sTypeMap.put("class java.lang.Integer", "int");
        sTypeMap.put("class java.lang.Long", "long");
        sTypeMap.put("class java.lang.Float", "float");
        sTypeMap.put("class java.lang.Double", "double");
        sTypeMap.put("class java.lang.Boolean", "bool");
        sTypeMap.put("char", "char");
        sTypeMap.put("byte", "char");
        sTypeMap.put("short", "int");
        sTypeMap.put("int", "int");
        sTypeMap.put("long", "long");
        sTypeMap.put("float", "float");
        sTypeMap.put("double", "double");
        sTypeMap.put("boolean", "bool");
        sTypeMap.put("class", "jobject");

        // -------------------------------------------------
        // Return type map
        sReturnTypeMap = new HashMap<String, String>();
        sReturnTypeMap.put("class java.lang.String", "jstring");
        sReturnTypeMap.put("class java.lang.Char", "char");
        sReturnTypeMap.put("class java.lang.Byte", "char");
        sReturnTypeMap.put("class java.lang.Short", "int");
        sReturnTypeMap.put("class java.lang.Integer", "int");
        sReturnTypeMap.put("class java.lang.Long", "long");
        sReturnTypeMap.put("class java.lang.Float", "float");
        sReturnTypeMap.put("class java.lang.Double", "double");
        sReturnTypeMap.put("class java.lang.Boolean", "bool");
        sReturnTypeMap.put("char", "char");
        sReturnTypeMap.put("byte", "char");
        sReturnTypeMap.put("short", "int");
        sReturnTypeMap.put("int", "int");
        sReturnTypeMap.put("long", "long");
        sReturnTypeMap.put("float", "float");
        sReturnTypeMap.put("double", "double");
        sReturnTypeMap.put("boolean", "bool");
        sReturnTypeMap.put("class", "jobject");

        // -------------------------------------------------
        // Identity value map
        sIdentityValueMap = new HashMap<String, String>();
        sIdentityValueMap.put("class java.lang.String", "NULL");
        sIdentityValueMap.put("class java.lang.Char", "0");
        sIdentityValueMap.put("class java.lang.Byte", "0");
        sIdentityValueMap.put("class java.lang.Short", "0");
        sIdentityValueMap.put("class java.lang.Integer", "0");
        sIdentityValueMap.put("class java.lang.Long", "0");
        sIdentityValueMap.put("class java.lang.Float", "0");
        sIdentityValueMap.put("class java.lang.Double", "0");
        sIdentityValueMap.put("class java.lang.Boolean", "0");
        sIdentityValueMap.put("char", "0");
        sIdentityValueMap.put("byte", "0");
        sIdentityValueMap.put("short", "0");
        sIdentityValueMap.put("int", "0");
        sIdentityValueMap.put("long", "0");
        sIdentityValueMap.put("float", "0");
        sIdentityValueMap.put("double", "0");
        sIdentityValueMap.put("boolean", "0");
        sIdentityValueMap.put("class", "NULL");

        // -------------------------------------------------
        // Mangle map
        sMangleMap = new HashMap<String, String>();
        sMangleMap.put("class java.lang.String", "s");
        sMangleMap.put("class java.lang.Char", "c");
        sMangleMap.put("class java.lang.Byte", "b");
        sMangleMap.put("class java.lang.Short", "s");
        sMangleMap.put("class java.lang.Integer", "i");
        sMangleMap.put("class java.lang.Long", "l");
        sMangleMap.put("class java.lang.Float", "f");
        sMangleMap.put("class java.lang.Double", "d");
        sMangleMap.put("class java.lang.Boolean", "z");
        sMangleMap.put("char", "c");
        sMangleMap.put("byte", "b");
        sMangleMap.put("short", "s");
        sMangleMap.put("int", "i");
        sMangleMap.put("long", "l");
        sMangleMap.put("float", "f");
        sMangleMap.put("double", "d");
        sMangleMap.put("boolean", "z");
        sMangleMap.put("class", "o");

        // -------------------------------------------------
        // Call method map
        sCallMethodMap = new HashMap<String, String>();
        sCallMethodMap.put("void", "CallVoidMethod");
        sCallMethodMap.put("class java.lang.String", "CallObjectMethod");
        sCallMethodMap.put("class java.lang.Char", "CallCharMethod");
        sCallMethodMap.put("class java.lang.Byte", "CallByteMethod");
        sCallMethodMap.put("class java.lang.Short", "CallShortMethod");
        sCallMethodMap.put("class java.lang.Integer", "CallIntMethod");
        sCallMethodMap.put("class java.lang.Long", "CallLongMethod");
        sCallMethodMap.put("class java.lang.Float", "CallFloatMethod");
        sCallMethodMap.put("class java.lang.Double", "CallDoubleMethod");
        sCallMethodMap.put("class java.lang.Boolean", "CallBooleanMethod");
        sCallMethodMap.put("char", "CallCharMethod");
        sCallMethodMap.put("byte", "CallByteMethod");
        sCallMethodMap.put("short", "CallShortMethod");
        sCallMethodMap.put("int", "CallIntMethod");
        sCallMethodMap.put("long", "CallLongMethod");
        sCallMethodMap.put("float", "CallFloatMethod");
        sCallMethodMap.put("double", "CallDoubleMethod");
        sCallMethodMap.put("boolean", "CallBooleanMethod");
        sCallMethodMap.put("class", "CallObjectMethod");

        // -------------------------------------------------
        // Static call method map
        sStaticCallMethodMap = new HashMap<String, String>();
        sStaticCallMethodMap.put("void", "CallStaticVoidMethod");
        sStaticCallMethodMap.put("class java.lang.String", "CallStaticObjectMethod");
        sStaticCallMethodMap.put("class java.lang.Char", "CallStaticCharMethod");
        sStaticCallMethodMap.put("class java.lang.Byte", "CallStaticByteMethod");
        sStaticCallMethodMap.put("class java.lang.Short", "CallStaticShortMethod");
        sStaticCallMethodMap.put("class java.lang.Integer", "CallStaticIntMethod");
        sStaticCallMethodMap.put("class java.lang.Long", "CallStaticLongMethod");
        sStaticCallMethodMap.put("class java.lang.Float", "CallStaticFloatMethod");
        sStaticCallMethodMap.put("class java.lang.Double", "CallStaticDoubleMethod");
        sStaticCallMethodMap.put("class java.lang.Boolean", "CallStaticBooleanMethod");
        sStaticCallMethodMap.put("char", "CallStaticCharMethod");
        sStaticCallMethodMap.put("byte", "CallStaticByteMethod");
        sStaticCallMethodMap.put("short", "CallStaticShortMethod");
        sStaticCallMethodMap.put("int", "CallStaticIntMethod");
        sStaticCallMethodMap.put("long", "CallStaticLongMethod");
        sStaticCallMethodMap.put("float", "CallStaticFloatMethod");
        sStaticCallMethodMap.put("double", "CallStaticDoubleMethod");
        sStaticCallMethodMap.put("boolean", "CallStaticBooleanMethod");
        sStaticCallMethodMap.put("class", "CallStaticObjectMethod");
    }

    private static String mapType(String t) {
        if (sTypeMap.containsKey(t)) {
            return sTypeMap.get(t);
        }
        else if (t.startsWith("class")) {
            return sTypeMap.get("class");
        }
        return t;
    }

    private static String mapReturnType(String t) {
        if (sReturnTypeMap.containsKey(t)) {
            return sReturnTypeMap.get(t);
        }
        else if (t.startsWith("class")) {
            return sReturnTypeMap.get("class");
        }
        return t;
    }

    private static String mapIdentityValue(String t) {
        if (sIdentityValueMap.containsKey(t)) {
            return sIdentityValueMap.get(t);
        }
        else if (t.startsWith("class")) {
            return sIdentityValueMap.get("class");
        }
        return t;
    }

    private static String mapMangleType(String t) {
        if (sMangleMap.containsKey(t)) {
            return sMangleMap.get(t);
        }
        else if (t.startsWith("class")) {
            return sMangleMap.get("class");
        }
        return t;
    }

    private static String mapCallMethod(String t) {
        if (sCallMethodMap.containsKey(t)) {
            return sCallMethodMap.get(t);
        }
        else if (t.startsWith("class")) {
            return sCallMethodMap.get("class");
        }
        throw new RuntimeException("Could not resolve call method for type: " + t);
    }

    private static String mapStaticCallMethod(String t) {
        if (sStaticCallMethodMap.containsKey(t)) {
            return sStaticCallMethodMap.get(t);
        }
        else if (t.startsWith("class")) {
            return sStaticCallMethodMap.get("class");
        }
        throw new RuntimeException("Could not resolve static call method for type: " + t);
    }

    @Option(name="-h",usage="help")
    private boolean mShowHelp = false;

    @Option(name="-v",usage="verbose")
    private boolean mVerbose = false;

    @Option(name="-d",usage="output directory",metaVar="<dir>")
    private File mOutDirC = new File(".");
    private File mOutDirH = new File(mOutDirC, INCLUDE_DIR);

    @Option(name="-classpath",usage="classpath",metaVar="<path>")
    private String mClasspath = ".";

    // receives other command line parameters
    @Argument
    private List<String> mArguments = new ArrayList<String>();

    private Template mTemplateC_H;
    private Template mTemplateC_C;
    private Template mTemplateCXX_H;
    private Template mTemplateCXX_C;

    public static void main(String[] args) {
        new JNIGen().exec(args);
    }

    public void exec(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            // parse the arguments.
            parser.parseArgument(args);

            if (mShowHelp) {
                System.err.println(USAGE);
                parser.printUsage(System.err);
                System.err.println();
                return;
            }

            if (mArguments.isEmpty()) {
                throw new CmdLineException(parser,"No Java classes given", null);
            }

            // Formulate the .h output directory
            mOutDirH = new File(mOutDirC, INCLUDE_DIR);
        }
        catch(CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println();
            System.err.println(USAGE);

            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            System.exit(3);
        }

        // -------------------------------------------------------------------
        // Load in the templates
        try {
            File basePath = new File(TEMPLATE_DIR);

            TemplateLoader loader = new FileTemplateLoader(basePath);
            Handlebars handlebars = new Handlebars(loader);
            mTemplateC_H = handlebars.compile("C/template_h");
            mTemplateC_C = handlebars.compile("C/template_c");
            mTemplateCXX_H = handlebars.compile("CXX/template_h");
            mTemplateCXX_C = handlebars.compile("CXX/template_c");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(4);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(5);
        }

        // -------------------------------------------------------------------
        // Create class loader
        List<URL> urlList = new ArrayList<URL>();
        try {
            String[] paths = mClasspath.split(":");

            for (String p : paths) {
                URL url = new File(p).toURI().toURL();
                urlList.add(url);
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(2);
        }

        URL[] urls = new URL[urlList.size()];
        urls = urlList.toArray(urls);
        ClassLoader classLoader = new URLClassLoader(urls);

        // -------------------------------------------------------------------
        // Search for annotated methods
        List<Method> methodCache = new ArrayList<Method>();

        for (String s : mArguments) {
            System.out.println("JNIGen: " + s);

            try {
                Class c = classLoader.loadClass(s);

                // Get all methods
                Method[] methods = c.getMethods();

                for (Method m : methods) {
                    Annotation[] annotations = m.getAnnotations();

                    for (Annotation a : annotations) {
                        if (a instanceof NativeCallable) {
                            methodCache.add(m);
                        }
                    }
                }

                if (!methodCache.isEmpty()) {
                    System.out.println("Found " + methodCache.size() + " NativeCallable method(s) for: " + c.getName());

                    Context context = createTemplateContext(c, methodCache);

                    generateCode(mTemplateC_H, context, mOutDirH, context.get("fname") + "_extern." + context.get("h_ext"));
                    generateCode(mTemplateC_C, context, mOutDirC, context.get("fname") + "_extern." + context.get("c_ext"));
                    generateCode(mTemplateCXX_H, context, mOutDirH, context.get("fname") + "." + context.get("h_ext"));
                    generateCode(mTemplateCXX_C, context, mOutDirC, context.get("fname") + "." + context.get("c_ext"));
                }

                methodCache.clear();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        System.exit(0);
    }

    // Create the template context
    public Context createTemplateContext(Class<?> c, List<Method> methods) {
        Map<String, Object> model = new HashMap<String, Object>();

        List<MethodContext> methodContexts = new ArrayList<MethodContext>();
        for (Method m : methods) {
            MethodContext mc = new MethodContext();

            mc.name = m.getName();
            mc.toString = m.toString();
            mc.returnType = m.getReturnType();
            mc.returnTypeMapped = JNIGen.mapReturnType(mc.returnType.toString());
            mc.returnTypeIdentityValue = JNIGen.mapIdentityValue(mc.returnType.toString());
            mc.returnTypeIsString = (mc.returnType.toString().contains("java.lang.String"));
            mc.isStatic = (m.toString().contains("static "));
            mc.parameterTypes = new ArrayList<ParameterContext>();
            mc.classPath = Type.getInternalName(c);
            mc.JNISignature = Type.getMethodDescriptor(m);
            mc.isVoid = (mc.returnTypeMapped.equals("void"));
            mc.mangle = "";

            if (mc.isStatic) {
                mc.callMethod = mapStaticCallMethod(mc.returnType.toString());
            }
            else {
                mc.callMethod = mapCallMethod(mc.returnType.toString());
            }

            Class[] parameterTypes = m.getParameterTypes();
            for (Class p : parameterTypes) {
                ParameterContext pc = new ParameterContext();
                pc.type = p;
                pc.typeMapped = JNIGen.mapType(pc.type.toString());
                pc.toString = p.toString();
                pc.isString = pc.type.toString().contains("java.lang.String");

                mc.parameterTypes.add(pc);
            }

            methodContexts.add(mc);
        }

        mangleMethods(methodContexts);

        model.put("class", c);
        model.put("methods", methodContexts);
        model.put("iname", c.getCanonicalName().replaceAll("\\.", "_"));
        model.put("fname", model.get("iname") + "_JAVA");
        model.put("h_ext", "h");
        model.put("c_ext", "cpp");

        Context context = Context
                .newBuilder(model)
                .resolver(
                        MapValueResolver.INSTANCE,
                        JavaBeanValueResolver.INSTANCE,
                        FieldValueResolver.INSTANCE
                ).build();

        return context;
    }

    private void mangleMethods(List<MethodContext> methodContexts) {
        Map<String, Integer> mangleCountMap = new HashMap<String, Integer>();

        // Loop through the list of methods and mangle those which are repeated
        for (int i=0; i<methodContexts.size(); i++) {
            MethodContext mci = methodContexts.get(i);

            boolean doMangle = false;

            for (int j=i+1; j<methodContexts.size(); j++) {
                MethodContext mcj = methodContexts.get(j);

                if (mcj.name.equals(mci.name) && mcj.mangle.equals("")) {
                    mcj.mangle = getMangle(mcj.name, mcj.parameterTypes, mangleCountMap);
                    doMangle = true;
                }
            }

            if (doMangle && mci.mangle.equals("")) {
                mci.mangle = getMangle(mci.name, mci.parameterTypes, mangleCountMap);
            }
        }
    }

    private String getMangle(String methodName, List<ParameterContext> parameterTypes, Map<String, Integer> mangleCountMap) {
        StringBuffer ret = new StringBuffer("_");
        for (ParameterContext pc : parameterTypes) {
            ret.append(JNIGen.mapMangleType(pc.toString));
        }

        String mangle = ret.toString();
        String mangleKey = methodName + mangle;
        String suffix = "";
        if (mangleCountMap.containsKey(mangleKey)) {
            Integer i = mangleCountMap.get(mangleKey);
            i++;
            mangleCountMap.put(mangleKey, i);

            suffix = String.valueOf(i);
        }
        else {
            mangleCountMap.put(mangleKey, 1);
        }
        return (mangle + suffix);
    }

    // -------------------------------------------------------------------
    // Generate code!
    public void generateCode(Template template, Context context, File outDir, String outName) {
        try {
            // Formulate output path and make sure intermediate directories exist
            outDir.mkdirs();

            // Formulate destination file
            File outFile = new File(outDir, outName);

            // Open a writer and execute the template
            Writer writer = new FileWriter(outFile);
            writer.write(template.apply(context));

            // Clean up writer
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
