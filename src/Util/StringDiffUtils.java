package Util;

public class StringDiffUtils {

	public static final int INDEX_NOT_FOUND = -1;

	public static final String EMPTY = "";

	public static String diff(String str1, String str2) {

	    if (str1 == null) {
	        return str2;
	    }
	    if (str2 == null) {
	        return str1;
	    }
	    int at = indexOfDiff(str1, str2);
	    if (at == INDEX_NOT_FOUND) {
	        return EMPTY;
	    }
	    return str2.substring(at);
	}

	public static int indexOfDiff(CharSequence cs1, CharSequence cs2) {

	    if (cs1 == cs2) {
	        return INDEX_NOT_FOUND;
	    }
	    if (cs1 == null || cs2 == null) {
	        return 0;
	    }
	    int i;
	    for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
	        if (cs1.charAt(i) != cs2.charAt(i)) {
	            break;
	        }
	    }
	    if (i < cs2.length() || i < cs1.length()) {
	        return i;
	    }
	    return INDEX_NOT_FOUND;
	}
}
