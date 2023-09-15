# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Don't note duplicate definition (Legacy Apche Http Client)
-dontnote android.net.http.*
-dontnote org.apache.http.**

-dontwarn signgate.core.provider.pbe.PBEKeyDerivation
-keep class signgate.core.crypto.pkcs.** {
    <fields>;
    <methods>;
}

-keep class signgate.core.crypto.pkcs7.** {
    <fields>;
    <methods>;
}

-keep class signgate.core.crypto.x509.** {
    <fields>;
    <methods>;
}

-keep class signgate.core.crypto.util.** {
    <fields>;
    <methods>;
}

-keep class signgate.core.javax.crypto.** {
    <fields>;
    <methods>;
}

-keep class signgate.core.provider.** {
    <fields>;
    <methods>;
}
-dontwarn netscape.ldap.**
-dontwarn javax.naming.directory.*
-dontwarn javax.naming.*
-dontwarn com.kica.security.asn1.*
-keep class com.kica.security.* {
    <fields>;
    <methods>;
}

-keep class com.kica.security.util.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.certpath.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.crypto.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.provider.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.x509.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.asn1.* {
    <fields>;
    <methods>;
}

-keep class com.kica.security.asn1.x509.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.asn1.cms.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.asn1.cmp.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.asn1.crmf.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.asn1.kisa.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.asn1.pkcs.** {
    <fields>;
    <methods>;
}

-keep interface com.kica.security.asn1.oiw.** {
    <fields>;
    <methods>;
}

-keep interface com.kica.security.asn1.oid.** {
    <fields>;
    <methods>;
}

-keep class com.kica.security.asn1.vid.** {
    <fields>;
    <methods>;
}
-dontwarn com.gpki.gpkiapi.**
-dontwarn com.sg.openews.api.pkcs7.SignedDataCommon
-keep class com.kica.crypto.** {
    <fields>;
    <methods>;
}

-keep class com.kica.km.** {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.common.** {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.* {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.asn1.** {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.cmp.** {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.cms.** {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.crypto.** {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.exception.** {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.key.* {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.key.impl.* {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.keystore.* {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.pkcs12.* {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.pkcs7.* {
    <fields>;
    <methods>;
}

-keep class com.sg.openews.api.util.* {
    <fields>;
    <methods>;
}
-dontwarn com.sg.openews.api.pkcs7.*
-dontwarn com.kica.tls.*
-keep class com.kica.ucpid.asn1.PersonInfoReq {
    <fields>;
    <methods>;
}

-keep class com.kica.ucpid.resource.* {
    <fields>;
    <methods>;
}
