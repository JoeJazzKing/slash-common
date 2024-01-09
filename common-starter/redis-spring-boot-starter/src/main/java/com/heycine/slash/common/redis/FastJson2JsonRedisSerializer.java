package com.heycine.slash.common.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author zzj
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	public static final Gson GSON = new Gson();
	private ObjectMapper objectMapper = new ObjectMapper();
	private Class<T> clazz;

	public FastJson2JsonRedisSerializer(Class<T> clazz) {

		this.clazz = clazz;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		return t == null ? new byte[0] : GSON.toJson(t).getBytes(DEFAULT_CHARSET);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes != null && bytes.length > 0) {
			String str = new String(bytes, DEFAULT_CHARSET);
			return GSON.fromJson(str, this.clazz);
		} else {
			return null;
		}
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = objectMapper;
	}

	protected JavaType getJavaType(Class<?> clazz) {

		return TypeFactory.defaultInstance().constructType(clazz);
	}
}
