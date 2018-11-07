package com.mavole.mavolenet.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public  class RequestParams {

	public ConcurrentHashMap<String, String> mPathParams = new ConcurrentHashMap<String, String>();
	public ConcurrentHashMap<String, Object> mFileParams = new ConcurrentHashMap<String, Object>();

	/**
	 * Constructs a new empty {@code RequestParams} instance.
	 */
	public RequestParams() {
		this(null);
	}

	/**
	 * Constructs a new RequestParams instance containing the value/value string
	 * params from the specified map.
	 *
	 * @param source
	 *            the source value/value string map to add.
	 */
	public RequestParams(Map<String, String> source) {
		if (source != null) {
			for (Map.Entry<String, String> entry : source.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Constructs a new RequestParams instance and populate it with a single
	 * initial value/value string param.
	 *
	 * @param key
	 *            the value name for the intial param.
	 * @param value
	 *            the value string for the initial param.
	 */
	public RequestParams(final String key, final String value) {
		this(new HashMap<String, String>() {
			{
				put(key, value);
			}
		});
	}

	/**
	 * Adds a value/value string pair to the request.
	 *
	 * @param key
	 *            the value name for the new param.
	 * @param value
	 *            the value string for the new param.
	 */
	public void put(String key, String value) {
		if (key != null && value != null) {
			mPathParams.put(key, value);
		}
	}

	public void put(String key, Object object) throws FileNotFoundException {

		if (key != null) {
			mFileParams.put(key, object);
		}
	}
}