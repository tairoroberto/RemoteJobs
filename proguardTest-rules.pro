# ------------------- TEST DEPENDENCIES -------------------
-dontobfuscate
-ignorewarnings
-dontwarn org.hamcrest.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.mockito.**
-dontwarn sun.reflect.**
-dontwarn java.lang.management.**

-keep class org.hamcrest.** {
   *;
}

-keep org.junit.** { *; }
-dontwarn org.junit.**

-keep junit.** { *; }
-dontwarn junit.**

-keep class sun.misc.** { *; }
-dontwarn sun.misc.**

-dontwarn com.squareup.javawriter.JavaWriter

-dontwarn com.squareup.**
-dontwarn com.google.android.**

-keep class com.google.android.** {
   *;
}
-keep class com.google.common.** {
   *;
}

-ignorewarnings

-keepattributes *Annotation*

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter
# Uncomment this if you use Mockito
-dontwarn org.mockito.**