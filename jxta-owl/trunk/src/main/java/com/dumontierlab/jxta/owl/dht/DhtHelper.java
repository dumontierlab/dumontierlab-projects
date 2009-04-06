package com.dumontierlab.jxta.owl.dht;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.WeakHashMap;

import aterm.ATerm;

public class DhtHelper {

	private static final WeakHashMap<Object, BigInteger> FLY_WEIGHT = new WeakHashMap<Object, BigInteger>();

	public static BigInteger hash(ATerm term) {
		BigInteger hash = FLY_WEIGHT.get(term);
		if (hash != null) {
			return hash;
		}
		hash = hash(term.toString());
		FLY_WEIGHT.put(term, hash);
		return hash;
	}

	public static BigInteger hash(String text) {
		return hash(text, false);
	}

	public static BigInteger hash(String text, boolean useFlyWeight) {
		BigInteger hash = null;
		if (useFlyWeight && (hash = FLY_WEIGHT.get(text)) != null) {
			return hash;
		}
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = md.digest();
			hash = new BigInteger(convertToHex(sha1hash), 16);
			if (useFlyWeight) {
				FLY_WEIGHT.put(text, hash);
			}
			return hash;
		} catch (Exception e) {
			assert false : e.getMessage();
			return null;
		}
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
