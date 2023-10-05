# proguard-rules.pro
-dontoptimize
-dontobfuscate
-ignorewarnings
-dontshrink

#-keep class io.realm.kotlin.Deleteable {
#    *;
#}

# https://github.com/realm/realm-kotlin/issues/1330
# Caused by: java.lang.NoSuchMethodException: io.realm.kotlin.jvm.SoLoader.load()
-keep class io.realm.kotlin.jvm.SoLoader {
    *;
}
