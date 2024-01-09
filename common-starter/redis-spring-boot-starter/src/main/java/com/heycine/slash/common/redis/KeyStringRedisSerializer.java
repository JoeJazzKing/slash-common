//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.heycine.slash.common.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author zzj
 */
public class KeyStringRedisSerializer implements RedisSerializer<String> {
	private String cachePrefix;

	public KeyStringRedisSerializer(String cachePrefix) {

		this.cachePrefix = cachePrefix;
	}

	@Override
	public byte[] serialize(String str) throws SerializationException {
		return str == null ?
				null : (this.cachePrefix + str).getBytes(Charset.defaultCharset());
	}

	@Override
	public String deserialize(byte[] bytes) throws SerializationException {
		return bytes == null ?
				null : (new String(bytes, Charset.defaultCharset())).replaceFirst(this.cachePrefix, "");
	}
}
