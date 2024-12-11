# Keep data models (e.g., Article, ArticleResponse)
-keep class com.example.padicareapp.data.response.** { *; }
-keep class com.example.padicareapp.view.home.** { *; }
-keep class com.example.padicareapp.data.retrofit.** { *; }
-keep class com.example.padicareapp.view.home.Article { *; }
-keep class com.example.padicareapp.view.home.HomeViewModel { *; }
-keep class com.example.padicareapp.data.response.ArticleResponse { *; }
-keep class retrofit2.** { *; }


# Keep Retrofit interfaces
-keep interface com.example.padicareapp.data.retrofit.** { *; }

# Keep Gson annotations
-keepattributes *Annotation*

# Keep Retrofit
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn javax.annotation.**
-keepattributes Signature
-keepattributes Exceptions

# Keep Glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.AppGlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#    **[] $VALUES;
#    public *;
#}
