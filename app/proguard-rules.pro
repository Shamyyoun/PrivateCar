# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/basim/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep ads here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#icepick
-dontwarn icepick.**
-keep class icepick.** { *; }
-keep class **$$Icepick { *; }
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#picasso
-dontwarn com.squareup.picasso.**

#UltimateRecyclerView
-dontwarn com.marshalchen.ultimaterecyclerview.**

#MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**


#leakcanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-keep class com.squareup.haha.** { *; }
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**
-dontwarn com.squareup.leakcanary.**


#Gson, don't change classes names
-keepattributes Signature, *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.privateegy.privatecar.models.** { *; }
-keep class com.google.android.gms.** { *; } #http://stackoverflow.com/q/27326218

#Prettytime
-keep class org.ocpsoft.prettytime.i18n.**

#remove Log messages
-assumenosideeffects class android.util.Log { *; }


#Fabric
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable


