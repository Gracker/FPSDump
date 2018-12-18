LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := FPSDump
LOCAL_DEX_PREOPT := false
LOCAL_CERTIFICATE := platform

LOCAL_STATIC_JAVA_LIBRARIES := qcrilhook-static
LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))
