package com.bot.middleware.utility.id;

public class PublicIdGenerator {

	private static final Integer VERSION_1 = 1;
	private static final Integer VERSION_INDEX = 0;
	private static final Integer PARITY_INDEX = 1;
	private static final Integer BASE_36 = 36;
	private static final String _64_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_-";

	public static Long generatePublicId() {
		return SequenceGenerator.nextId();
	}

	public static String encodedPublicId(Long publicId) {
		String encodedNumber = encodeNumber(publicId, BASE_36);
		String addOn = VERSION_1.toString() + getParityChar(publicId);
		String encodedNumberAppended = addOn + encodedNumber;
		return encodedNumberAppended;
	}

	public static Long decodePublicId(String encodedId) {
		try {
			String originalId = encodedId.substring(2);
			int version = Integer.parseInt(Character.toString(encodedId.charAt(VERSION_INDEX)));
			long parity = Long.parseLong(Character.toString(encodedId.charAt(PARITY_INDEX)));
			if (VERSION_1 == version) {
				Long decodedNumber = decodeNumber(originalId, BASE_36);
				if (parity == getParityChar(decodedNumber)) {
					return decodedNumber;
				}
			}
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}

	private static String encodeNumber(long x, int base) {
		char[] buf = new char[64];
		int p = buf.length;
		do {
			buf[--p] = _64_CHARS.charAt((int) (x % base));
			x /= base;
		} while (x != 0);
		return new String(buf, p, buf.length - p);
	}

	private static long decodeNumber(String s, int base) {
		long x = 0;
		for (char c : s.toCharArray()) {
			int charValue = _64_CHARS.indexOf(c);
			if (charValue == -1)
				throw new NumberFormatException(s);
			x *= base;
			x += charValue;
		}
		return x;
	}

	private static long sumOfDigits(long num) {
		long sum = 0;
		while (num > 0) {
			sum = sum + num % 10;
			num = num / 10;
		}
		return sum;
	}

	private static long getParityChar(long num) {
		long sum = sumOfDigits(num);
		long parity = 10 - (sum % 10);
		if (parity == 10)
			parity = 0;
		return parity;
	}

}
