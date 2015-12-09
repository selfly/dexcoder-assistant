package com.dexcoder.commons.utils;

/**
 * 本类来源于apache-commons-lang > StringUtils
 * <p/>
 * Created by liyd on 2015-12-4.
 */
public class StrUtils {

    /**
     * The empty String <code>""</code>.
     *
     * @since 2.0
     */
    public static final String EMPTY           = "";

    /**
     * Represents a failed index search.
     *
     * @since 2.1
     */
    public static final int    INDEX_NOT_FOUND = -1;

    /**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int   PAD_LIMIT       = 8192;

    /**
     * <p><code>StrUtils</code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>StrUtils.trim(" foo ");</code>.</p>
     * <p/>
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public StrUtils() {
        super();
    }

    // Empty checks
    //-----------------------------------------------------------------------

    /**
     * <p>Checks if a String is empty ("") or null.</p>
     * <p/>
     * <pre>
     * StrUtils.isEmpty(null)      = true
     * StrUtils.isEmpty("")        = true
     * StrUtils.isEmpty(" ")       = false
     * StrUtils.isEmpty("bob")     = false
     * StrUtils.isEmpty("  bob  ") = false
     * </pre>
     * <p/>
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>Checks if a String is not empty ("") and not null.</p>
     * <p/>
     * <pre>
     * StrUtils.isNotEmpty(null)      = false
     * StrUtils.isNotEmpty("")        = false
     * StrUtils.isNotEmpty(" ")       = true
     * StrUtils.isNotEmpty("bob")     = true
     * StrUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String str) {
        return !StrUtils.isEmpty(str);
    }

    /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     * <p/>
     * <pre>
     * StrUtils.isBlank(null)      = true
     * StrUtils.isBlank("")        = true
     * StrUtils.isBlank(" ")       = true
     * StrUtils.isBlank("bob")     = false
     * StrUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
     * <p/>
     * <pre>
     * StrUtils.isNotBlank(null)      = false
     * StrUtils.isNotBlank("")        = false
     * StrUtils.isNotBlank(" ")       = false
     * StrUtils.isNotBlank("bob")     = true
     * StrUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is
     * not empty and not null and not whitespace
     * @since 2.0
     */
    public static boolean isNotBlank(String str) {
        return !StrUtils.isBlank(str);
    }

    // Trim
    //-----------------------------------------------------------------------

