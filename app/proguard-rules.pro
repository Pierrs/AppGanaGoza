# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Mantener solo los modelos de datos
-keep class com.dapm.ganagoza.modelo.** { *; }

# Mantener clases necesarias para Room
-keep class com.dapm.ganagoza.datos.base_datos.BaseDatosReto { *; }
-keep class com.dapm.ganagoza.datos.dao.AccesoDatosReto { *; }

# Si alguna clase usa anotaciones (como con Room)
-keepattributes *Annotation*

# Para serialización/deserialización
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Para Parcelable
-keepnames class * implements android.os.Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Mantener información para debugging
-keepattributes SourceFile,LineNumberTable

# Mantener anotaciones de Kotlin
-keepattributes *Annotation*, InnerClasses
-keepattributes Signature
-keepattributes Exceptions
