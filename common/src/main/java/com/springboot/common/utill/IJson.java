package com.springboot.common.utill;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 *  再次封装的 GSon 类
 */
public class IJson {
	private static Gson gson; // 请尽量用单例，效率高
	static {

		ExclusionStrategy myExclusionStrategy = new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes fa) {
				return fa.getName().startsWith("_");
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		};

		TypeAdapter<String> stringTypeAdapter = new TypeAdapter<String>() {

			@Override
			public void write(JsonWriter out, String value) throws IOException {
				if (StringUtils.isEmpty(value)) {
					out.value(""); // 序列化时将 null 转为 ""
				} else {
					out.value(value);
				}
			}

			@Override
			public String read(JsonReader in) throws IOException {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					return null;
				}
				String str = in.nextString();
				return StringUtils.isEmpty(str) ? "" : str;
			}

		};

		TypeAdapter<Integer> intTypeAdapter = new TypeAdapter<Integer>() {

			@Override
			public void write(JsonWriter out, Integer value) throws IOException {
				if (value == null) {
					out.value(0);
				} else {
					out.value(value);
				}
			}

			@Override
			public Integer read(JsonReader in) throws IOException {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					return null;
				}
				return StringUtils.getInt(in.nextString());
			}

		};

		TypeAdapter<Long> longTypeAdapter = new TypeAdapter<Long>() {

			@Override
			public void write(JsonWriter out, Long value) throws IOException {
				if (value == null) {
					out.value(0);
				} else {
					out.value(value);
				}
			}

			@Override
			public Long read(JsonReader in) throws IOException {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					return null;
				}
				return StringUtils.getLong(in.nextString());
			}

		};

		TypeAdapter<Float> floatTypeAdapter = new TypeAdapter<Float>() {

			@Override
			public void write(JsonWriter out, Float value) throws IOException {
				if (value == null) {
					out.value(0);
				} else {
					out.value(value);
				}
			}

			@Override
			public Float read(JsonReader in) throws IOException {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					return null;
				}
				return StringUtils.getFloat(in.nextString());
			}

		};

		TypeAdapter<Double> doubleTypeAdapter = new TypeAdapter<Double>() {

			@Override
			public void write(JsonWriter out, Double value) throws IOException {
				if (value == null) {
					out.value(0);
				} else {
					out.value(value);
				}
			}

			@Override
			public Double read(JsonReader in) throws IOException {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					return null;
				}
				return StringUtils.getDouble(in.nextString());
			}

		};

		gson = new GsonBuilder().setExclusionStrategies(myExclusionStrategy)
				.registerTypeAdapter(String.class, stringTypeAdapter).registerTypeAdapter(Integer.class, intTypeAdapter)
				.registerTypeAdapter(int.class, intTypeAdapter).registerTypeAdapter(Long.class, longTypeAdapter)
				.registerTypeAdapter(long.class, longTypeAdapter).registerTypeAdapter(Float.class, floatTypeAdapter)
				.registerTypeAdapter(float.class, floatTypeAdapter).registerTypeAdapter(Double.class, doubleTypeAdapter)
				.registerTypeAdapter(double.class, doubleTypeAdapter).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

	/**
	 * 把对象转为json, _开头的会被忽略
	 *
	 * @param object
	 *            某个对象
	 * @param type
	 *            类型
	 * @return json string
	 */
	public static String toJson(Object object, Type type) {
		if (object == null || type == null)
			return null;
		String string = null;
		try {
			string = gson.toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}

	/**
	 * 将json 转为对象，_开头的会被忽略
	 */
	public static <T> T fromJson(String jsonString, Type type) {
		if (StringUtils.isEmpty(jsonString) || type == null)
			return null;
		T tmp = null;
		try {
			tmp = gson.fromJson(jsonString, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

}
