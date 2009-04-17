package com.dumontierlab.jxta.owl.io;

import java.io.IOException;

import org.mindswap.pellet.utils.ATermUtils;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

public class ATermSerializer {

	public static ATermAppl deserialize(String string) throws IOException {
		try {
			return (ATermAppl) ATermUtils.term(string);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public static String serialize(ATerm term) {
		StringBuilder sb = new StringBuilder();
		serialize(term, sb);
		return sb.toString();
	}

	public static void serialize(ATerm term, StringBuilder sb) {
		if (term instanceof ATermAppl) {
			ATermAppl appl = (ATermAppl) term;
			if (ATermUtils.isPrimitive(appl)) {
				sb.append("\"" + appl.getAFun().getName() + "\"");
			} else {
				AFun fun = appl.getAFun();
				sb.append(fun.getName());
				if (fun.getArity() > 0) {
					sb.append("(");
					for (ATerm arg : appl.getArgumentArray()) {
						serialize(arg, sb);
						sb.append(", ");
					}
					sb.deleteCharAt(sb.length() - 1);
					sb.deleteCharAt(sb.length() - 1);
					sb.append(")");
				}
			}
		} else if (term instanceof ATermList) {
			ATermList list = (ATermList) term;
			sb.append("[");
			if (!list.isEmpty()) {
				int i = 0;
				for (; i < list.getLength() - 1; i++) {
					serialize(list.elementAt(i), sb);
					sb.append(", ");
				}
				serialize(list.elementAt(i), sb);
			}
			sb.append("]");
		} else {
			sb.append(term.toString());
		}
	}

}