    /**
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String, handling <code>null</code> by returning
     * an empty String ("").</p>
     * <p/>
     * <pre>
     * StrUtils.clean(null)          = ""
     * StrUtils.clean("")            = ""
     * StrUtils.clean("abc")         = "abc"
     * StrUtils.clean("    abc    ") = "abc"
     * StrUtils.clean("     ")       = ""
     * </pre>
     *
     * @param str the String to clean, may be null
     * @return the trimmed text, never <code>null</code>
     * @see String#trim()
     * @deprecated Use the clearer named {@link #trimToEmpty(String)}.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String clean(String str) {
        return str == null ? EMPTY : str.trim();
    }

    /**
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String, handling <code>null</code> by returning
     * <code>null</code>.</p>
     * <p/>
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #strip(String)}.</p>
     * <p/>
     * <p>To trim your choice of characters, use the
     * {@link #strip(String, String)} methods.</p>
     * <p/>
     * <pre>
     * StrUtils.trim(null)          = null
     * StrUtils.trim("")            = ""
     * StrUtils.trim("     ")       = ""
     * StrUtils.trim("abc")         = "abc"
     * StrUtils.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param str the String to be trimmed, may be null
     * @return the trimmed string, <code>null</code> if null String input
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String returning <code>null</code> if the String is
     * empty ("") after the trim or if it is <code>null</code>.
     * <p/>
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #stripToNull(String)}.</p>
     * <p/>
     * <pre>
     * StrUtils.trimToNull(null)          = null
     * StrUtils.trimToNull("")            = null
     * StrUtils.trimToNull("     ")       = null
     * StrUtils.trimToNull("abc")         = "abc"
     * StrUtils.trimToNull("    abc    ") = "abc"
     * </pre>
     *
     * @param str the String to be trimmed, may be null
     * @return the trimmed String,
     * <code>null</code> if only chars &lt;= 32, empty or null String input
     * @since 2.0
     */
    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    /**
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String returning an empty String ("") if the String
     * is empty ("") after the trim or if it is <code>null</code>.
     * <p/>
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #stripToEmpty(String)}.</p>
     * <p/>
     * <pre>
     * StrUtils.trimToEmpty(null)          = ""
     * StrUtils.trimToEmpty("")            = ""
     * StrUtils.trimToEmpty("     ")       = ""
     * StrUtils.trimToEmpty("abc")         = "abc"
     * StrUtils.trimToEmpty("    abc    ") = "abc"
     * </pre>
     *
     * @param str the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     * @since 2.0
     */
    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }

    // Stripping
    //-----------------------------------------------------------------------

    /**
     * <p>Strips whitespace from the start and end of a String.</p>
     * <p/>
     * <p>This is similar to {@link #trim(String)} but removes whitespace.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.strip(null)     = null
     * StrUtils.strip("")       = ""
     * StrUtils.strip("   ")    = ""
     * StrUtils.strip("abc")    = "abc"
     * StrUtils.strip("  abc")  = "abc"
     * StrUtils.strip("abc  ")  = "abc"
     * StrUtils.strip(" abc ")  = "abc"
     * StrUtils.strip(" ab c ") = "ab c"
     * </pre>
     *
     * @param str the String to remove whitespace from, may be null
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String strip(String str) {
        return strip(str, null);
    }

    /**
     * <p>Strips whitespace from the start and end of a String  returning
     * <code>null</code> if the String is empty ("") after the strip.</p>
     * <p/>
     * <p>This is similar to {@link #trimToNull(String)} but removes whitespace.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <pre>
     * StrUtils.stripToNull(null)     = null
     * StrUtils.stripToNull("")       = null
     * StrUtils.stripToNull("   ")    = null
     * StrUtils.stripToNull("abc")    = "abc"
     * StrUtils.stripToNull("  abc")  = "abc"
     * StrUtils.stripToNull("abc  ")  = "abc"
     * StrUtils.stripToNull(" abc ")  = "abc"
     * StrUtils.stripToNull(" ab c ") = "ab c"
     * </pre>
     *
     * @param str the String to be stripped, may be null
     * @return the stripped String,
     * <code>null</code> if whitespace, empty or null String input
     * @since 2.0
     */
    public static String stripToNull(String str) {
        if (str == null) {
            return null;
        }
        str = strip(str, null);
        return str.length() == 0 ? null : str;
    }

    /**
     * <p>Strips whitespace from the start and end of a String  returning
     * an empty String if <code>null</code> input.</p>
     * <p/>
     * <p>This is similar to {@link #trimToEmpty(String)} but removes whitespace.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <pre>
     * StrUtils.stripToEmpty(null)     = ""
     * StrUtils.stripToEmpty("")       = ""
     * StrUtils.stripToEmpty("   ")    = ""
     * StrUtils.stripToEmpty("abc")    = "abc"
     * StrUtils.stripToEmpty("  abc")  = "abc"
     * StrUtils.stripToEmpty("abc  ")  = "abc"
     * StrUtils.stripToEmpty(" abc ")  = "abc"
     * StrUtils.stripToEmpty(" ab c ") = "ab c"
     * </pre>
     *
     * @param str the String to be stripped, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     * @since 2.0
     */
    public static String stripToEmpty(String str) {
        return str == null ? EMPTY : strip(str, null);
    }

    /**
     * <p>Strips any of a set of characters from the start and end of a String.
     * This is similar to {@link String#trim()} but allows the characters
     * to be stripped to be controlled.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.
     * An empty string ("") input returns the empty string.</p>
     * <p/>
     * <p>If the stripChars String is <code>null</code>, whitespace is
     * stripped as defined by {@link Character#isWhitespace(char)}.
     * Alternatively use {@link #strip(String)}.</p>
     * <p/>
     * <pre>
     * StrUtils.strip(null, *)          = null
     * StrUtils.strip("", *)            = ""
     * StrUtils.strip("abc", null)      = "abc"
     * StrUtils.strip("  abc", null)    = "abc"
     * StrUtils.strip("abc  ", null)    = "abc"
     * StrUtils.strip(" abc ", null)    = "abc"
     * StrUtils.strip("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str        the String to remove characters from, may be null
     * @param stripChars the characters to remove, null treated as whitespace
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String strip(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        str = stripStart(str, stripChars);
        return stripEnd(str, stripChars);
    }

    /**
     * <p>Strips any of a set of characters from the start of a String.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.
     * An empty string ("") input returns the empty string.</p>
     * <p/>
     * <p>If the stripChars String is <code>null</code>, whitespace is
     * stripped as defined by {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <pre>
     * StrUtils.stripStart(null, *)          = null
     * StrUtils.stripStart("", *)            = ""
     * StrUtils.stripStart("abc", "")        = "abc"
     * StrUtils.stripStart("abc", null)      = "abc"
     * StrUtils.stripStart("  abc", null)    = "abc"
     * StrUtils.stripStart("abc  ", null)    = "abc  "
     * StrUtils.stripStart(" abc ", null)    = "abc "
     * StrUtils.stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     *
     * @param str        the String to remove characters from, may be null
     * @param stripChars the characters to remove, null treated as whitespace
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }
        return str.substring(start);
    }

    /**
     * <p>Strips any of a set of characters from the end of a String.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.
     * An empty string ("") input returns the empty string.</p>
     * <p/>
     * <p>If the stripChars String is <code>null</code>, whitespace is
     * stripped as defined by {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <pre>
     * StrUtils.stripEnd(null, *)          = null
     * StrUtils.stripEnd("", *)            = ""
     * StrUtils.stripEnd("abc", "")        = "abc"
     * StrUtils.stripEnd("abc", null)      = "abc"
     * StrUtils.stripEnd("  abc", null)    = "  abc"
     * StrUtils.stripEnd("abc  ", null)    = "abc"
     * StrUtils.stripEnd(" abc ", null)    = " abc"
     * StrUtils.stripEnd("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str        the String to remove characters from, may be null
     * @param stripChars the characters to remove, null treated as whitespace
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }
        return str.substring(0, end);
    }

    // StripAll
    //-----------------------------------------------------------------------

    /**
     * <p>Strips whitespace from the start and end of every String in an array.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <p>A new array is returned each time, except for length zero.
     * A <code>null</code> array will return <code>null</code>.
     * An empty array will return itself.
     * A <code>null</code> array entry will be ignored.</p>
     * <p/>
     * <pre>
     * StrUtils.stripAll(null)             = null
     * StrUtils.stripAll([])               = []
     * StrUtils.stripAll(["abc", "  abc"]) = ["abc", "abc"]
     * StrUtils.stripAll(["abc  ", null])  = ["abc", null]
     * </pre>
     *
     * @param strs the array to remove whitespace from, may be null
     * @return the stripped Strings, <code>null</code> if null array input
     */
    public static String[] stripAll(String[] strs) {
        return stripAll(strs, null);
    }

    /**
     * <p>Strips any of a set of characters from the start and end of every
     * String in an array.</p>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <p>A new array is returned each time, except for length zero.
     * A <code>null</code> array will return <code>null</code>.
     * An empty array will return itself.
     * A <code>null</code> array entry will be ignored.
     * A <code>null</code> stripChars will strip whitespace as defined by
     * {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <pre>
     * StrUtils.stripAll(null, *)                = null
     * StrUtils.stripAll([], *)                  = []
     * StrUtils.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
     * StrUtils.stripAll(["abc  ", null], null)  = ["abc", null]
     * StrUtils.stripAll(["abc  ", null], "yz")  = ["abc  ", null]
     * StrUtils.stripAll(["yabcz", null], "yz")  = ["abc", null]
     * </pre>
     *
     * @param strs       the array to remove characters from, may be null
     * @param stripChars the characters to remove, null treated as whitespace
     * @return the stripped Strings, <code>null</code> if null array input
     */
    public static String[] stripAll(String[] strs, String stripChars) {
        int strsLen;
        if (strs == null || (strsLen = strs.length) == 0) {
            return strs;
        }
        String[] newArr = new String[strsLen];
        for (int i = 0; i < strsLen; i++) {
            newArr[i] = strip(strs[i], stripChars);
        }
        return newArr;
    }

    // Equals
    //-----------------------------------------------------------------------

    /**
     * <p>Compares two Strings, returning <code>true</code> if they are equal.</p>
     * <p/>
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     * <p/>
     * <pre>
     * StrUtils.equals(null, null)   = true
     * StrUtils.equals(null, "abc")  = false
     * StrUtils.equals("abc", null)  = false
     * StrUtils.equals("abc", "abc") = true
     * StrUtils.equals("abc", "ABC") = false
     * </pre>
     *
     * @param str1 the first String, may be null
     * @param str2 the second String, may be null
     * @return <code>true</code> if the Strings are equal, case sensitive, or
     * both <code>null</code>
     * @see String#equals(Object)
     */
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * <p>Compares two Strings, returning <code>true</code> if they are equal ignoring
     * the case.</p>
     * <p/>
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered equal. Comparison is case insensitive.</p>
     * <p/>
     * <pre>
     * StrUtils.equalsIgnoreCase(null, null)   = true
     * StrUtils.equalsIgnoreCase(null, "abc")  = false
     * StrUtils.equalsIgnoreCase("abc", null)  = false
     * StrUtils.equalsIgnoreCase("abc", "abc") = true
     * StrUtils.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @param str1 the first String, may be null
     * @param str2 the second String, may be null
     * @return <code>true</code> if the Strings are equal, case insensitive, or
     * both <code>null</code>
     * @see String#equalsIgnoreCase(String)
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    // IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the first index within a String, handling <code>null</code>.
     * This method uses {@link String#indexOf(int)}.</p>
     * <p/>
     * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.indexOf(null, *)         = -1
     * StrUtils.indexOf("", *)           = -1
     * StrUtils.indexOf("aabaabaa", 'a') = 0
     * StrUtils.indexOf("aabaabaa", 'b') = 2
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param searchChar the character to find
     * @return the first index of the search character,
     * -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, char searchChar) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.indexOf(searchChar);
    }

    /**
     * <p>Finds the first index within a String from a start position,
     * handling <code>null</code>.
     * This method uses {@link String#indexOf(int, int)}.</p>
     * <p/>
     * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.
     * A negative start position is treated as zero.
     * A start position greater than the string length returns <code>-1</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.indexOf(null, *, *)          = -1
     * StrUtils.indexOf("", *, *)            = -1
     * StrUtils.indexOf("aabaabaa", 'b', 0)  = 2
     * StrUtils.indexOf("aabaabaa", 'b', 3)  = 5
     * StrUtils.indexOf("aabaabaa", 'b', 9)  = -1
     * StrUtils.indexOf("aabaabaa", 'b', -1) = 2
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param searchChar the character to find
     * @param startPos   the start position, negative treated as zero
     * @return the first index of the search character,
     * -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, char searchChar, int startPos) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.indexOf(searchChar, startPos);
    }

    /**
     * <p>Finds the first index within a String, handling <code>null</code>.
     * This method uses {@link String#indexOf(String)}.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>-1</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.indexOf(null, *)          = -1
     * StrUtils.indexOf(*, null)          = -1
     * StrUtils.indexOf("", "")           = 0
     * StrUtils.indexOf("aabaabaa", "a")  = 0
     * StrUtils.indexOf("aabaabaa", "b")  = 2
     * StrUtils.indexOf("aabaabaa", "ab") = 1
     * StrUtils.indexOf("aabaabaa", "")   = 0
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @return the first index of the search String,
     * -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }

    /**
     * <p>Finds the n-th index within a String, handling <code>null</code>.
     * This method uses {@link String#indexOf(String)}.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>-1</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.ordinalIndexOf(null, *, *)          = -1
     * StrUtils.ordinalIndexOf(*, null, *)          = -1
     * StrUtils.ordinalIndexOf("", "", *)           = 0
     * StrUtils.ordinalIndexOf("aabaabaa", "a", 1)  = 0
     * StrUtils.ordinalIndexOf("aabaabaa", "a", 2)  = 1
     * StrUtils.ordinalIndexOf("aabaabaa", "b", 1)  = 2
     * StrUtils.ordinalIndexOf("aabaabaa", "b", 2)  = 5
     * StrUtils.ordinalIndexOf("aabaabaa", "ab", 1) = 1
     * StrUtils.ordinalIndexOf("aabaabaa", "ab", 2) = 4
     * StrUtils.ordinalIndexOf("aabaabaa", "", 1)   = 0
     * StrUtils.ordinalIndexOf("aabaabaa", "", 2)   = 0
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @param ordinal   the n-th <code>searchStr</code> to find
     * @return the n-th index of the search String,
     * <code>-1</code> (<code>INDEX_NOT_FOUND</code>) if no match or <code>null</code> string input
     * @since 2.1
     */
    public static int ordinalIndexOf(String str, String searchStr, int ordinal) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return 0;
        }
        int found = 0;
        int index = INDEX_NOT_FOUND;
        do {
            index = str.indexOf(searchStr, index + 1);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    /**
     * <p>Finds the first index within a String, handling <code>null</code>.
     * This method uses {@link String#indexOf(String, int)}.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A negative start position is treated as zero.
     * An empty ("") search String always matches.
     * A start position greater than the string length only matches
     * an empty search String.</p>
     * <p/>
     * <pre>
     * StrUtils.indexOf(null, *, *)          = -1
     * StrUtils.indexOf(*, null, *)          = -1
     * StrUtils.indexOf("", "", 0)           = 0
     * StrUtils.indexOf("aabaabaa", "a", 0)  = 0
     * StrUtils.indexOf("aabaabaa", "b", 0)  = 2
     * StrUtils.indexOf("aabaabaa", "ab", 0) = 1
     * StrUtils.indexOf("aabaabaa", "b", 3)  = 5
     * StrUtils.indexOf("aabaabaa", "b", 9)  = -1
     * StrUtils.indexOf("aabaabaa", "b", -1) = 2
     * StrUtils.indexOf("aabaabaa", "", 2)   = 2
     * StrUtils.indexOf("abc", "", 9)        = 3
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search String,
     * -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        // JDK1.2/JDK1.3 have a bug, when startPos > str.length for "", hence
        if (searchStr.length() == 0 && startPos >= str.length()) {
            return str.length();
        }
        return str.indexOf(searchStr, startPos);
    }

    // LastIndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the last index within a String, handling <code>null</code>.
     * This method uses {@link String#lastIndexOf(int)}.</p>
     * <p/>
     * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.lastIndexOf(null, *)         = -1
     * StrUtils.lastIndexOf("", *)           = -1
     * StrUtils.lastIndexOf("aabaabaa", 'a') = 7
     * StrUtils.lastIndexOf("aabaabaa", 'b') = 5
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param searchChar the character to find
     * @return the last index of the search character,
     * -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, char searchChar) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(searchChar);
    }

    /**
     * <p>Finds the last index within a String from a start position,
     * handling <code>null</code>.
     * This method uses {@link String#lastIndexOf(int, int)}.</p>
     * <p/>
     * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.
     * A negative start position returns <code>-1</code>.
     * A start position greater than the string length searches the whole string.</p>
     * <p/>
     * <pre>
     * StrUtils.lastIndexOf(null, *, *)          = -1
     * StrUtils.lastIndexOf("", *,  *)           = -1
     * StrUtils.lastIndexOf("aabaabaa", 'b', 8)  = 5
     * StrUtils.lastIndexOf("aabaabaa", 'b', 4)  = 2
     * StrUtils.lastIndexOf("aabaabaa", 'b', 0)  = -1
     * StrUtils.lastIndexOf("aabaabaa", 'b', 9)  = 5
     * StrUtils.lastIndexOf("aabaabaa", 'b', -1) = -1
     * StrUtils.lastIndexOf("aabaabaa", 'a', 0)  = 0
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param searchChar the character to find
     * @param startPos   the start position
     * @return the last index of the search character,
     * -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, char searchChar, int startPos) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(searchChar, startPos);
    }

    /**
     * <p>Finds the last index within a String, handling <code>null</code>.
     * This method uses {@link String#lastIndexOf(String)}.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>-1</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.lastIndexOf(null, *)          = -1
     * StrUtils.lastIndexOf(*, null)          = -1
     * StrUtils.lastIndexOf("", "")           = 0
     * StrUtils.lastIndexOf("aabaabaa", "a")  = 0
     * StrUtils.lastIndexOf("aabaabaa", "b")  = 2
     * StrUtils.lastIndexOf("aabaabaa", "ab") = 1
     * StrUtils.lastIndexOf("aabaabaa", "")   = 8
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @return the last index of the search String,
     * -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr);
    }

    /**
     * <p>Finds the first index within a String, handling <code>null</code>.
     * This method uses {@link String#lastIndexOf(String, int)}.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A negative start position returns <code>-1</code>.
     * An empty ("") search String always matches unless the start position is negative.
     * A start position greater than the string length searches the whole string.</p>
     * <p/>
     * <pre>
     * StrUtils.lastIndexOf(null, *, *)          = -1
     * StrUtils.lastIndexOf(*, null, *)          = -1
     * StrUtils.lastIndexOf("aabaabaa", "a", 8)  = 7
     * StrUtils.lastIndexOf("aabaabaa", "b", 8)  = 5
     * StrUtils.lastIndexOf("aabaabaa", "ab", 8) = 4
     * StrUtils.lastIndexOf("aabaabaa", "b", 9)  = 5
     * StrUtils.lastIndexOf("aabaabaa", "b", -1) = -1
     * StrUtils.lastIndexOf("aabaabaa", "a", 0)  = 0
     * StrUtils.lastIndexOf("aabaabaa", "b", 0)  = -1
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search String,
     * -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr, startPos);
    }

    // Contains
    //-----------------------------------------------------------------------

    /**
     * <p>Checks if String contains a search character, handling <code>null</code>.
     * This method uses {@link String#indexOf(int)}.</p>
     * <p/>
     * <p>A <code>null</code> or empty ("") String will return <code>false</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.contains(null, *)    = false
     * StrUtils.contains("", *)      = false
     * StrUtils.contains("abc", 'a') = true
     * StrUtils.contains("abc", 'z') = false
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param searchChar the character to find
     * @return true if the String contains the search character,
     * false if not or <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, char searchChar) {
        if (isEmpty(str)) {
            return false;
        }
        return str.indexOf(searchChar) >= 0;
    }

    /**
     * <p>Checks if String contains a search String, handling <code>null</code>.
     * This method uses {@link String#indexOf(String)}.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>false</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.contains(null, *)     = false
     * StrUtils.contains(*, null)     = false
     * StrUtils.contains("", "")      = true
     * StrUtils.contains("abc", "")   = true
     * StrUtils.contains("abc", "a")  = true
     * StrUtils.contains("abc", "z")  = false
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @return true if the String contains the search String,
     * false if not or <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.indexOf(searchStr) >= 0;
    }

    /**
     * <p>Checks if String contains a search String irrespective of case,
     * handling <code>null</code>. This method uses
     * {@link #contains(String, String)}.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>false</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.contains(null, *) = false
     * StrUtils.contains(*, null) = false
     * StrUtils.contains("", "") = true
     * StrUtils.contains("abc", "") = true
     * StrUtils.contains("abc", "a") = true
     * StrUtils.contains("abc", "z") = false
     * StrUtils.contains("abc", "A") = true
     * StrUtils.contains("abc", "Z") = false
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @return true if the String contains the search String irrespective of
     * case or false if not or <code>null</code> string input
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return contains(str.toUpperCase(), searchStr.toUpperCase());
    }

    // ContainsAny
    //-----------------------------------------------------------------------

    /**
     * <p>Checks if the String contains any character in the given
     * set of characters.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>false</code>.
     * A <code>null</code> or zero length search array will return <code>false</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.containsAny(null, *)                = false
     * StrUtils.containsAny("", *)                  = false
     * StrUtils.containsAny(*, null)                = false
     * StrUtils.containsAny(*, [])                  = false
     * StrUtils.containsAny("zzabyycdxx",['z','a']) = true
     * StrUtils.containsAny("zzabyycdxx",['b','y']) = true
     * StrUtils.containsAny("aba", ['z'])           = false
     * </pre>
     *
     * @param str         the String to check, may be null
     * @param searchChars the chars to search for, may be null
     * @return the <code>true</code> if any of the chars are found,
     * <code>false</code> if no match or null input
     * @since 2.4
     */
    public static boolean containsAny(String str, char[] searchChars) {
        if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            for (int j = 0; j < searchChars.length; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * Checks if the String contains any character in the given set of characters.
     * </p>
     * <p/>
     * <p>
     * A <code>null</code> String will return <code>false</code>. A <code>null</code> search string will return
     * <code>false</code>.
     * </p>
     * <p/>
     * <pre>
     * StrUtils.containsAny(null, *)            = false
     * StrUtils.containsAny("", *)              = false
     * StrUtils.containsAny(*, null)            = false
     * StrUtils.containsAny(*, "")              = false
     * StrUtils.containsAny("zzabyycdxx", "za") = true
     * StrUtils.containsAny("zzabyycdxx", "by") = true
     * StrUtils.containsAny("aba","z")          = false
     * </pre>
     *
     * @param str         the String to check, may be null
     * @param searchChars the chars to search for, may be null
     * @return the <code>true</code> if any of the chars are found, <code>false</code> if no match or null input
     * @since 2.4
     */
    public static boolean containsAny(String str, String searchChars) {
        if (searchChars == null) {
            return false;
        }
        return containsAny(str, searchChars.toCharArray());
    }

    /**
     * <p>Search a String to find the first index of any
     * character not in the given set of characters.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> search string will return <code>-1</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.indexOfAnyBut(null, *)            = -1
     * StrUtils.indexOfAnyBut("", *)              = -1
     * StrUtils.indexOfAnyBut(*, null)            = -1
     * StrUtils.indexOfAnyBut(*, "")              = -1
     * StrUtils.indexOfAnyBut("zzabyycdxx", "za") = 3
     * StrUtils.indexOfAnyBut("zzabyycdxx", "")   = 0
     * StrUtils.indexOfAnyBut("aba","ab")         = -1
     * </pre>
     *
     * @param str         the String to check, may be null
     * @param searchChars the chars to search for, may be null
     * @return the index of any of the chars, -1 if no match or null input
     * @since 2.0
     */
    public static int indexOfAnyBut(String str, String searchChars) {
        if (isEmpty(str) || isEmpty(searchChars)) {
            return -1;
        }
        for (int i = 0; i < str.length(); i++) {
            if (searchChars.indexOf(str.charAt(i)) < 0) {
                return i;
            }
        }
        return -1;
    }

    // ContainsNone
    //-----------------------------------------------------------------------

    /**
     * <p>Checks that the String does not contain certain characters.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>true</code>.
     * A <code>null</code> invalid character array will return <code>true</code>.
     * An empty String ("") always returns true.</p>
     * <p/>
     * <pre>
     * StrUtils.containsNone(null, *)       = true
     * StrUtils.containsNone(*, null)       = true
     * StrUtils.containsNone("", *)         = true
     * StrUtils.containsNone("ab", '')      = true
     * StrUtils.containsNone("abab", 'xyz') = true
     * StrUtils.containsNone("ab1", 'xyz')  = true
     * StrUtils.containsNone("abz", 'xyz')  = false
     * </pre>
     *
     * @param str          the String to check, may be null
     * @param invalidChars an array of invalid chars, may be null
     * @return true if it contains none of the invalid chars, or is null
     * @since 2.0
     */
    public static boolean containsNone(String str, char[] invalidChars) {
        if (str == null || invalidChars == null) {
            return true;
        }
        int strSize = str.length();
        int validSize = invalidChars.length;
        for (int i = 0; i < strSize; i++) {
            char ch = str.charAt(i);
            for (int j = 0; j < validSize; j++) {
                if (invalidChars[j] == ch) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * <p>Checks that the String does not contain certain characters.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>true</code>.
     * A <code>null</code> invalid character array will return <code>true</code>.
     * An empty String ("") always returns true.</p>
     * <p/>
     * <pre>
     * StrUtils.containsNone(null, *)       = true
     * StrUtils.containsNone(*, null)       = true
     * StrUtils.containsNone("", *)         = true
     * StrUtils.containsNone("ab", "")      = true
     * StrUtils.containsNone("abab", "xyz") = true
     * StrUtils.containsNone("ab1", "xyz")  = true
     * StrUtils.containsNone("abz", "xyz")  = false
     * </pre>
     *
     * @param str          the String to check, may be null
     * @param invalidChars a String of invalid chars, may be null
     * @return true if it contains none of the invalid chars, or is null
     * @since 2.0
     */
    public static boolean containsNone(String str, String invalidChars) {
        if (str == null || invalidChars == null) {
            return true;
        }
        return containsNone(str, invalidChars.toCharArray());
    }

    // IndexOfAny strings
    //-----------------------------------------------------------------------

    /**
     * <p>Find the first index of any of a set of potential substrings.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> or zero length search array will return <code>-1</code>.
     * A <code>null</code> search array entry will be ignored, but a search
     * array containing "" will return <code>0</code> if <code>str</code> is not
     * null. This method uses {@link String#indexOf(String)}.</p>
     * <p/>
     * <pre>
     * StrUtils.indexOfAny(null, *)                     = -1
     * StrUtils.indexOfAny(*, null)                     = -1
     * StrUtils.indexOfAny(*, [])                       = -1
     * StrUtils.indexOfAny("zzabyycdxx", ["ab","cd"])   = 2
     * StrUtils.indexOfAny("zzabyycdxx", ["cd","ab"])   = 2
     * StrUtils.indexOfAny("zzabyycdxx", ["mn","op"])   = -1
     * StrUtils.indexOfAny("zzabyycdxx", ["zab","aby"]) = 1
     * StrUtils.indexOfAny("zzabyycdxx", [""])          = 0
     * StrUtils.indexOfAny("", [""])                    = 0
     * StrUtils.indexOfAny("", ["a"])                   = -1
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param searchStrs the Strings to search for, may be null
     * @return the first index of any of the searchStrs in str, -1 if no match
     */
    public static int indexOfAny(String str, String[] searchStrs) {
        if ((str == null) || (searchStrs == null)) {
            return -1;
        }
        int sz = searchStrs.length;

        // String's can't have a MAX_VALUEth index.
        int ret = Integer.MAX_VALUE;

        int tmp = 0;
        for (int i = 0; i < sz; i++) {
            String search = searchStrs[i];
            if (search == null) {
                continue;
            }
            tmp = str.indexOf(search);
            if (tmp == -1) {
                continue;
            }

            if (tmp < ret) {
                ret = tmp;
            }
        }

        return (ret == Integer.MAX_VALUE) ? -1 : ret;
    }

    /**
     * <p>Find the latest index of any of a set of potential substrings.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> search array will return <code>-1</code>.
     * A <code>null</code> or zero length search array entry will be ignored,
     * but a search array containing "" will return the length of <code>str</code>
     * if <code>str</code> is not null. This method uses {@link String#indexOf(String)}</p>
     * <p/>
     * <pre>
     * StrUtils.lastIndexOfAny(null, *)                   = -1
     * StrUtils.lastIndexOfAny(*, null)                   = -1
     * StrUtils.lastIndexOfAny(*, [])                     = -1
     * StrUtils.lastIndexOfAny(*, [null])                 = -1
     * StrUtils.lastIndexOfAny("zzabyycdxx", ["ab","cd"]) = 6
     * StrUtils.lastIndexOfAny("zzabyycdxx", ["cd","ab"]) = 6
     * StrUtils.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
     * StrUtils.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
     * StrUtils.lastIndexOfAny("zzabyycdxx", ["mn",""])   = 10
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param searchStrs the Strings to search for, may be null
     * @return the last index of any of the Strings, -1 if no match
     */
    public static int lastIndexOfAny(String str, String[] searchStrs) {
        if ((str == null) || (searchStrs == null)) {
            return -1;
        }
        int sz = searchStrs.length;
        int ret = -1;
        int tmp = 0;
        for (int i = 0; i < sz; i++) {
            String search = searchStrs[i];
            if (search == null) {
                continue;
            }
            tmp = str.lastIndexOf(search);
            if (tmp > ret) {
                ret = tmp;
            }
        }
        return ret;
    }

    // Substring
    //-----------------------------------------------------------------------

    /**
     * <p>Gets a substring from the specified String avoiding exceptions.</p>
     * <p/>
     * <p>A negative start position can be used to start <code>n</code>
     * characters from the end of the String.</p>
     * <p/>
     * <p>A <code>null</code> String will return <code>null</code>.
     * An empty ("") String will return "".</p>
     * <p/>
     * <pre>
     * StrUtils.substring(null, *)   = null
     * StrUtils.substring("", *)     = ""
     * StrUtils.substring("abc", 0)  = "abc"
     * StrUtils.substring("abc", 2)  = "c"
     * StrUtils.substring("abc", 4)  = ""
     * StrUtils.substring("abc", -2) = "bc"
     * StrUtils.substring("abc", -4) = "abc"
     * </pre>
     *
     * @param str   the String to get the substring from, may be null
     * @param start the position to start from, negative means
     *              count back from the end of the String by this many characters
     * @return substring from start position, <code>null</code> if null String input
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }

        return str.substring(start);
    }

    /**
     * <p>Gets a substring from the specified String avoiding exceptions.</p>
     * <p/>
     * <p>A negative start position can be used to start/end <code>n</code>
     * characters from the end of the String.</p>
     * <p/>
     * <p>The returned substring starts with the character in the <code>start</code>
     * position and ends before the <code>end</code> position. All position counting is
     * zero-based -- i.e., to start at the beginning of the string use
     * <code>start = 0</code>. Negative start and end positions can be used to
     * specify offsets relative to the end of the String.</p>
     * <p/>
     * <p>If <code>start</code> is not strictly to the left of <code>end</code>, ""
     * is returned.</p>
     * <p/>
     * <pre>
     * StrUtils.substring(null, *, *)    = null
     * StrUtils.substring("", * ,  *)    = "";
     * StrUtils.substring("abc", 0, 2)   = "ab"
     * StrUtils.substring("abc", 2, 0)   = ""
     * StrUtils.substring("abc", 2, 4)   = "c"
     * StrUtils.substring("abc", 4, 6)   = ""
     * StrUtils.substring("abc", 2, 2)   = ""
     * StrUtils.substring("abc", -2, -1) = "b"
     * StrUtils.substring("abc", -4, 2)  = "ab"
     * </pre>
     *
     * @param str   the String to get the substring from, may be null
     * @param start the position to start from, negative means
     *              count back from the end of the String by this many characters
     * @param end   the position to end at (exclusive), negative means
     *              count back from the end of the String by this many characters
     * @return substring from start position to end positon,
     * <code>null</code> if null String input
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    // Left/Right/Mid
    //-----------------------------------------------------------------------

    /**
     * <p>Gets the leftmost <code>len</code> characters of a String.</p>
     * <p/>
     * <p>If <code>len</code> characters are not available, or the
     * String is <code>null</code>, the String will be returned without
     * an exception. An exception is thrown if len is negative.</p>
     * <p/>
     * <pre>
     * StrUtils.left(null, *)    = null
     * StrUtils.left(*, -ve)     = ""
     * StrUtils.left("", *)      = ""
     * StrUtils.left("abc", 0)   = ""
     * StrUtils.left("abc", 2)   = "ab"
     * StrUtils.left("abc", 4)   = "abc"
     * </pre>
     *
     * @param str the String to get the leftmost characters from, may be null
     * @param len the length of the required String, must be zero or positive
     * @return the leftmost characters, <code>null</code> if null String input
     */
    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /**
     * <p>Gets the rightmost <code>len</code> characters of a String.</p>
     * <p/>
     * <p>If <code>len</code> characters are not available, or the String
     * is <code>null</code>, the String will be returned without an
     * an exception. An exception is thrown if len is negative.</p>
     * <p/>
     * <pre>
     * StrUtils.right(null, *)    = null
     * StrUtils.right(*, -ve)     = ""
     * StrUtils.right("", *)      = ""
     * StrUtils.right("abc", 0)   = ""
     * StrUtils.right("abc", 2)   = "bc"
     * StrUtils.right("abc", 4)   = "abc"
     * </pre>
     *
     * @param str the String to get the rightmost characters from, may be null
     * @param len the length of the required String, must be zero or positive
     * @return the rightmost characters, <code>null</code> if null String input
     */
    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    /**
     * <p>Gets <code>len</code> characters from the middle of a String.</p>
     * <p/>
     * <p>If <code>len</code> characters are not available, the remainder
     * of the String will be returned without an exception. If the
     * String is <code>null</code>, <code>null</code> will be returned.
     * An exception is thrown if len is negative.</p>
     * <p/>
     * <pre>
     * StrUtils.mid(null, *, *)    = null
     * StrUtils.mid(*, *, -ve)     = ""
     * StrUtils.mid("", 0, *)      = ""
     * StrUtils.mid("abc", 0, 2)   = "ab"
     * StrUtils.mid("abc", 0, 4)   = "abc"
     * StrUtils.mid("abc", 2, 4)   = "c"
     * StrUtils.mid("abc", 4, 2)   = ""
     * StrUtils.mid("abc", -2, 2)  = "ab"
     * </pre>
     *
     * @param str the String to get the characters from, may be null
     * @param pos the position to start from, negative treated as zero
     * @param len the length of the required String, must be zero or positive
     * @return the middle characters, <code>null</code> if null String input
     */
    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return EMPTY;
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= (pos + len)) {
            return str.substring(pos);
        }
        return str.substring(pos, pos + len);
    }

    // SubStringAfter/SubStringBefore
    //-----------------------------------------------------------------------

    /**
     * <p>Gets the substring before the first occurrence of a separator.
     * The separator is not returned.</p>
     * <p/>
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * A <code>null</code> separator will return the input string.</p>
     * <p/>
     * <pre>
     * StrUtils.substringBefore(null, *)      = null
     * StrUtils.substringBefore("", *)        = ""
     * StrUtils.substringBefore("abc", "a")   = ""
     * StrUtils.substringBefore("abcba", "b") = "a"
     * StrUtils.substringBefore("abc", "c")   = "ab"
     * StrUtils.substringBefore("abc", "d")   = "abc"
     * StrUtils.substringBefore("abc", "")    = ""
     * StrUtils.substringBefore("abc", null)  = "abc"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring before the first occurrence of the separator,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * <p>Gets the substring after the first occurrence of a separator.
     * The separator is not returned.</p>
     * <p/>
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * A <code>null</code> separator will return the empty string if the
     * input string is not <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.substringAfter(null, *)      = null
     * StrUtils.substringAfter("", *)        = ""
     * StrUtils.substringAfter(*, null)      = ""
     * StrUtils.substringAfter("abc", "a")   = "bc"
     * StrUtils.substringAfter("abcba", "b") = "cba"
     * StrUtils.substringAfter("abc", "c")   = ""
     * StrUtils.substringAfter("abc", "d")   = ""
     * StrUtils.substringAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring after the first occurrence of the separator,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>Gets the substring before the last occurrence of a separator.
     * The separator is not returned.</p>
     * <p/>
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * An empty or <code>null</code> separator will return the input string.</p>
     * <p/>
     * <pre>
     * StrUtils.substringBeforeLast(null, *)      = null
     * StrUtils.substringBeforeLast("", *)        = ""
     * StrUtils.substringBeforeLast("abcba", "b") = "abc"
     * StrUtils.substringBeforeLast("abc", "c")   = "ab"
     * StrUtils.substringBeforeLast("a", "a")     = ""
     * StrUtils.substringBeforeLast("a", "z")     = "a"
     * StrUtils.substringBeforeLast("a", null)    = "a"
     * StrUtils.substringBeforeLast("a", "")      = "a"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring before the last occurrence of the separator,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * <p>Gets the substring after the last occurrence of a separator.
     * The separator is not returned.</p>
     * <p/>
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * An empty or <code>null</code> separator will return the empty string if
     * the input string is not <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.substringAfterLast(null, *)      = null
     * StrUtils.substringAfterLast("", *)        = ""
     * StrUtils.substringAfterLast(*, "")        = ""
     * StrUtils.substringAfterLast(*, null)      = ""
     * StrUtils.substringAfterLast("abc", "a")   = "bc"
     * StrUtils.substringAfterLast("abcba", "b") = "a"
     * StrUtils.substringAfterLast("abc", "c")   = ""
     * StrUtils.substringAfterLast("a", "a")     = ""
     * StrUtils.substringAfterLast("a", "z")     = ""
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring after the last occurrence of the separator,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == (str.length() - separator.length())) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    // Substring between
    //-----------------------------------------------------------------------

    /**
     * <p>Gets the String that is nested in between two instances of the
     * same String.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> tag returns <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.substringBetween(null, *)            = null
     * StrUtils.substringBetween("", "")             = ""
     * StrUtils.substringBetween("", "tag")          = null
     * StrUtils.substringBetween("tagabctag", null)  = null
     * StrUtils.substringBetween("tagabctag", "")    = ""
     * StrUtils.substringBetween("tagabctag", "tag") = "abc"
     * </pre>
     *
     * @param str the String containing the substring, may be null
     * @param tag the String before and after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @since 2.0
     */
    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag);
    }

    /**
     * <p>Gets the String that is nested in between two Strings.
     * Only the first match is returned.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> open/close returns <code>null</code> (no match).
     * An empty ("") open and close returns an empty string.</p>
     * <p/>
     * <pre>
     * StrUtils.substringBetween("wx[b]yz", "[", "]") = "b"
     * StrUtils.substringBetween(null, *, *)          = null
     * StrUtils.substringBetween(*, null, *)          = null
     * StrUtils.substringBetween(*, *, null)          = null
     * StrUtils.substringBetween("", "", "")          = ""
     * StrUtils.substringBetween("", "", "]")         = null
     * StrUtils.substringBetween("", "[", "]")        = null
     * StrUtils.substringBetween("yabcz", "", "")     = ""
     * StrUtils.substringBetween("yabcz", "y", "z")   = "abc"
     * StrUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str   the String containing the substring, may be null
     * @param open  the String before the substring, may be null
     * @param close the String after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @since 2.0
     */
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    // Nested extraction
    //-----------------------------------------------------------------------

    /**
     * <p>Gets the String that is nested in between two instances of the
     * same String.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> tag returns <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.getNestedString(null, *)            = null
     * StrUtils.getNestedString("", "")             = ""
     * StrUtils.getNestedString("", "tag")          = null
     * StrUtils.getNestedString("tagabctag", null)  = null
     * StrUtils.getNestedString("tagabctag", "")    = ""
     * StrUtils.getNestedString("tagabctag", "tag") = "abc"
     * </pre>
     *
     * @param str the String containing nested-string, may be null
     * @param tag the String before and after nested-string, may be null
     * @return the nested String, <code>null</code> if no match
     * @deprecated Use the better named {@link #substringBetween(String, String)}.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String getNestedString(String str, String tag) {
        return substringBetween(str, tag, tag);
    }

    /**
     * <p>Gets the String that is nested in between two Strings.
     * Only the first match is returned.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> open/close returns <code>null</code> (no match).
     * An empty ("") open/close returns an empty string.</p>
     * <p/>
     * <pre>
     * StrUtils.getNestedString(null, *, *)          = null
     * StrUtils.getNestedString("", "", "")          = ""
     * StrUtils.getNestedString("", "", "tag")       = null
     * StrUtils.getNestedString("", "tag", "tag")    = null
     * StrUtils.getNestedString("yabcz", null, null) = null
     * StrUtils.getNestedString("yabcz", "", "")     = ""
     * StrUtils.getNestedString("yabcz", "y", "z")   = "abc"
     * StrUtils.getNestedString("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str   the String containing nested-string, may be null
     * @param open  the String before nested-string, may be null
     * @param close the String after nested-string, may be null
     * @return the nested String, <code>null</code> if no match
     * @deprecated Use the better named {@link #substringBetween(String, String, String)}.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String getNestedString(String str, String open, String close) {
        return substringBetween(str, open, close);
    }

    // Joining
    //-----------------------------------------------------------------------

    /**
     * <p>Concatenates elements of an array into a single String.
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     * <p/>
     * <pre>
     * StrUtils.concatenate(null)            = null
     * StrUtils.concatenate([])              = ""
     * StrUtils.concatenate([null])          = ""
     * StrUtils.concatenate(["a", "b", "c"]) = "abc"
     * StrUtils.concatenate([null, "", "a"]) = "a"
     * </pre>
     *
     * @param array the array of values to concatenate, may be null
     * @return the concatenated String, <code>null</code> if null array input
     * @deprecated Use the better named {@link #join(Object[])} instead.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String concatenate(Object[] array) {
        return join(array, null);
    }

    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     * <p/>
     * <p>No separator is added to the joined String.
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     * <p/>
     * <pre>
     * StrUtils.join(null)            = null
     * StrUtils.join([])              = ""
     * StrUtils.join([null])          = ""
     * StrUtils.join(["a", "b", "c"]) = "abc"
     * StrUtils.join([null, "", "a"]) = "a"
     * </pre>
     *
     * @param array the array of values to join together, may be null
     * @return the joined String, <code>null</code> if null array input
     * @since 2.0
     */
    public static String join(Object[] array) {
        return join(array, null);
    }

    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     * <p/>
     * <p>No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     * <p/>
     * <pre>
     * StrUtils.join(null, *)               = null
     * StrUtils.join([], *)                 = ""
     * StrUtils.join([null], *)             = ""
     * StrUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StrUtils.join(["a", "b", "c"], null) = "abc"
     * StrUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, <code>null</code> if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }

        return join(array, separator, 0, array.length);
    }

    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     * <p/>
     * <p>No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     * <p/>
     * <pre>
     * StrUtils.join(null, *)               = null
     * StrUtils.join([], *)                 = ""
     * StrUtils.join([null], *)             = ""
     * StrUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StrUtils.join(["a", "b", "c"], null) = "abc"
     * StrUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from.  It is
     *                   an error to pass in an end index past the end of the array
     * @param endIndex   the index to stop joining from (exclusive). It is
     *                   an error to pass in an end index past the end of the array
     * @return the joined String, <code>null</code> if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     * <p/>
     * <p>No delimiter is added before or after the list.
     * A <code>null</code> separator is the same as an empty String ("").
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     * <p/>
     * <pre>
     * StrUtils.join(null, *)                = null
     * StrUtils.join([], *)                  = ""
     * StrUtils.join([null], *)              = ""
     * StrUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StrUtils.join(["a", "b", "c"], null)  = "abc"
     * StrUtils.join(["a", "b", "c"], "")    = "abc"
     * StrUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, <code>null</code> if null array input
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     * <p/>
     * <p>No delimiter is added before or after the list.
     * A <code>null</code> separator is the same as an empty String ("").
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     * <p/>
     * <pre>
     * StrUtils.join(null, *)                = null
     * StrUtils.join([], *)                  = ""
     * StrUtils.join([null], *)              = ""
     * StrUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StrUtils.join(["a", "b", "c"], null)  = "abc"
     * StrUtils.join(["a", "b", "c"], "")    = "abc"
     * StrUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from.  It is
     *                   an error to pass in an end index past the end of the array
     * @param endIndex   the index to stop joining from (exclusive). It is
     *                   an error to pass in an end index past the end of the array
     * @return the joined String, <code>null</code> if null array input
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>Deletes all whitespaces from a String as defined by
     * {@link Character#isWhitespace(char)}.</p>
     * <p/>
     * <pre>
     * StrUtils.deleteWhitespace(null)         = null
     * StrUtils.deleteWhitespace("")           = ""
     * StrUtils.deleteWhitespace("abc")        = "abc"
     * StrUtils.deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str the String to delete whitespace from, may be null
     * @return the String without whitespaces, <code>null</code> if null String input
     */
    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }

    // Remove
    //-----------------------------------------------------------------------

    /**
     * <p>Removes a substring only if it is at the begining of a source string,
     * otherwise returns the source string.</p>
     * <p/>
     * <p>A <code>null</code> source string will return <code>null</code>.
     * An empty ("") source string will return the empty string.
     * A <code>null</code> search string will return the source string.</p>
     * <p/>
     * <pre>
     * StrUtils.removeStart(null, *)      = null
     * StrUtils.removeStart("", *)        = ""
     * StrUtils.removeStart(*, null)      = *
     * StrUtils.removeStart("www.domain.com", "www.")   = "domain.com"
     * StrUtils.removeStart("domain.com", "www.")       = "domain.com"
     * StrUtils.removeStart("www.domain.com", "domain") = "www.domain.com"
     * StrUtils.removeStart("abc", "")    = "abc"
     * </pre>
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for and remove, may be null
     * @return the substring with the string removed if found,
     * <code>null</code> if null String input
     * @since 2.1
     */
    public static String removeStart(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    /**
     * <p>Case insensitive removal of a substring if it is at the begining of a source string,
     * otherwise returns the source string.</p>
     * <p/>
     * <p>A <code>null</code> source string will return <code>null</code>.
     * An empty ("") source string will return the empty string.
     * A <code>null</code> search string will return the source string.</p>
     * <p/>
     * <pre>
     * StrUtils.removeStartIgnoreCase(null, *)      = null
     * StrUtils.removeStartIgnoreCase("", *)        = ""
     * StrUtils.removeStartIgnoreCase(*, null)      = *
     * StrUtils.removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
     * StrUtils.removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
     * StrUtils.removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
     * StrUtils.removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
     * StrUtils.removeStartIgnoreCase("abc", "")    = "abc"
     * </pre>
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for (case insensitive) and remove, may be null
     * @return the substring with the string removed if found,
     * <code>null</code> if null String input
     * @since 2.4
     */
    public static String removeStartIgnoreCase(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (startsWithIgnoreCase(str, remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    /**
     * <p>Removes a substring only if it is at the end of a source string,
     * otherwise returns the source string.</p>
     * <p/>
     * <p>A <code>null</code> source string will return <code>null</code>.
     * An empty ("") source string will return the empty string.
     * A <code>null</code> search string will return the source string.</p>
     * <p/>
     * <pre>
     * StrUtils.removeEnd(null, *)      = null
     * StrUtils.removeEnd("", *)        = ""
     * StrUtils.removeEnd(*, null)      = *
     * StrUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
     * StrUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
     * StrUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
     * StrUtils.removeEnd("abc", "")    = "abc"
     * </pre>
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for and remove, may be null
     * @return the substring with the string removed if found,
     * <code>null</code> if null String input
     * @since 2.1
     */
    public static String removeEnd(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * <p>Case insensitive removal of a substring if it is at the end of a source string,
     * otherwise returns the source string.</p>
     * <p/>
     * <p>A <code>null</code> source string will return <code>null</code>.
     * An empty ("") source string will return the empty string.
     * A <code>null</code> search string will return the source string.</p>
     * <p/>
     * <pre>
     * StrUtils.removeEnd(null, *)      = null
     * StrUtils.removeEnd("", *)        = ""
     * StrUtils.removeEnd(*, null)      = *
     * StrUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com."
     * StrUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
     * StrUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
     * StrUtils.removeEnd("abc", "")    = "abc"
     * </pre>
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for (case insensitive) and remove, may be null
     * @return the substring with the string removed if found,
     * <code>null</code> if null String input
     * @since 2.4
     */
    public static String removeEndIgnoreCase(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (endsWithIgnoreCase(str, remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * <p>Removes all occurrences of a substring from within the source string.</p>
     * <p/>
     * <p>A <code>null</code> source string will return <code>null</code>.
     * An empty ("") source string will return the empty string.
     * A <code>null</code> remove string will return the source string.
     * An empty ("") remove string will return the source string.</p>
     * <p/>
     * <pre>
     * StrUtils.remove(null, *)        = null
     * StrUtils.remove("", *)          = ""
     * StrUtils.remove(*, null)        = *
     * StrUtils.remove(*, "")          = *
     * StrUtils.remove("queued", "ue") = "qd"
     * StrUtils.remove("queued", "zz") = "queued"
     * </pre>
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for and remove, may be null
     * @return the substring with the string removed if found,
     * <code>null</code> if null String input
     * @since 2.1
     */
    public static String remove(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        return replace(str, remove, EMPTY, -1);
    }

    /**
     * <p>Removes all occurrences of a character from within the source string.</p>
     * <p/>
     * <p>A <code>null</code> source string will return <code>null</code>.
     * An empty ("") source string will return the empty string.</p>
     * <p/>
     * <pre>
     * StrUtils.remove(null, *)       = null
     * StrUtils.remove("", *)         = ""
     * StrUtils.remove("queued", 'u') = "qeed"
     * StrUtils.remove("queued", 'z') = "queued"
     * </pre>
     *
     * @param str    the source String to search, may be null
     * @param remove the char to search for and remove, may be null
     * @return the substring with the char removed if found,
     * <code>null</code> if null String input
     * @since 2.1
     */
    public static String remove(String str, char remove) {
        if (isEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    // Replacing
    //-----------------------------------------------------------------------

    /**
     * <p>Replaces a String with another String inside a larger String, once.</p>
     * <p/>
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     * <p/>
     * <pre>
     * StrUtils.replaceOnce(null, *, *)        = null
     * StrUtils.replaceOnce("", *, *)          = ""
     * StrUtils.replaceOnce("any", null, *)    = "any"
     * StrUtils.replaceOnce("any", *, null)    = "any"
     * StrUtils.replaceOnce("any", "", *)      = "any"
     * StrUtils.replaceOnce("aba", "a", null)  = "aba"
     * StrUtils.replaceOnce("aba", "a", "")    = "ba"
     * StrUtils.replaceOnce("aba", "a", "z")   = "zba"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace with, may be null
     * @return the text with any replacements processed,
     * <code>null</code> if null String input
     * @see #replace(String text, String searchString, String replacement, int max)
     */
    public static String replaceOnce(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * <p>Replaces all occurrences of a String within another String.</p>
     * <p/>
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     * <p/>
     * <pre>
     * StrUtils.replace(null, *, *)        = null
     * StrUtils.replace("", *, *)          = ""
     * StrUtils.replace("any", null, *)    = "any"
     * StrUtils.replace("any", *, null)    = "any"
     * StrUtils.replace("any", "", *)      = "any"
     * StrUtils.replace("aba", "a", null)  = "aba"
     * StrUtils.replace("aba", "a", "")    = "b"
     * StrUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     * <code>null</code> if null String input
     * @see #replace(String text, String searchString, String replacement, int max)
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first <code>max</code> values of the search String.</p>
     * <p/>
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     * <p/>
     * <pre>
     * StrUtils.replace(null, *, *, *)         = null
     * StrUtils.replace("", *, *, *)           = ""
     * StrUtils.replace("any", null, *, *)     = "any"
     * StrUtils.replace("any", *, null, *)     = "any"
     * StrUtils.replace("any", "", *, *)       = "any"
     * StrUtils.replace("any", *, *, 0)        = "any"
     * StrUtils.replace("abaa", "a", null, -1) = "abaa"
     * StrUtils.replace("abaa", "a", "", -1)   = "b"
     * StrUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StrUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StrUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StrUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max          maximum number of values to replace, or <code>-1</code> if no maximum
     * @return the text with any replacements processed,
     * <code>null</code> if null String input
     */
    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p/>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op, or if
     * any "search string" or "string to replace" is null, that replace will be
     * ignored. This will not repeat. For repeating replaces, call the
     * overloaded method.
     * </p>
     * <p/>
     * <pre>
     *  StrUtils.replaceEach(null, *, *)        = null
     *  StrUtils.replaceEach("", *, *)          = ""
     *  StrUtils.replaceEach("aba", null, null) = "aba"
     *  StrUtils.replaceEach("aba", new String[0], null) = "aba"
     *  StrUtils.replaceEach("aba", null, new String[0]) = "aba"
     *  StrUtils.replaceEach("aba", new String[]{"a"}, null)  = "aba"
     *  StrUtils.replaceEach("aba", new String[]{"a"}, new String[]{""})  = "b"
     *  StrUtils.replaceEach("aba", new String[]{null}, new String[]{"a"})  = "aba"
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"})  = "wcte"
     *  (example of how it does not repeat)
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"})  = "dcte"
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     * @throws IndexOutOfBoundsException if the lengths of the arrays are not the same (null is ok,
     *                                   and/or size 0)
     * @since 2.4
     */
    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p/>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op, or if
     * any "search string" or "string to replace" is null, that replace will be
     * ignored. This will not repeat. For repeating replaces, call the
     * overloaded method.
     * </p>
     * <p/>
     * <pre>
     *  StrUtils.replaceEach(null, *, *, *) = null
     *  StrUtils.replaceEach("", *, *, *) = ""
     *  StrUtils.replaceEach("aba", null, null, *) = "aba"
     *  StrUtils.replaceEach("aba", new String[0], null, *) = "aba"
     *  StrUtils.replaceEach("aba", null, new String[0], *) = "aba"
     *  StrUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  StrUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  StrUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, true) = IllegalArgumentException
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, false) = "dcabe"
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     * @throws IllegalArgumentException  if the search is repeating and there is an endless loop due
     *                                   to outputs of one being inputs to another
     * @throws IndexOutOfBoundsException if the lengths of the arrays are not the same (null is ok,
     *                                   and/or size 0)
     * @since 2.4
     */
    public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        // timeToLive should be 0 if not used or nothing to replace, else it's
        // the length of the replace array
        int timeToLive = searchList == null ? 0 : searchList.length;
        return replaceEach(text, searchList, replacementList, true, timeToLive);
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p/>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op, or if
     * any "search string" or "string to replace" is null, that replace will be
     * ignored.
     * </p>
     * <p/>
     * <pre>
     *  StrUtils.replaceEach(null, *, *, *) = null
     *  StrUtils.replaceEach("", *, *, *) = ""
     *  StrUtils.replaceEach("aba", null, null, *) = "aba"
     *  StrUtils.replaceEach("aba", new String[0], null, *) = "aba"
     *  StrUtils.replaceEach("aba", null, new String[0], *) = "aba"
     *  StrUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  StrUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  StrUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  StrUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *) = IllegalArgumentException
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @param repeat          if true, then replace repeatedly
     *                        until there are no more possible replacements or timeToLive < 0
     * @param timeToLive      if less than 0 then there is a circular reference and endless
     *                        loop
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     * @throws IllegalArgumentException  if the search is repeating and there is an endless loop due
     *                                   to outputs of one being inputs to another
     * @throws IndexOutOfBoundsException if the lengths of the arrays are not the same (null is ok,
     *                                   and/or size 0)
     * @since 2.4
     */
    private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat,
                                      int timeToLive) {

        // mchyzer Performance note: This creates very few new objects (one major goal)
        // let me know if there are performance requests, we can create a harness to measure

        if (text == null || text.length() == 0 || searchList == null || searchList.length == 0
            || replacementList == null || replacementList.length == 0) {
            return text;
        }

        // if recursing, this shouldnt be less than 0
        if (timeToLive < 0) {
            throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
        }

        int searchLength = searchList.length;
        int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs "
                                               + replacementLength);
        }

        // keep track of which still have matches
        boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
                || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesnt have to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);

        StringBuffer buf = new StringBuffer(text.length() + increase);

        while (textIndex != -1) {

            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
                    || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        String result = buf.toString();
        if (!repeat) {
            return result;
        }

        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }

    // Replace, character based
    //-----------------------------------------------------------------------

    /**
     * <p>Replaces all occurrences of a character in a String with another.
     * This is a null-safe version of {@link String#replace(char, char)}.</p>
     * <p/>
     * <p>A <code>null</code> string input returns <code>null</code>.
     * An empty ("") string input returns an empty string.</p>
     * <p/>
     * <pre>
     * StrUtils.replaceChars(null, *, *)        = null
     * StrUtils.replaceChars("", *, *)          = ""
     * StrUtils.replaceChars("abcba", 'b', 'y') = "aycya"
     * StrUtils.replaceChars("abcba", 'z', 'y') = "abcba"
     * </pre>
     *
     * @param str         String to replace characters in, may be null
     * @param searchChar  the character to search for, may be null
     * @param replaceChar the character to replace, may be null
     * @return modified String, <code>null</code> if null string input
     * @since 2.0
     */
    public static String replaceChars(String str, char searchChar, char replaceChar) {
        if (str == null) {
            return null;
        }
        return str.replace(searchChar, replaceChar);
    }

    /**
     * <p>Replaces multiple characters in a String in one go.
     * This method can also be used to delete characters.</p>
     * <p/>
     * <p>For example:<br />
     * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>.</p>
     * <p/>
     * <p>A <code>null</code> string input returns <code>null</code>.
     * An empty ("") string input returns an empty string.
     * A null or empty set of search characters returns the input string.</p>
     * <p/>
     * <p>The length of the search characters should normally equal the length
     * of the replace characters.
     * If the search characters is longer, then the extra search characters
     * are deleted.
     * If the search characters is shorter, then the extra replace characters
     * are ignored.</p>
     * <p/>
     * <pre>
     * StrUtils.replaceChars(null, *, *)           = null
     * StrUtils.replaceChars("", *, *)             = ""
     * StrUtils.replaceChars("abc", null, *)       = "abc"
     * StrUtils.replaceChars("abc", "", *)         = "abc"
     * StrUtils.replaceChars("abc", "b", null)     = "ac"
     * StrUtils.replaceChars("abc", "b", "")       = "ac"
     * StrUtils.replaceChars("abcba", "bc", "yz")  = "ayzya"
     * StrUtils.replaceChars("abcba", "bc", "y")   = "ayya"
     * StrUtils.replaceChars("abcba", "bc", "yzx") = "ayzya"
     * </pre>
     *
     * @param str          String to replace characters in, may be null
     * @param searchChars  a set of characters to search for, may be null
     * @param replaceChars a set of characters to replace, may be null
     * @return modified String, <code>null</code> if null string input
     * @since 2.0
     */
    public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (isEmpty(str) || isEmpty(searchChars)) {
            return str;
        }
        if (replaceChars == null) {
            replaceChars = EMPTY;
        }
        boolean modified = false;
        int replaceCharsLength = replaceChars.length();
        int strLength = str.length();
        StringBuffer buf = new StringBuffer(strLength);
        for (int i = 0; i < strLength; i++) {
            char ch = str.charAt(i);
            int index = searchChars.indexOf(ch);
            if (index >= 0) {
                modified = true;
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
            } else {
                buf.append(ch);
            }
        }
        if (modified) {
            return buf.toString();
        }
        return str;
    }

    // Overlay
    //-----------------------------------------------------------------------

    /**
     * <p>Overlays part of a String with another String.</p>
     * <p/>
     * <pre>
     * StrUtils.overlayString(null, *, *, *)           = NullPointerException
     * StrUtils.overlayString(*, null, *, *)           = NullPointerException
     * StrUtils.overlayString("", "abc", 0, 0)         = "abc"
     * StrUtils.overlayString("abcdef", null, 2, 4)    = "abef"
     * StrUtils.overlayString("abcdef", "", 2, 4)      = "abef"
     * StrUtils.overlayString("abcdef", "zzzz", 2, 4)  = "abzzzzef"
     * StrUtils.overlayString("abcdef", "zzzz", 4, 2)  = "abcdzzzzcdef"
     * StrUtils.overlayString("abcdef", "zzzz", -1, 4) = IndexOutOfBoundsException
     * StrUtils.overlayString("abcdef", "zzzz", 2, 8)  = IndexOutOfBoundsException
     * </pre>
     *
     * @param text    the String to do overlaying in, may be null
     * @param overlay the String to overlay, may be null
     * @param start   the position to start overlaying at, must be valid
     * @param end     the position to stop overlaying before, must be valid
     * @return overlayed String, <code>null</code> if null String input
     * @throws NullPointerException      if text or overlay is null
     * @throws IndexOutOfBoundsException if either position is invalid
     * @deprecated Use better named {@link #overlay(String, String, int, int)} instead.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String overlayString(String text, String overlay, int start, int end) {
        return new StringBuffer(start + overlay.length() + text.length() - end + 1).append(text.substring(0, start))
            .append(overlay).append(text.substring(end)).toString();
    }

    /**
     * <p>Overlays part of a String with another String.</p>
     * <p/>
     * <p>A <code>null</code> string input returns <code>null</code>.
     * A negative index is treated as zero.
     * An index greater than the string length is treated as the string length.
     * The start index is always the smaller of the two indices.</p>
     * <p/>
     * <pre>
     * StrUtils.overlay(null, *, *, *)            = null
     * StrUtils.overlay("", "abc", 0, 0)          = "abc"
     * StrUtils.overlay("abcdef", null, 2, 4)     = "abef"
     * StrUtils.overlay("abcdef", "", 2, 4)       = "abef"
     * StrUtils.overlay("abcdef", "", 4, 2)       = "abef"
     * StrUtils.overlay("abcdef", "zzzz", 2, 4)   = "abzzzzef"
     * StrUtils.overlay("abcdef", "zzzz", 4, 2)   = "abzzzzef"
     * StrUtils.overlay("abcdef", "zzzz", -1, 4)  = "zzzzef"
     * StrUtils.overlay("abcdef", "zzzz", 2, 8)   = "abzzzz"
     * StrUtils.overlay("abcdef", "zzzz", -2, -3) = "zzzzabcdef"
     * StrUtils.overlay("abcdef", "zzzz", 8, 10)  = "abcdefzzzz"
     * </pre>
     *
     * @param str     the String to do overlaying in, may be null
     * @param overlay the String to overlay, may be null
     * @param start   the position to start overlaying at
     * @param end     the position to stop overlaying before
     * @return overlayed String, <code>null</code> if null String input
     * @since 2.0
     */
    public static String overlay(String str, String overlay, int start, int end) {
        if (str == null) {
            return null;
        }
        if (overlay == null) {
            overlay = EMPTY;
        }
        int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > len) {
            start = len;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        return new StringBuffer(len + start - end + overlay.length() + 1).append(str.substring(0, start))
            .append(overlay).append(str.substring(end)).toString();
    }

    /**
     * <p>Removes <code>separator</code> from the end of
     * <code>str</code> if it's there, otherwise leave it alone.</p>
     * <p/>
     * <p>NOTE: This method changed in version 2.0.
     * It now more closely matches Perl chomp.
     * For the previous behavior, use {@link #substringBeforeLast(String, String)}.
     * This method uses {@link String#endsWith(String)}.</p>
     * <p/>
     * <pre>
     * StrUtils.chomp(null, *)         = null
     * StrUtils.chomp("", *)           = ""
     * StrUtils.chomp("foobar", "bar") = "foo"
     * StrUtils.chomp("foobar", "baz") = "foobar"
     * StrUtils.chomp("foo", "foo")    = ""
     * StrUtils.chomp("foo ", "foo")   = "foo "
     * StrUtils.chomp(" foo", "foo")   = " "
     * StrUtils.chomp("foo", "foooo")  = "foo"
     * StrUtils.chomp("foo", "")       = "foo"
     * StrUtils.chomp("foo", null)     = "foo"
     * </pre>
     *
     * @param str       the String to chomp from, may be null
     * @param separator separator String, may be null
     * @return String without trailing separator, <code>null</code> if null String input
     */
    public static String chomp(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (str.endsWith(separator)) {
            return str.substring(0, str.length() - separator.length());
        }
        return str;
    }

    /**
     * <p>Remove a value if and only if the String ends with that value.</p>
     *
     * @param str the String to chomp from, must not be null
     * @param sep the String to chomp, must not be null
     * @return String without chomped ending
     * @throws NullPointerException if str or sep is <code>null</code>
     * @deprecated Use {@link #chomp(String, String)} instead.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String chompLast(String str, String sep) {
        if (str.length() == 0) {
            return str;
        }
        String sub = str.substring(str.length() - sep.length());
        if (sep.equals(sub)) {
            return str.substring(0, str.length() - sep.length());
        }
        return str;
    }

    /**
     * <p>Remove everything and return the last value of a supplied String, and
     * everything after it from a String.</p>
     *
     * @param str the String to chomp from, must not be null
     * @param sep the String to chomp, must not be null
     * @return String chomped
     * @throws NullPointerException if str or sep is <code>null</code>
     * @deprecated Use {@link #substringAfterLast(String, String)} instead
     * (although this doesn't include the separator)
     * Method will be removed in Commons Lang 3.0.
     */
    public static String getChomp(String str, String sep) {
        int idx = str.lastIndexOf(sep);
        if (idx == str.length() - sep.length()) {
            return sep;
        } else if (idx != -1) {
            return str.substring(idx);
        } else {
            return EMPTY;
        }
    }

    /**
     * <p>Remove the first value of a supplied String, and everything before it
     * from a String.</p>
     *
     * @param str the String to chomp from, must not be null
     * @param sep the String to chomp, must not be null
     * @return String without chomped beginning
     * @throws NullPointerException if str or sep is <code>null</code>
     * @deprecated Use {@link #substringAfter(String, String)} instead.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String prechomp(String str, String sep) {
        int idx = str.indexOf(sep);
        if (idx == -1) {
            return str;
        }
        return str.substring(idx + sep.length());
    }

    /**
     * <p>Remove and return everything before the first value of a
     * supplied String from another String.</p>
     *
     * @param str the String to chomp from, must not be null
     * @param sep the String to chomp, must not be null
     * @return String prechomped
     * @throws NullPointerException if str or sep is <code>null</code>
     * @deprecated Use {@link #substringBefore(String, String)} instead
     * (although this doesn't include the separator).
     * Method will be removed in Commons Lang 3.0.
     */
    public static String getPrechomp(String str, String sep) {
        int idx = str.indexOf(sep);
        if (idx == -1) {
            return EMPTY;
        }
        return str.substring(0, idx + sep.length());
    }

    // Padding
    //-----------------------------------------------------------------------

    /**
     * <p>Repeat a String <code>repeat</code> times to form a
     * new String.</p>
     * <p/>
     * <pre>
     * StrUtils.repeat(null, 2) = null
     * StrUtils.repeat("", 0)   = ""
     * StrUtils.repeat("", 2)   = ""
     * StrUtils.repeat("a", 3)  = "aaa"
     * StrUtils.repeat("ab", 2) = "abab"
     * StrUtils.repeat("a", -2) = ""
     * </pre>
     *
     * @param str    the String to repeat, may be null
     * @param repeat number of times to repeat str, negative treated as zero
     * @return a new String consisting of the original String repeated,
     * <code>null</code> if null String input
     */
    public static String repeat(String str, int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return padding(repeat, str.charAt(0));
        }

        int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                char ch = str.charAt(0);
                char[] output1 = new char[outputLength];
                for (int i = repeat - 1; i >= 0; i--) {
                    output1[i] = ch;
                }
                return new String(output1);
            case 2:
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                StringBuffer buf = new StringBuffer(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }

    /**
     * <p>Returns padding using the specified delimiter repeated
     * to a given length.</p>
     * <p/>
     * <pre>
     * StrUtils.padding(0, 'e')  = ""
     * StrUtils.padding(3, 'e')  = "eee"
     * StrUtils.padding(-2, 'e') = IndexOutOfBoundsException
     * </pre>
     * <p/>
     * <p>Note: this method doesn't not support padding with
     * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
     * as they require a pair of <code>char</code>s to be represented.
     * If you are needing to support full I18N of your applications
     * consider using {@link #repeat(String, int)} instead.
     * </p>
     *
     * @param repeat  number of times to repeat delim
     * @param padChar character to repeat
     * @return String with repeated character
     * @throws IndexOutOfBoundsException if <code>repeat &lt; 0</code>
     * @see #repeat(String, int)
     */
    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf);
    }

    /**
     * <p>Right pad a String with spaces (' ').</p>
     * <p/>
     * <p>The String is padded to the size of <code>size</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.rightPad(null, *)   = null
     * StrUtils.rightPad("", 3)     = "   "
     * StrUtils.rightPad("bat", 3)  = "bat"
     * StrUtils.rightPad("bat", 5)  = "bat  "
     * StrUtils.rightPad("bat", 1)  = "bat"
     * StrUtils.rightPad("bat", -1) = "bat"
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size the size to pad to
     * @return right padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     */
    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    /**
     * <p>Right pad a String with a specified character.</p>
     * <p/>
     * <p>The String is padded to the size of <code>size</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.rightPad(null, *, *)     = null
     * StrUtils.rightPad("", 3, 'z')     = "zzz"
     * StrUtils.rightPad("bat", 3, 'z')  = "bat"
     * StrUtils.rightPad("bat", 5, 'z')  = "batzz"
     * StrUtils.rightPad("bat", 1, 'z')  = "bat"
     * StrUtils.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return right padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(padding(pads, padChar));
    }

    /**
     * <p>Right pad a String with a specified String.</p>
     * <p/>
     * <p>The String is padded to the size of <code>size</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.rightPad(null, *, *)      = null
     * StrUtils.rightPad("", 3, "z")      = "zzz"
     * StrUtils.rightPad("bat", 3, "yz")  = "bat"
     * StrUtils.rightPad("bat", 5, "yz")  = "batyz"
     * StrUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
     * StrUtils.rightPad("bat", 1, "yz")  = "bat"
     * StrUtils.rightPad("bat", -1, "yz") = "bat"
     * StrUtils.rightPad("bat", 5, null)  = "bat  "
     * StrUtils.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     *
     * @param str    the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return right padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     */
    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    /**
     * <p>Left pad a String with spaces (' ').</p>
     * <p/>
     * <p>The String is padded to the size of <code>size<code>.</p>
     * <p/>
     * <pre>
     * StrUtils.leftPad(null, *)   = null
     * StrUtils.leftPad("", 3)     = "   "
     * StrUtils.leftPad("bat", 3)  = "bat"
     * StrUtils.leftPad("bat", 5)  = "  bat"
     * StrUtils.leftPad("bat", 1)  = "bat"
     * StrUtils.leftPad("bat", -1) = "bat"
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size the size to pad to
     * @return left padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     */
    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    /**
     * <p>Left pad a String with a specified character.</p>
     * <p/>
     * <p>Pad to a size of <code>size</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.leftPad(null, *, *)     = null
     * StrUtils.leftPad("", 3, 'z')     = "zzz"
     * StrUtils.leftPad("bat", 3, 'z')  = "bat"
     * StrUtils.leftPad("bat", 5, 'z')  = "zzbat"
     * StrUtils.leftPad("bat", 1, 'z')  = "bat"
     * StrUtils.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return left padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return padding(pads, padChar).concat(str);
    }

    /**
     * <p>Left pad a String with a specified String.</p>
     * <p/>
     * <p>Pad to a size of <code>size</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.leftPad(null, *, *)      = null
     * StrUtils.leftPad("", 3, "z")      = "zzz"
     * StrUtils.leftPad("bat", 3, "yz")  = "bat"
     * StrUtils.leftPad("bat", 5, "yz")  = "yzbat"
     * StrUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
     * StrUtils.leftPad("bat", 1, "yz")  = "bat"
     * StrUtils.leftPad("bat", -1, "yz") = "bat"
     * StrUtils.leftPad("bat", 5, null)  = "  bat"
     * StrUtils.leftPad("bat", 5, "")    = "  bat"
     * </pre>
     *
     * @param str    the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return left padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     */
    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    /**
     * Gets a String's length or <code>0</code> if the String is <code>null</code>.
     *
     * @param str a String or <code>null</code>
     * @return String length or <code>0</code> if the String is <code>null</code>.
     * @since 2.4
     */
    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    // Centering
    //-----------------------------------------------------------------------

    /**
     * <p>Centers a String in a larger String of size <code>size</code>
     * using the space character (' ').<p>
     * <p/>
     * <p>If the size is less than the String length, the String is returned.
     * A <code>null</code> String returns <code>null</code>.
     * A negative size is treated as zero.</p>
     * <p/>
     * <p>Equivalent to <code>center(str, size, " ")</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.center(null, *)   = null
     * StrUtils.center("", 4)     = "    "
     * StrUtils.center("ab", -1)  = "ab"
     * StrUtils.center("ab", 4)   = " ab "
     * StrUtils.center("abcd", 2) = "abcd"
     * StrUtils.center("a", 4)    = " a  "
     * </pre>
     *
     * @param str  the String to center, may be null
     * @param size the int size of new String, negative treated as zero
     * @return centered String, <code>null</code> if null String input
     */
    public static String center(String str, int size) {
        return center(str, size, ' ');
    }

    /**
     * <p>Centers a String in a larger String of size <code>size</code>.
     * Uses a supplied character as the value to pad the String with.</p>
     * <p/>
     * <p>If the size is less than the String length, the String is returned.
     * A <code>null</code> String returns <code>null</code>.
     * A negative size is treated as zero.</p>
     * <p/>
     * <pre>
     * StrUtils.center(null, *, *)     = null
     * StrUtils.center("", 4, ' ')     = "    "
     * StrUtils.center("ab", -1, ' ')  = "ab"
     * StrUtils.center("ab", 4, ' ')   = " ab"
     * StrUtils.center("abcd", 2, ' ') = "abcd"
     * StrUtils.center("a", 4, ' ')    = " a  "
     * StrUtils.center("a", 4, 'y')    = "yayy"
     * </pre>
     *
     * @param str     the String to center, may be null
     * @param size    the int size of new String, negative treated as zero
     * @param padChar the character to pad the new String with
     * @return centered String, <code>null</code> if null String input
     * @since 2.0
     */
    public static String center(String str, int size, char padChar) {
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padChar);
        str = rightPad(str, size, padChar);
        return str;
    }

    /**
     * <p>Centers a String in a larger String of size <code>size</code>.
     * Uses a supplied String as the value to pad the String with.</p>
     * <p/>
     * <p>If the size is less than the String length, the String is returned.
     * A <code>null</code> String returns <code>null</code>.
     * A negative size is treated as zero.</p>
     * <p/>
     * <pre>
     * StrUtils.center(null, *, *)     = null
     * StrUtils.center("", 4, " ")     = "    "
     * StrUtils.center("ab", -1, " ")  = "ab"
     * StrUtils.center("ab", 4, " ")   = " ab"
     * StrUtils.center("abcd", 2, " ") = "abcd"
     * StrUtils.center("a", 4, " ")    = " a  "
     * StrUtils.center("a", 4, "yz")   = "yayz"
     * StrUtils.center("abc", 7, null) = "  abc  "
     * StrUtils.center("abc", 7, "")   = "  abc  "
     * </pre>
     *
     * @param str    the String to center, may be null
     * @param size   the int size of new String, negative treated as zero
     * @param padStr the String to pad the new String with, must not be null or empty
     * @return centered String, <code>null</code> if null String input
     * @throws IllegalArgumentException if padStr is <code>null</code> or empty
     */
    public static String center(String str, int size, String padStr) {
        if (str == null || size <= 0) {
            return str;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padStr);
        str = rightPad(str, size, padStr);
        return str;
    }

    // Case conversion
    //-----------------------------------------------------------------------

    /**
     * <p>Converts a String to upper case as per {@link String#toUpperCase()}.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.upperCase(null)  = null
     * StrUtils.upperCase("")    = ""
     * StrUtils.upperCase("aBc") = "ABC"
     * </pre>
     *
     * @param str the String to upper case, may be null
     * @return the upper cased String, <code>null</code> if null String input
     */
    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    /**
     * <p>Converts a String to lower case as per {@link String#toLowerCase()}.</p>
     * <p/>
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.lowerCase(null)  = null
     * StrUtils.lowerCase("")    = ""
     * StrUtils.lowerCase("aBc") = "abc"
     * </pre>
     *
     * @param str the String to lower case, may be null
     * @return the lower cased String, <code>null</code> if null String input
     */
    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    /**
     * <p>Capitalizes a String changing the first letter to title case as
     * per {@link Character#toTitleCase(char)}. No other letters are changed.</p>
     * <p/>
     * A <code>null</code> input String returns <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.capitalize(null)  = null
     * StrUtils.capitalize("")    = ""
     * StrUtils.capitalize("cat") = "Cat"
     * StrUtils.capitalize("cAt") = "CAt"
     * </pre>
     *
     * @param str the String to capitalize, may be null
     * @return the capitalized String, <code>null</code> if null String input
     * @see #uncapitalize(String)
     * @since 2.0
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
            .toString();
    }

    /**
     * <p>Capitalizes a String changing the first letter to title case as
     * per {@link Character#toTitleCase(char)}. No other letters are changed.</p>
     *
     * @param str the String to capitalize, may be null
     * @return the capitalized String, <code>null</code> if null String input
     * @deprecated Use the standardly named {@link #capitalize(String)}.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String capitalise(String str) {
        return capitalize(str);
    }

    /**
     * <p>Uncapitalizes a String changing the first letter to title case as
     * per {@link Character#toLowerCase(char)}. No other letters are changed.</p>
     * <p/>
     * <pre>
     * StrUtils.uncapitalize(null)  = null
     * StrUtils.uncapitalize("")    = ""
     * StrUtils.uncapitalize("Cat") = "cat"
     * StrUtils.uncapitalize("CAT") = "cAT"
     * </pre>
     *
     * @param str the String to uncapitalize, may be null
     * @return the uncapitalized String, <code>null</code> if null String input
     * @see #capitalize(String)
     * @since 2.0
     */
    public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
            .toString();
    }

    /**
     * <p>Uncapitalizes a String changing the first letter to title case as
     * per {@link Character#toLowerCase(char)}. No other letters are changed.</p>
     *
     * @param str the String to uncapitalize, may be null
     * @return the uncapitalized String, <code>null</code> if null String input
     * @deprecated Use the standardly named {@link #uncapitalize(String)}.
     * Method will be removed in Commons Lang 3.0.
     */
    public static String uncapitalise(String str) {
        return uncapitalize(str);
    }

    /**
     * <p>Swaps the case of a String changing upper and title case to
     * lower case, and lower case to upper case.</p>
     * <p/>
     * <ul>
     * <li>Upper case character converts to Lower case</li>
     * <li>Title case character converts to Lower case</li>
     * <li>Lower case character converts to Upper case</li>
     * </ul>
     * <p/>
     * A <code>null</code> input String returns <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.swapCase(null)                 = null
     * StrUtils.swapCase("")                   = ""
     * StrUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
     * </pre>
     * <p/>
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer performs a word based algorithm.
     * If you only use ASCII, you will notice no change.
     * That functionality is available in WordUtils.</p>
     *
     * @param str the String to swap case, may be null
     * @return the changed String, <code>null</code> if null String input
     */
    public static String swapCase(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(strLen);

        char ch = 0;
        for (int i = 0; i < strLen; i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                ch = Character.toUpperCase(ch);
            }
            buffer.append(ch);
        }
        return buffer.toString();
    }

    // Count matches
    //-----------------------------------------------------------------------

    /**
     * <p>Counts how many times the substring appears in the larger String.</p>
     * <p/>
     * <p>A <code>null</code> or empty ("") String input returns <code>0</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.countMatches(null, *)       = 0
     * StrUtils.countMatches("", *)         = 0
     * StrUtils.countMatches("abba", null)  = 0
     * StrUtils.countMatches("abba", "")    = 0
     * StrUtils.countMatches("abba", "a")   = 2
     * StrUtils.countMatches("abba", "ab")  = 1
     * StrUtils.countMatches("abba", "xxx") = 0
     * </pre>
     *
     * @param str the String to check, may be null
     * @param sub the substring to count, may be null
     * @return the number of occurrences, 0 if either String is <code>null</code>
     */
    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    // Character Tests
    //-----------------------------------------------------------------------

    /**
     * <p>Checks if the String contains only unicode letters.</p>
     * <p/>
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.isAlpha(null)   = false
     * StrUtils.isAlpha("")     = true
     * StrUtils.isAlpha("  ")   = false
     * StrUtils.isAlpha("abc")  = true
     * StrUtils.isAlpha("ab2c") = false
     * StrUtils.isAlpha("ab-c") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains letters, and is non-null
     */
    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLetter(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters and
     * space (' ').</p>
     * <p/>
     * <p><code>null</code> will return <code>false</code>
     * An empty String ("") will return <code>true</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.isAlphaSpace(null)   = false
     * StrUtils.isAlphaSpace("")     = true
     * StrUtils.isAlphaSpace("  ")   = true
     * StrUtils.isAlphaSpace("abc")  = true
     * StrUtils.isAlphaSpace("ab c") = true
     * StrUtils.isAlphaSpace("ab2c") = false
     * StrUtils.isAlphaSpace("ab-c") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains letters and space,
     * and is non-null
     */
    public static boolean isAlphaSpace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isLetter(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters or digits.</p>
     * <p/>
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.isAlphanumeric(null)   = false
     * StrUtils.isAlphanumeric("")     = true
     * StrUtils.isAlphanumeric("  ")   = false
     * StrUtils.isAlphanumeric("abc")  = true
     * StrUtils.isAlphanumeric("ab c") = false
     * StrUtils.isAlphanumeric("ab2c") = true
     * StrUtils.isAlphanumeric("ab-c") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains letters or digits,
     * and is non-null
     */
    public static boolean isAlphanumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLetterOrDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters, digits
     * or space (<code>' '</code>).</p>
     * <p/>
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.isAlphanumeric(null)   = false
     * StrUtils.isAlphanumeric("")     = true
     * StrUtils.isAlphanumeric("  ")   = true
     * StrUtils.isAlphanumeric("abc")  = true
     * StrUtils.isAlphanumeric("ab c") = true
     * StrUtils.isAlphanumeric("ab2c") = true
     * StrUtils.isAlphanumeric("ab-c") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains letters, digits or space,
     * and is non-null
     */
    public static boolean isAlphanumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isLetterOrDigit(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode digits.
     * A decimal point is not a unicode digit and returns false.</p>
     * <p/>
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.isNumeric(null)   = false
     * StrUtils.isNumeric("")     = true
     * StrUtils.isNumeric("  ")   = false
     * StrUtils.isNumeric("123")  = true
     * StrUtils.isNumeric("12 3") = false
     * StrUtils.isNumeric("ab2c") = false
     * StrUtils.isNumeric("12-3") = false
     * StrUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode digits or space
     * (<code>' '</code>).
     * A decimal point is not a unicode digit and returns false.</p>
     * <p/>
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.isNumeric(null)   = false
     * StrUtils.isNumeric("")     = true
     * StrUtils.isNumeric("  ")   = true
     * StrUtils.isNumeric("123")  = true
     * StrUtils.isNumeric("12 3") = true
     * StrUtils.isNumeric("ab2c") = false
     * StrUtils.isNumeric("12-3") = false
     * StrUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains digits or space,
     * and is non-null
     */
    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isDigit(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only whitespace.</p>
     * <p/>
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.isWhitespace(null)   = false
     * StrUtils.isWhitespace("")     = true
     * StrUtils.isWhitespace("  ")   = true
     * StrUtils.isWhitespace("abc")  = false
     * StrUtils.isWhitespace("ab2c") = false
     * StrUtils.isWhitespace("ab-c") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains whitespace, and is non-null
     * @since 2.0
     */
    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    // Defaults
    //-----------------------------------------------------------------------

    /**
     * <p>Returns either the passed in String,
     * or if the String is <code>null</code>, an empty String ("").</p>
     * <p/>
     * <pre>
     * StrUtils.defaultString(null)  = ""
     * StrUtils.defaultString("")    = ""
     * StrUtils.defaultString("bat") = "bat"
     * </pre>
     *
     * @param str the String to check, may be null
     * @return the passed in String, or the empty String if it
     * was <code>null</code>
     * @see String#valueOf(Object)
     */
    public static String defaultString(String str) {
        return str == null ? EMPTY : str;
    }

    /**
     * <p>Returns either the passed in String, or if the String is
     * <code>null</code>, the value of <code>defaultStr</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.defaultString(null, "NULL")  = "NULL"
     * StrUtils.defaultString("", "NULL")    = ""
     * StrUtils.defaultString("bat", "NULL") = "bat"
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param defaultStr the default String to return
     *                   if the input is <code>null</code>, may be null
     * @return the passed in String, or the default if it was <code>null</code>
     * @see String#valueOf(Object)
     */
    public static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    /**
     * <p>Returns either the passed in String, or if the String is
     * empty or <code>null</code>, the value of <code>defaultStr</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.defaultIfEmpty(null, "NULL")  = "NULL"
     * StrUtils.defaultIfEmpty("", "NULL")    = "NULL"
     * StrUtils.defaultIfEmpty("bat", "NULL") = "bat"
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param defaultStr the default String to return
     *                   if the input is empty ("") or <code>null</code>, may be null
     * @return the passed in String, or the default
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        return StrUtils.isEmpty(str) ? defaultStr : str;
    }

    // Reversing
    //-----------------------------------------------------------------------

    /**
     * <p>Reverses a String as per {@link StringBuffer#reverse()}.</p>
     * <p/>
     * <p>A <code>null</code> String returns <code>null</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.reverse(null)  = null
     * StrUtils.reverse("")    = ""
     * StrUtils.reverse("bat") = "tab"
     * </pre>
     *
     * @param str the String to reverse, may be null
     * @return the reversed String, <code>null</code> if null String input
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuffer(str).reverse().toString();
    }

    // Abbreviating
    //-----------------------------------------------------------------------

    /**
     * <p>Abbreviates a String using ellipses. This will turn
     * "Now is the time for all good men" into "Now is the time for..."</p>
     * <p/>
     * <p>Specifically:
     * <ul>
     * <li>If <code>str</code> is less than <code>maxWidth</code> characters
     * long, return it.</li>
     * <li>Else abbreviate it to <code>(substring(str, 0, max-3) + "...")</code>.</li>
     * <li>If <code>maxWidth</code> is less than <code>4</code>, throw an
     * <code>IllegalArgumentException</code>.</li>
     * <li>In no case will it return a String of length greater than
     * <code>maxWidth</code>.</li>
     * </ul>
     * </p>
     * <p/>
     * <pre>
     * StrUtils.abbreviate(null, *)      = null
     * StrUtils.abbreviate("", 4)        = ""
     * StrUtils.abbreviate("abcdefg", 6) = "abc..."
     * StrUtils.abbreviate("abcdefg", 7) = "abcdefg"
     * StrUtils.abbreviate("abcdefg", 8) = "abcdefg"
     * StrUtils.abbreviate("abcdefg", 4) = "a..."
     * StrUtils.abbreviate("abcdefg", 3) = IllegalArgumentException
     * </pre>
     *
     * @param str      the String to check, may be null
     * @param maxWidth maximum length of result String, must be at least 4
     * @return abbreviated String, <code>null</code> if null String input
     * @throws IllegalArgumentException if the width is too small
     * @since 2.0
     */
    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }

    /**
     * <p>Abbreviates a String using ellipses. This will turn
     * "Now is the time for all good men" into "...is the time for..."</p>
     * <p/>
     * <p>Works like <code>abbreviate(String, int)</code>, but allows you to specify
     * a "left edge" offset.  Note that this left edge is not necessarily going to
     * be the leftmost character in the result, or the first character following the
     * ellipses, but it will appear somewhere in the result.
     * <p/>
     * <p>In no case will it return a String of length greater than
     * <code>maxWidth</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.abbreviate(null, *, *)                = null
     * StrUtils.abbreviate("", 0, 4)                  = ""
     * StrUtils.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
     * StrUtils.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
     * StrUtils.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
     * StrUtils.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
     * StrUtils.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
     * StrUtils.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
     * StrUtils.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
     * StrUtils.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
     * StrUtils.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
     * StrUtils.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
     * StrUtils.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
     * </pre>
     *
     * @param str      the String to check, may be null
     * @param offset   left edge of source String
     * @param maxWidth maximum length of result String, must be at least 4
     * @return abbreviated String, <code>null</code> if null String input
     * @throws IllegalArgumentException if the width is too small
     * @since 2.0
     */
    public static String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if ((str.length() - offset) < (maxWidth - 3)) {
            offset = str.length() - (maxWidth - 3);
        }
        if (offset <= 4) {
            return str.substring(0, maxWidth - 3) + "...";
        }
        if (maxWidth < 7) {
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        }
        if ((offset + (maxWidth - 3)) < str.length()) {
            return "..." + abbreviate(str.substring(offset), maxWidth - 3);
        }
        return "..." + str.substring(str.length() - (maxWidth - 3));
    }

    // Difference
    //-----------------------------------------------------------------------

    /**
     * <p>Compares two Strings, and returns the portion where they differ.
     * (More precisely, return the remainder of the second String,
     * starting from where it's different from the first.)</p>
     * <p/>
     * <p>For example,
     * <code>difference("i am a machine", "i am a robot") -> "robot"</code>.</p>
     * <p/>
     * <pre>
     * StrUtils.difference(null, null) = null
     * StrUtils.difference("", "") = ""
     * StrUtils.difference("", "abc") = "abc"
     * StrUtils.difference("abc", "") = ""
     * StrUtils.difference("abc", "abc") = ""
     * StrUtils.difference("ab", "abxyz") = "xyz"
     * StrUtils.difference("abcde", "abxyz") = "xyz"
     * StrUtils.difference("abcde", "xyz") = "xyz"
     * </pre>
     *
     * @param str1 the first String, may be null
     * @param str2 the second String, may be null
     * @return the portion of str2 where it differs from str1; returns the
     * empty String if they are equal
     * @since 2.0
     */
    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == -1) {
            return EMPTY;
        }
        return str2.substring(at);
    }

    /**
     * <p>Compares two Strings, and returns the index at which the
     * Strings begin to differ.</p>
     * <p/>
     * <p>For example,
     * <code>indexOfDifference("i am a machine", "i am a robot") -> 7</code></p>
     * <p/>
     * <pre>
     * StrUtils.indexOfDifference(null, null) = -1
     * StrUtils.indexOfDifference("", "") = -1
     * StrUtils.indexOfDifference("", "abc") = 0
     * StrUtils.indexOfDifference("abc", "") = 0
     * StrUtils.indexOfDifference("abc", "abc") = -1
     * StrUtils.indexOfDifference("ab", "abxyz") = 2
     * StrUtils.indexOfDifference("abcde", "abxyz") = 2
     * StrUtils.indexOfDifference("abcde", "xyz") = 0
     * </pre>
     *
     * @param str1 the first String, may be null
     * @param str2 the second String, may be null
     * @return the index where str2 and str1 begin to differ; -1 if they are equal
     * @since 2.0
     */
    public static int indexOfDifference(String str1, String str2) {
        if (str1 == str2) {
            return -1;
        }
        if (str1 == null || str2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < str1.length() && i < str2.length(); ++i) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
        }
        if (i < str2.length() || i < str1.length()) {
            return i;
        }
        return -1;
    }

    /**
     * <p>Compares all Strings in an array and returns the index at which the
     * Strings begin to differ.</p>
     * <p/>
     * <p>For example,
     * <code>indexOfDifference(new String[] {"i am a machine", "i am a robot"}) -> 7</code></p>
     * <p/>
     * <pre>
     * StrUtils.indexOfDifference(null) = -1
     * StrUtils.indexOfDifference(new String[] {}) = -1
     * StrUtils.indexOfDifference(new String[] {"abc"}) = -1
     * StrUtils.indexOfDifference(new String[] {null, null}) = -1
     * StrUtils.indexOfDifference(new String[] {"", ""}) = -1
     * StrUtils.indexOfDifference(new String[] {"", null}) = 0
     * StrUtils.indexOfDifference(new String[] {"abc", null, null}) = 0
     * StrUtils.indexOfDifference(new String[] {null, null, "abc"}) = 0
     * StrUtils.indexOfDifference(new String[] {"", "abc"}) = 0
     * StrUtils.indexOfDifference(new String[] {"abc", ""}) = 0
     * StrUtils.indexOfDifference(new String[] {"abc", "abc"}) = -1
     * StrUtils.indexOfDifference(new String[] {"abc", "a"}) = 1
     * StrUtils.indexOfDifference(new String[] {"ab", "abxyz"}) = 2
     * StrUtils.indexOfDifference(new String[] {"abcde", "abxyz"}) = 2
     * StrUtils.indexOfDifference(new String[] {"abcde", "xyz"}) = 0
     * StrUtils.indexOfDifference(new String[] {"xyz", "abcde"}) = 0
     * StrUtils.indexOfDifference(new String[] {"i am a machine", "i am a robot"}) = 7
     * </pre>
     *
     * @param strs array of strings, entries may be null
     * @return the index where the strings begin to differ; -1 if they are all equal
     * @since 2.4
     */
    public static int indexOfDifference(String[] strs) {
        if (strs == null || strs.length <= 1) {
            return -1;
        }
        boolean anyStringNull = false;
        boolean allStringsNull = true;
        int arrayLen = strs.length;
        int shortestStrLen = Integer.MAX_VALUE;
        int longestStrLen = 0;

        // find the min and max string lengths; this avoids checking to make
        // sure we are not exceeding the length of the string each time through
        // the bottom loop.
        for (int i = 0; i < arrayLen; i++) {
            if (strs[i] == null) {
                anyStringNull = true;
                shortestStrLen = 0;
            } else {
                allStringsNull = false;
                shortestStrLen = Math.min(strs[i].length(), shortestStrLen);
                longestStrLen = Math.max(strs[i].length(), longestStrLen);
            }
        }

        // handle lists containing all nulls or all empty strings
        if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
            return -1;
        }

        // handle lists containing some nulls or some empty strings
        if (shortestStrLen == 0) {
            return 0;
        }

        // find the position with the first difference across all strings
        int firstDiff = -1;
        for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
            char comparisonChar = strs[0].charAt(stringPos);
            for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
                if (strs[arrayPos].charAt(stringPos) != comparisonChar) {
                    firstDiff = stringPos;
                    break;
                }
            }
            if (firstDiff != -1) {
                break;
            }
        }

        if (firstDiff == -1 && shortestStrLen != longestStrLen) {
            // we compared all of the characters up to the length of the
            // shortest string and didn't find a match, but the string lengths
            // vary, so return the length of the shortest string.
            return shortestStrLen;
        }
        return firstDiff;
    }

    /**
     * <p>Compares all Strings in an array and returns the initial sequence of
     * characters that is common to all of them.</p>
     * <p/>
     * <p>For example,
     * <code>getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) -> "i am a "</code></p>
     * <p/>
     * <pre>
     * StrUtils.getCommonPrefix(null) = ""
     * StrUtils.getCommonPrefix(new String[] {}) = ""
     * StrUtils.getCommonPrefix(new String[] {"abc"}) = "abc"
     * StrUtils.getCommonPrefix(new String[] {null, null}) = ""
     * StrUtils.getCommonPrefix(new String[] {"", ""}) = ""
     * StrUtils.getCommonPrefix(new String[] {"", null}) = ""
     * StrUtils.getCommonPrefix(new String[] {"abc", null, null}) = ""
     * StrUtils.getCommonPrefix(new String[] {null, null, "abc"}) = ""
     * StrUtils.getCommonPrefix(new String[] {"", "abc"}) = ""
     * StrUtils.getCommonPrefix(new String[] {"abc", ""}) = ""
     * StrUtils.getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
     * StrUtils.getCommonPrefix(new String[] {"abc", "a"}) = "a"
     * StrUtils.getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
     * StrUtils.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
     * StrUtils.getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
     * StrUtils.getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
     * StrUtils.getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
     * </pre>
     *
     * @param strs array of String objects, entries may be null
     * @return the initial sequence of characters that are common to all Strings
     * in the array; empty String if the array is null, the elements are all null
     * or if there is no common prefix.
     * @since 2.4
     */
    public static String getCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return EMPTY;
        }
        int smallestIndexOfDiff = indexOfDifference(strs);
        if (smallestIndexOfDiff == -1) {
            // all strings were identical
            if (strs[0] == null) {
                return EMPTY;
            }
            return strs[0];
        } else if (smallestIndexOfDiff == 0) {
            // there were no common initial characters
            return EMPTY;
        } else {
            // we found a common initial character sequence
            return strs[0].substring(0, smallestIndexOfDiff);
        }
    }

    // Misc
    //-----------------------------------------------------------------------

    /**
     * <p>Find the Levenshtein distance between two Strings.</p>
     * <p/>
     * <p>This is the number of changes needed to change one String into
     * another, where each change is a single character modification (deletion,
     * insertion or substitution).</p>
     * <p/>
     * <p>The previous implementation of the Levenshtein distance algorithm
     * was from <a href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a></p>
     * <p/>
     * <p>Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError
     * which can occur when my Java implementation is used with very large strings.<br>
     * This implementation of the Levenshtein distance algorithm
     * is from <a href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a></p>
     * <p/>
     * <pre>
     * StrUtils.getLevenshteinDistance(null, *)             = IllegalArgumentException
     * StrUtils.getLevenshteinDistance(*, null)             = IllegalArgumentException
     * StrUtils.getLevenshteinDistance("","")               = 0
     * StrUtils.getLevenshteinDistance("","a")              = 1
     * StrUtils.getLevenshteinDistance("aaapppp", "")       = 7
     * StrUtils.getLevenshteinDistance("frog", "fog")       = 1
     * StrUtils.getLevenshteinDistance("fly", "ant")        = 3
     * StrUtils.getLevenshteinDistance("elephant", "hippo") = 7
     * StrUtils.getLevenshteinDistance("hippo", "elephant") = 7
     * StrUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
     * StrUtils.getLevenshteinDistance("hello", "hallo")    = 1
     * </pre>
     *
     * @param s the first String, must not be null
     * @param t the second String, must not be null
     * @return result distance
     * @throws IllegalArgumentException if either String input <code>null</code>
     */
    public static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
           The difference between this impl. and the previous is that, rather
           than creating and retaining a matrix of size s.length()+1 by t.length()+1,
           we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
           is the 'current working' distance array that maintains the newest distance cost
           counts as we iterate through the characters of String s.  Each time we increment
           the index of String t we are comparing, d is copied to p, the second int[].  Doing so
           allows us to retain the previous cost counts as required by the algorithm (taking
           the minimum of the cost count to the left, up one, and diagonally up and to the left
           of the current cost count being calculated).  (Note that the arrays aren't really
           copied anymore, just switched...this is clearly much better than cloning an array
           or doing a System.arraycopy() each time  through the outer loop.)

           Effectively, the difference between the two implementations is this one does not
           cause an out of memory condition when calculating the LD over two very large strings.
         */

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory
            String tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }

        int p[] = new int[n + 1]; //'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }

    /**
     * <p>Gets the minimum of three <code>int</code> values.</p>
     *
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return the smallest of the values
     */
    /*
        private static int min(int a, int b, int c) {
            // Method copied from NumberUtils to avoid dependency on subpackage
            if (b < a) {
                a = b;
            }
            if (c < a) {
                a = c;
            }
            return a;
        }
    */

    // startsWith
    //-----------------------------------------------------------------------

    /**
     * <p>Check if a String starts with a specified prefix.</p>
     * <p/>
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     * <p/>
     * <pre>
     * StrUtils.startsWith(null, null)      = true
     * StrUtils.startsWith(null, "abcdef")  = false
     * StrUtils.startsWith("abc", null)     = false
     * StrUtils.startsWith("abc", "abcdef") = true
     * StrUtils.startsWith("abc", "ABCDEF") = false
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case sensitive, or
     * both <code>null</code>
     * @see String#startsWith(String)
     * @since 2.4
     */
    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    /**
     * <p>Case insensitive check if a String starts with a specified prefix.</p>
     * <p/>
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case insensitive.</p>
     * <p/>
     * <pre>
     * StrUtils.startsWithIgnoreCase(null, null)      = true
     * StrUtils.startsWithIgnoreCase(null, "abcdef")  = false
     * StrUtils.startsWithIgnoreCase("abc", null)     = false
     * StrUtils.startsWithIgnoreCase("abc", "abcdef") = true
     * StrUtils.startsWithIgnoreCase("abc", "ABCDEF") = true
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case insensitive, or
     * both <code>null</code>
     * @see String#startsWith(String)
     * @since 2.4
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    /**
     * <p>Check if a String starts with a specified prefix (optionally case insensitive).</p>
     *
     * @param str        the String to check, may be null
     * @param prefix     the prefix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case
     *                   (case insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or
     * both <code>null</code>
     * @see String#startsWith(String)
     */
    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    // endsWith
    //-----------------------------------------------------------------------

    /**
     * <p>Check if a String ends with a specified suffix.</p>
     * <p/>
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     * <p/>
     * <pre>
     * StrUtils.endsWith(null, null)      = true
     * StrUtils.endsWith(null, "abcdef")  = false
     * StrUtils.endsWith("def", null)     = false
     * StrUtils.endsWith("def", "abcdef") = true
     * StrUtils.endsWith("def", "ABCDEF") = false
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case sensitive, or
     * both <code>null</code>
     * @see String#endsWith(String)
     * @since 2.4
     */
    public static boolean endsWith(String str, String suffix) {
        return endsWith(str, suffix, false);
    }

    /**
     * <p>Case insensitive check if a String ends with a specified suffix.</p>
     * <p/>
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case insensitive.</p>
     * <p/>
     * <pre>
     * StrUtils.endsWithIgnoreCase(null, null)      = true
     * StrUtils.endsWithIgnoreCase(null, "abcdef")  = false
     * StrUtils.endsWithIgnoreCase("def", null)     = false
     * StrUtils.endsWithIgnoreCase("def", "abcdef") = true
     * StrUtils.endsWithIgnoreCase("def", "ABCDEF") = false
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case insensitive, or
     * both <code>null</code>
     * @see String#endsWith(String)
     * @since 2.4
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    /**
     * <p>Check if a String ends with a specified suffix (optionally case insensitive).</p>
     *
     * @param str        the String to check, may be null
     * @param suffix     the suffix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case
     *                   (case insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or
     * both <code>null</code>
     * @see String#endsWith(String)
     */
    private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
    }
}