package ch.interlis.ili2c.parser;

public final class InterlisString {
    private InterlisString() {
    }

    public static String parseEscapeSequences(String input) {
        StringBuilder result = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\\') {
                if (i + 1 >= input.length()) {
                    throw new IllegalArgumentException("Incomplete escape sequence: '\\' at end of string");
                }
                char next = input.charAt(i + 1);
                switch (next) {
                    case '\"':
                        result.append('\"');
                        break;
                    case '\\':
                        result.append('\\');
                        break;
                    case 'u':
                        int numberStart = i + 2;
                        int numberEnd = numberStart + 4;
                        if (numberEnd > input.length()) {
                            throw new IllegalArgumentException("Incomplete unicode escape sequence at end of string");
                        }
                        String hexNumber = input.substring(numberStart, numberEnd);
                        try {
                            int unicodeValue = Integer.parseInt(hexNumber, 16);
                            result.append((char) unicodeValue);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid unicode escape sequence: \\u" + hexNumber);
                        }
                        i += 4;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown escape sequence: \\" + next);
                }
                i++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String escapeSpecialChars(String input) {
        StringBuilder result = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (requiresUnicodeEscape(c)) {
                result.append("\\u");
                result.append(String.format("%04x", (int) c));
            } else {
                switch (c) {
                    case '\"':
                        result.append("\\\"");
                        break;
                    case '\\':
                        result.append("\\\\");
                        break;
                    default:
                        result.append(c);
                        break;
                }
            }
        }
        return result.toString();
    }

    private static boolean requiresUnicodeEscape(char c) {
        return c < 0x20 || c > 0x7E;
    }
}
