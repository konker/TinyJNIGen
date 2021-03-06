/*! DO NOT EDIT THIS FILE: GENERATED BY JNIGen */

#ifndef _JNI_GEN_com_morningwoodsoftware_datautils_DataBundle_h_
#define _JNI_GEN_com_morningwoodsoftware_datautils_DataBundle_h_

/**
 */

#include <jni.h>


class com_morningwoodsoftware_datautils_DataBundle
{
public:
    com_morningwoodsoftware_datautils_DataBundle(JavaVM *vm);
    ~com_morningwoodsoftware_datautils_DataBundle();


    /* public java.lang.String com.morningwoodsoftware.datautils.DataBundle.getString(java.lang.String,java.lang.String) */
    const char* getString(jobject thiz,const char* p0,const char* p1);

    /* public java.lang.String com.morningwoodsoftware.datautils.DataBundle.getString(java.lang.String) */
    const char* getString(jobject thiz,const char* p0);

    /* public com.morningwoodsoftware.datautils.DataBundle com.morningwoodsoftware.datautils.DataBundle.getBundle(java.lang.String) */
    jobject getBundle(jobject thiz,const char* p0);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putString(java.lang.String,java.lang.String) */
    void putString(jobject thiz,const char* p0,const char* p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putDataBundle(java.lang.String,com.morningwoodsoftware.datautils.DataBundle) */
    void putDataBundle(jobject thiz,const char* p0,jobject p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.remove(java.lang.String) */
    void remove(jobject thiz,const char* p0);

    /* public java.lang.Object com.morningwoodsoftware.datautils.DataBundle.get(java.lang.String) */
    jobject get(jobject thiz,const char* p0);

    /* public synchronized java.lang.String com.morningwoodsoftware.datautils.DataBundle.toString() */
    const char* toString(jobject thiz);

    /* public boolean com.morningwoodsoftware.datautils.DataBundle.getBoolean(java.lang.String) */
    bool getBoolean(jobject thiz,const char* p0);

    /* public boolean com.morningwoodsoftware.datautils.DataBundle.getBoolean(java.lang.String,boolean) */
    bool getBoolean(jobject thiz,const char* p0,bool p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putBoolean(java.lang.String,boolean) */
    void putBoolean(jobject thiz,const char* p0,bool p1);

    /* public java.lang.Byte com.morningwoodsoftware.datautils.DataBundle.getByte(java.lang.String,byte) */
    char getByte(jobject thiz,const char* p0,char p1);

    /* public byte com.morningwoodsoftware.datautils.DataBundle.getByte(java.lang.String) */
    char getByte(jobject thiz,const char* p0);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putByte(java.lang.String,byte) */
    void putByte(jobject thiz,const char* p0,char p1);

    /* public short com.morningwoodsoftware.datautils.DataBundle.getShort(java.lang.String) */
    int getShort(jobject thiz,const char* p0);

    /* public short com.morningwoodsoftware.datautils.DataBundle.getShort(java.lang.String,short) */
    int getShort(jobject thiz,const char* p0,int p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putShort(java.lang.String,short) */
    void putShort(jobject thiz,const char* p0,int p1);

    /* public char com.morningwoodsoftware.datautils.DataBundle.getChar(java.lang.String) */
    char getChar(jobject thiz,const char* p0);

    /* public char com.morningwoodsoftware.datautils.DataBundle.getChar(java.lang.String,char) */
    char getChar(jobject thiz,const char* p0,char p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putChar(java.lang.String,char) */
    void putChar(jobject thiz,const char* p0,char p1);

    /* public int com.morningwoodsoftware.datautils.DataBundle.getInt(java.lang.String) */
    int getInt(jobject thiz,const char* p0);

    /* public int com.morningwoodsoftware.datautils.DataBundle.getInt(java.lang.String,int) */
    int getInt(jobject thiz,const char* p0,int p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putInt(java.lang.String,int) */
    void putInt(jobject thiz,const char* p0,int p1);

    /* public long com.morningwoodsoftware.datautils.DataBundle.getLong(java.lang.String) */
    long getLong(jobject thiz,const char* p0);

    /* public long com.morningwoodsoftware.datautils.DataBundle.getLong(java.lang.String,long) */
    long getLong(jobject thiz,const char* p0,long p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putLong(java.lang.String,long) */
    void putLong(jobject thiz,const char* p0,long p1);

    /* public float com.morningwoodsoftware.datautils.DataBundle.getFloat(java.lang.String) */
    float getFloat(jobject thiz,const char* p0);

    /* public float com.morningwoodsoftware.datautils.DataBundle.getFloat(java.lang.String,float) */
    float getFloat(jobject thiz,const char* p0,float p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putFloat(java.lang.String,float) */
    void putFloat(jobject thiz,const char* p0,float p1);

    /* public double com.morningwoodsoftware.datautils.DataBundle.getDouble(java.lang.String,double) */
    double getDouble(jobject thiz,const char* p0,double p1);

    /* public double com.morningwoodsoftware.datautils.DataBundle.getDouble(java.lang.String) */
    double getDouble(jobject thiz,const char* p0);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putDouble(java.lang.String,double) */
    void putDouble(jobject thiz,const char* p0,double p1);

    /* public void com.morningwoodsoftware.datautils.DataBundle.clear() */
    void clear(jobject thiz);

    /* public boolean com.morningwoodsoftware.datautils.DataBundle.isEmpty() */
    bool isEmpty(jobject thiz);

    /* public int com.morningwoodsoftware.datautils.DataBundle.size() */
    int size(jobject thiz);

    /* public void com.morningwoodsoftware.datautils.DataBundle.putAll(com.morningwoodsoftware.datautils.DataBundle) */
    void putAll(jobject thiz,jobject p0);

    /* public boolean com.morningwoodsoftware.datautils.DataBundle.containsKey(java.lang.String) */
    bool containsKey(jobject thiz,const char* p0);

    /* public static com.morningwoodsoftware.datautils.DataBundle com.morningwoodsoftware.datautils.DataBundle.create() */
    jobject create();


private:
    JavaVM *mJavaVM;
};

#endif /* _JNI_GEN_com_morningwoodsoftware_datautils_DataBundle_h_ */
