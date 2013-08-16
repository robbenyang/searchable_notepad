#include"jni.h"
#include<string>
#include<android/log.h>
#include<iostream>
#include<vector>
#include"./zinnia/zinnia.h"

using namespace std;

extern "C" {
JNIEXPORT jobjectArray JNICALL Java_com_example_searchablenotepad_handwriting_OCR(
		JNIEnv* env, jobject thiz, jintArray data, jintArray div) {
	jsize size = env->GetArrayLength(data);
	jobjectArray ret;
	vector < string > output;
	vector<int> v;
	vector<int> t;
	vector<int> d;

	//convert jint arrays to C++ vector
	jint *body = env->GetIntArrayElements(data, 0);
	for (int j = 0; j < size; j++) {
		v.push_back(body[j]);
	}
	size = env->GetArrayLength(div);
	jint *temp = env->GetIntArrayElements(div, 0);
	for (int j = 0; j < size; j++) {
		d.push_back(temp[j]-1);
	}
//	for (int i = 0; i < 30; i++) {
//		__android_log_print(ANDROID_LOG_WARN, "C++ debug info",
//				"value of Vs: %d", v[i]);
//	}
//	for (int i = 0; i < d.size(); i++) {
//		__android_log_print(ANDROID_LOG_WARN, "C++ debug info",
//				"value of Ds: %d", d[i]);
//	}
	__android_log_print(ANDROID_LOG_WARN, "C++ debug info","size of v: %d", v.size());

	for (int i = 0; i < d.size() - 1; i++) {
		t.clear();
		for (int j = d[i]; j < d[i + 1]; j++) {
			t.push_back(v[3 * j]);
			t.push_back(v[3 * j + 1]);
			t.push_back(v[3 * j + 2]);
		}
//		for (int k = 0; k < t.size(); k++) {
//			__android_log_print(ANDROID_LOG_WARN, "C++ debug info",
//					"value of Ks: %d", t[k]);
//		}
		zinnia::Recognizer *recognizer = zinnia::Recognizer::create();
		if (!recognizer->open(
				"/storage/sdcard0/Pictures/snote/handwriting-ja.model")) {
			jobjectArray _1 = (jobjectArray) env->NewObjectArray(1,
					env->FindClass("java/lang/String"), env->NewStringUTF(""));
			env->SetObjectArrayElement(_1, i, env->NewStringUTF("Load model failed"));
			return _1;
		}
		zinnia::Character *character = zinnia::Character::create();
		character->clear();
		__android_log_print(ANDROID_LOG_WARN, "C++ debug info","size of t: %d", t.size());
		for (int k = 0; k < t.size()-2; k += 3) {
			character->add(t[k], t[k + 1], t[k + 2]);
//			__android_log_print(ANDROID_LOG_WARN, "C++ debug info","t-value,%d,%d,%d", t[k],t[k+1],t[k+2]);
		}

		zinnia::Result *result = recognizer->classify(*character, 5);
		if (!result) {
			jobjectArray _2 = (jobjectArray) env->NewObjectArray(1,
					env->FindClass("java/lang/String"), env->NewStringUTF(""));
			env->SetObjectArrayElement(_2, i, env->NewStringUTF("Not recognized!!"));
			return _2;
		}

		if(result->size()>0){
			output.push_back(result->value(0));
		}
		__android_log_print(ANDROID_LOG_WARN, "C++ debug info",
						"After dereference");
		delete result;
		delete character;
		delete recognizer;
	}
	const char *message[output.size()];
	__android_log_print(ANDROID_LOG_WARN, "C++ Crash Debug info",
							"Out of the recognize for loop");
	for (int i = 0; i < output.size(); i++) {
		message[i] = output[i].c_str();
	}

	ret = (jobjectArray) env->NewObjectArray(output.size(),
			env->FindClass("java/lang/String"), env->NewStringUTF(""));

	for (int i = 0; i < output.size(); i++) {
		env->SetObjectArrayElement(ret, i, env->NewStringUTF(message[i]));
	}
	return ret;
}

}
