package org.blockwiseph.cftemplate.generator.util;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.StringUtils.replace;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CFGeneratorUtils {

    private static final int YAML_INDENT_SIZE = 2;

    /**
     * Indents a multi line string by 2 on each line.
     * @param str the string to indent.
     * @return transformation of str where each line in str is indented with 2 spaces before it.
     */
    public static String ymlIndent(final String str) {
        final String indent = repeat(SPACE, YAML_INDENT_SIZE);
        return indent + replace(str, "\n", "\n" + indent);
    }
}
