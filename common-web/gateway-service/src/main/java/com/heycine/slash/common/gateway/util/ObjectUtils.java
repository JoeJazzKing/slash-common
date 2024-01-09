package com.heycine.slash.common.gateway.util;

import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ObjectUtils {

	public static boolean matches(String str, List<String> strs) {
		if (!isEmpty(str) && !isEmpty(strs)) {
			Iterator var2 = strs.iterator();

			String pattern;
			do {
				if (!var2.hasNext()) {
					return false;
				}

				pattern = (String) var2.next();
			} while (!isMatch(pattern, str));

			return true;
		} else {
			return false;
		}
	}

	public static boolean isMatch(String pattern, String url) {
		AntPathMatcher matcher = new AntPathMatcher();
		return matcher.match(pattern, url);
	}

	public static boolean isEmpty(String str) {
		return isNull(str) || "".equals(str.trim());
	}

	public static boolean isNull(Object object) {
		return object == null;
	}

	public static boolean isEmpty(Collection<?> coll) {
		return isNull(coll) || coll.isEmpty();
	}

}
