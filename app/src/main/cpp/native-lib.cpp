#include <jni.h>
#include <string>

#include <sys/stat.h>
#include <android/log.h>

extern "C"
JNIEXPORT jlong JNICALL
Java_edu_cityu_fileoptlogger_jni_JNITools_getIno(JNIEnv *env, jobject instance, jstring fName_) {
    const char *fName = env->GetStringUTFChars(fName_, 0);

    struct stat st;

    __android_log_print(ANDROID_LOG_DEBUG, "FileOptLogger", "path=%s", fName);
    if(fName == NULL) {
        return -1;
    }

    if(stat(fName, &st) == -1){
        perror("[hubery stat error] !!!");
        return -1;
    }


    env->ReleaseStringUTFChars(fName_, fName); // release fName object
    return (jlong)st.st_ino;
}