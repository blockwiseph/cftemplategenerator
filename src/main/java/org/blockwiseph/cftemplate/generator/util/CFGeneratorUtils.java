package org.blockwiseph.cftemplate.generator.util;

import com.amazonaws.http.HttpMethodName;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources.commonparts.APIResponse.APIResponseParameters;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainString;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainStrings;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.listOf;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CFGeneratorUtils {

    public static final String NO_VALUE = "Ref: \"AWS::NoValue\"";

    private static final int YAML_INDENT_SIZE = 2;

    /**
     * Indents a multi line string by 2 on each line.
     *
     * @param str the string to indent.
     * @return transformation of str where each line in str is indented with 2 spaces before it.
     */
    public static String ymlIndent(final String str) {
        final String indent = repeat(SPACE, YAML_INDENT_SIZE);
        return indent + replace(str, "\n", "\n" + indent);
    }

    /**
     * Prepends !Ref to a resource Id. Useful for adding references to other cloud formation resources.
     *
     * @param resourceId the resource Id
     * @return resourceId prepended with !Ref
     */
    public static String referencing(final String resourceId) {
        return "!Ref " + resourceId;
    }

    /**
     * Generates a cloud formation field using the Fn::If function. It uses the conditionId as the condition, and plugs
     * valueIfTrue and valueIfFalse as the true and false values.
     *
     * @param conditionId  the condition Id
     * @param valueIfTrue  the value if the condition is true
     * @param valueIfFalse the value if the condition is false
     * @return a string representing the conditional value using cloud formation Fn::If function
     */
    public static String conditionalValue(final String conditionId, final String valueIfTrue, final String valueIfFalse) {
        return "\n" + ymlIndent(
                titleWithAggregateBuilders("Fn::If", listOf(
                        fromPlainString(conditionId),
                        fromPlainString(valueIfTrue),
                        fromPlainString(valueIfFalse)
                )).getSectionContents()
        );
    }

    /**
     * Generates a cloud formation field using the Fn::Join function. It joins the substrings with the provided delimiter.
     *
     * @param delimiter  the delimiter to use when joining using Fn::Join
     * @param substrings Varargs/array of the substrings to join
     * @return a string representing the join using cloud formation Fn::Join function
     */
    public static String joining(final String delimiter, final String... substrings) {
        return "\n" + ymlIndent(
                titleWithAggregateBuilders("Fn::Join", listOf(
                        fromPlainString("\"" + delimiter + "\""),
                        listOf(fromPlainStrings(ImmutableList.copyOf(substrings)))
                )).getSectionContents()
        );
    }

    /**
     * Generates a !Equals function to compare resource reference to a value
     *
     * @param resourceId the resource id
     * @param value      the value to compare to using !Equals
     * @return !Equals function to compare resource reference to a value
     */
    public static String resourceEquals(final String resourceId, final String value) {
        return String.format("!Equals [ %s, %s ]", referencing(resourceId), value);
    }

    /**
     * Generates a Fn::GetAtt function to get the Arn for the resource id, for use in cloud formation template.
     *
     * @param resourceId the resource id
     * @return Fn::GetAtt function to get the Arn of the resource id
     */
    public static String getAttArn(final String resourceId) {
        return singleLine("Fn::GetAtt", "[" + resourceId + ", Arn]")
                .getSectionContents();
    }

    /**
     * Generates a !GetAtt function to get the attribute for the resource id, for use in cloud formation template.
     *
     * @param resourceId the resource id
     * @param attribute  the attribute to get using !GetAtt
     * @return !GetAtt function to get the attribute of the resource id
     */
    public static String getATTAttribute(final String resourceId, final String attribute) {
        return fromPlainString("!GetAtt " + resourceId + "." + attribute)
                .getSectionContents();
    }

    /**
     * Generates APIResponseParameters for the provided allowedMethods.
     *
     * @param allowedMethods the HttpMethodNames for which to generate the APIResponseParameters
     * @return APIResponseParameters for the provided allowedMethods
     */
    public static APIResponseParameters apiResponseForAllowedMethods(final HttpMethodName... allowedMethods) {
        return APIResponseParameters.builder()
                .allowHeaders("\"'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token'\"")
                .allowMethods("\"'" + Stream.of(allowedMethods).map(Enum::name).collect(Collectors.joining(",")) + "'\"")
                .allowOrigins("\"'*'\"")
                .build();
    }

    /**
     * APIResponseParameters for the which allowedHeaders,Origins,Methods are all true.
     *
     * @return APIResponseParameters for the which allowedHeaders,Origins,Methods are all true.
     */
    public static APIResponseParameters apiResponseParametersAllTrue() {
        return APIResponseParameters.builder()
                .allowHeaders("true")
                .allowMethods("true")
                .allowOrigins("true")
                .build();
    }
}
