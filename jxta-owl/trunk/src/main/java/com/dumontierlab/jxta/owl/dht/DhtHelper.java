package com.dumontierlab.jxta.owl.dht;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.WeakHashMap;

public class DhtHelper {

	private static final WeakHashMap<String, BigInteger> FLY_WEIGHT = new WeakHashMap<String, BigInteger>();

	public static String getClosest(String key, String... toCompare) {
		BigInteger[] numericHashes = new BigInteger[toCompare.length];
		for (int i = 0; i < toCompare.length; i++) {
			numericHashes[i] = hash(toCompare[i]);
		}
		int closest = getClosest(hash(key), numericHashes);
		if (closest == -1) {
			return null;
		}
		return toCompare[closest];
	}

	public static int getClosest(BigInteger key, BigInteger... toCompare) {
		if (toCompare.length == 0) {
			return -1;
		}
		int closest = 0;
		BigInteger distanceToClosest = key.subtract(toCompare[closest]).abs();
		for (int i = 1; i < toCompare.length; i++) {
			BigInteger difference = key.subtract(toCompare[i]).abs();
			if (distanceToClosest.min(difference) == difference) {
				distanceToClosest = difference;
				closest = i;
			}
		}
		return closest;
	}

	public static BigInteger hash(String text) {
		BigInteger hash = FLY_WEIGHT.get(text);
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = md.digest();
			hash = new BigInteger(convertToHex(sha1hash), 16);
		} catch (Exception e) {
			assert false : e.getMessage();
		}
		return hash;
	}

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

}
