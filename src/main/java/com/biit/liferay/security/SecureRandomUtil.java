package com.biit.liferay.security;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SecureRandomUtil {

	public static boolean nextBoolean() {
		byte b = nextByte();

		if (b < 0) {
			return false;
		} else {
			return true;
		}
	}

	public static byte nextByte() {
		int index = _index.getAndIncrement();

		if (index < _BUFFER_SIZE) {
			return _bytes[index];
		}

		return (byte) _reload();
	}

	public static double nextDouble() {
		int index = _index.getAndAdd(8);

		if ((index + 7) < _BUFFER_SIZE) {
			return BigEndianCodec.getDouble(_bytes, index);
		}

		return Double.longBitsToDouble(_reload());
	}

	public static float nextFloat() {
		int index = _index.getAndAdd(4);

		if ((index + 3) < _BUFFER_SIZE) {
			return BigEndianCodec.getFloat(_bytes, index);
		}

		return Float.intBitsToFloat((int) _reload());
	}

	public static int nextInt() {
		int index = _index.getAndAdd(4);

		if ((index + 3) < _BUFFER_SIZE) {
			return BigEndianCodec.getInt(_bytes, index);
		}

		return (int) _reload();
	}

	public static long nextLong() {
		int index = _index.getAndAdd(8);

		if ((index + 7) < _BUFFER_SIZE) {
			return BigEndianCodec.getLong(_bytes, index);
		}

		return _reload();
	}

	private static long _reload() {
		if (_reloadingFlag.compareAndSet(false, true)) {
			_random.nextBytes(_bytes);

			_index.set(0);

			_reloadingFlag.set(false);
		}

		int offset = _index.get() % (_BUFFER_SIZE - 7);

		long l = BigEndianCodec.getLong(_bytes, offset) ^ _gapSeed;

		_gapSeed = l;

		return l;
	}

	private static final int _BUFFER_SIZE;

	private static final int _MIN_BUFFER_SIZE = 1024;

	private static final byte[] _bytes;
	private static final AtomicInteger _index = new AtomicInteger();
	private static final Random _random = new SecureRandom();
	private static final AtomicBoolean _reloadingFlag = new AtomicBoolean();

	static {
		_BUFFER_SIZE = _MIN_BUFFER_SIZE;

		_bytes = new byte[_BUFFER_SIZE];

		_random.nextBytes(_bytes);

		_gapSeed = _random.nextLong();
	}

	private static long _gapSeed;

}
