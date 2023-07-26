package org.secwallet.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ObjectMapperUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ObjectMapperUtils.class);
	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		objectMapper.setDateFormat(dateFormat);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * Convert java type object to JSON formatted string
	 *
	 * @param object
	 * @return
	 */
	public static <T> String serialize(T object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOG.debug(e.getMessage(), e);
			LOG.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Convert JSON formatted string to java type object or java array type object
	 * 
	 * @param json
	 * @param typeReference
	 * @return
	 */
	public static <T> T deserialize(String json, TypeReference<T> typeReference) {
		try {
			return objectMapper.readValue(json, typeReference);
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
			LOG.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Convert JSON formatted string to java type object or java array type object
	 * 
	 * @param json
	 * @param clz
	 * @return
	 */
	public static <T> T readValue(String json, Class<T> clz) {
		try {
			return objectMapper.readValue(json, clz);
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
			LOG.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Check if it is json
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isJSONValid(String json) {
		try {
			objectMapper.readTree(json);
			return true;
		} catch (IOException e) {
		}
		return false;
	}

}
