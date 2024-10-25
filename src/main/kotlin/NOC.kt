package net.riedel

import java.nio.file.Path

object NOC {

    val comparator = Comparator<String> { s, t -> compare(s, t) }
    val pathComparator = Comparator<Path> { s, t -> compare(s.toString(), t.toString()) }

    /**
     * @param s first string
     * @param t second string
     * @return zero iff `s` and `t` are equal,
     * a value less than zero iff `s` lexicographically precedes `t`
     * and a value larger than zero iff `s` lexicographically follows `t`
     */
    fun compare(s: String?, t: String?): Int {
        var sIndex = 0
        var tIndex = 0

        if (s == null) {
            return if (t == null) 0 else -1
        }
        if (t == null) {
            return 1
        }

        val sLength = s.length
        val tLength = t.length

        while (true) {
            // both character indices are after a subword (or at zero)

            // Check if one string is at end
            if (sIndex == sLength && tIndex == tLength) {
                return 0
            }
            if (sIndex == sLength) {
                return -1
            }
            if (tIndex == tLength) {
                return 1
            }

            // Compare sub word
            var sChar = s[sIndex]
            var tChar = t[tIndex]

            var sCharIsDigit = Character.isDigit(sChar)
            var tCharIsDigit = Character.isDigit(tChar)

            if (sCharIsDigit && tCharIsDigit) {
                // Compare numbers

                // skip leading 0s
                var sLeadingZeroCount = 0
                while (sChar == '0') {
                    ++sLeadingZeroCount
                    ++sIndex
                    if (sIndex == sLength) {
                        break
                    }
                    sChar = s[sIndex]
                }
                var tLeadingZeroCount = 0
                while (tChar == '0') {
                    ++tLeadingZeroCount
                    ++tIndex
                    if (tIndex == tLength) {
                        break
                    }
                    tChar = t[tIndex]
                }
                val sAllZero = sIndex == sLength || !Character.isDigit(sChar)
                val tAllZero = tIndex == tLength || !Character.isDigit(tChar)
                if (sAllZero) {
                    return if (tAllZero) {
                        continue
                    } else {
                        -1
                    }
                }
                if (tAllZero) {
                    return 1
                }

                var diff = 0
                do {
                    if (diff == 0) {
                        diff = sChar - tChar
                    }
                    ++sIndex
                    ++tIndex
                    if (sIndex == sLength && tIndex == tLength) {
                        return if (diff != 0) diff else sLeadingZeroCount - tLeadingZeroCount
                    }
                    if (sIndex == sLength) {
                        if (diff == 0) {
                            return -1
                        }
                        return if (Character.isDigit(t[tIndex])) -1 else diff
                    }
                    if (tIndex == tLength) {
                        if (diff == 0) {
                            return 1
                        }
                        return if (Character.isDigit(s[sIndex])) 1 else diff
                    }
                    sChar = s[sIndex]
                    tChar = t[tIndex]
                    sCharIsDigit = Character.isDigit(sChar)
                    tCharIsDigit = Character.isDigit(tChar)
                    if (!sCharIsDigit && !tCharIsDigit) {
                        // both number sub words have the same length
                        if (diff != 0) {
                            return diff
                        }
                        break
                    }
                    if (!sCharIsDigit) {
                        return -1
                    }
                    if (!tCharIsDigit) {
                        return 1
                    }
                } while (true)
            } else {
                do {
                    if (sChar != tChar) {
                        val result = Character.toUpperCase(sChar) - Character.toUpperCase(tChar)
                        return if (result != 0) result else sChar - tChar
                    }
                    ++sIndex
                    ++tIndex
                    if (sIndex == sLength && tIndex == tLength) {
                        return 0
                    }
                    if (sIndex == sLength) {
                        return -1
                    }
                    if (tIndex == tLength) {
                        return 1
                    }
                    sChar = s[sIndex]
                    tChar = t[tIndex]
                    sCharIsDigit = Character.isDigit(sChar)
                    tCharIsDigit = Character.isDigit(tChar)
                } while (!sCharIsDigit && !tCharIsDigit)
            }
        }
    }
}
