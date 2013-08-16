LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := recognize.cpp ./zinnia/character.cpp ./zinnia/feature.cpp ./zinnia/libzinnia.cpp ./zinnia/param.cpp ./zinnia/recognizer.cpp ./zinnia/sexp.cpp ./zinnia/svm.cpp ./zinnia/trainer.cpp ./zinnia/zinnia.cpp
LOCAL_C_INCLUDES := recignize.h ./zinnia/config.h ./zinnia/common.h ./zinnia/feature.h ./zinnia/freelist.h ./zinnia/mmap.h ./zinnia/param.h ./zinnia/sexp.h ./zinnia/scoped_ptr.h ./zinnia/svm.h ./zinnia/zinnia.h ./zinnia/stsream_wrapper.h
LOCAL_CFLAGS += -DHAVE_CONFIG_H
LOCAL_LDLIBS := -llog
LOCAL_MODULE := CharRecognizer

include $(BUILD_SHARED_LIBRARY)