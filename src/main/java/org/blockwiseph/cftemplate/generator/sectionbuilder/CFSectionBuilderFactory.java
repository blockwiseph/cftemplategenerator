package org.blockwiseph.cftemplate.generator.sectionbuilder;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.blockwiseph.cftemplate.generator.sectionbuilder.specific.ConditionCFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.specific.OutputCFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.specific.ParameterCFSectionBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contains factory methods to create instances of
 *
 * @see org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CFSectionBuilderFactory {

    private static final String DEFAULT_SPACE_BETWEEN_SECTIONS = "\n";

    /**
     * Returns a CFSectionBuilder for the description section of the cloud formation template.
     * This is just a key value pair, with "Description" as they key, and the passed in description as the value.
     *
     * @param description the value of the description of the cloud formation template
     * @return a CFSectionBuilder that builds the description section of the cloud formation template.
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-description-structure.html">AWS CloudFormation Description Documentation</a>
     */
    public static CFSectionBuilder description(final String description) {
        return singleLine("Description", description);
    }

    /**
     * Returns a CFSectionBuilder for the parameters section of the cloud formation template.
     *
     * @param parameterCFSectionBuilders varargs/array of the parameters to include in the cloud formation template.
     * @return a CFSectionBuilder that builds the parameters section of the cloud formation template with the given outputs.
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/parameters-section-structure.html">AWS CloudFormation Parameters Documentation</a>
     */
    public static CFSectionBuilder parameters(final ParameterCFSectionBuilder... parameterCFSectionBuilders) {
        return titleWithAggregateBuilders("Parameters", parameterCFSectionBuilders);
    }

    /**
     * Returns a CFSectionBuilder for the conditions section of the cloud formation template.
     *
     * @param conditionCFSectionBuilders varargs/array of the conditions to include in the cloud formation template.
     * @return a CFSectionBuilder that builds the conditions section of the cloud formation template with the given outputs.
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/conditions-section-structure.html">AWS CloudFormation Conditions Documentation</a>
     */
    public static CFSectionBuilder conditions(final ConditionCFSectionBuilder... conditionCFSectionBuilders) {
        return titleWithAggregateBuilders("Conditions", conditionCFSectionBuilders);
    }

    /**
     * Returns a CFSectionBuilder for the resources section of the cloud formation template.
     *
     * @param resourcesBuilders varargs/array of the resource builders to include in the cloud formation template.
     * @return a CFSectionBuilder that builds the resources section of the cloud formation template with the given resources.
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/resources-section-structure.html">AWS CloudFormation Resources Documentation</a>
     */
    public static CFSectionBuilder resources(final CFSectionBuilder... resourcesBuilders) {
        return titleWithAggregateBuilders("Resources", resourcesBuilders);
    }

    /**
     * Returns a CFSectionBuilder for the outputs section of the cloud formation template.
     *
     * @param outputCFSectionBuilders varargs/array of the outputs to include in the cloud formation template.
     * @return a CFSectionBuilder that builds the parameters section of the cloud formation template with the given parameters.
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/outputs-section-structure.html">AWS CloudFormation Outputs Documentation</a>
     */
    public static CFSectionBuilder outputs(final OutputCFSectionBuilder... outputCFSectionBuilders) {
        return titleWithAggregateBuilders("Outputs", outputCFSectionBuilders);
    }

    /**
     * Returns a CFSectionBuilder that builds a section with a title and then the sub sections indented under that title.
     *
     * @param title           the title of the section
     * @param sectionBuilders varargs / array of subsections to indent under that title
     * @return a CFSectionBuilder that builds a section with a title and then the sub sections indented under that title
     */
    public static CFSectionBuilder titleWithAggregateBuilders(final String title,
                                                              final CFSectionBuilder... sectionBuilders) {
        return titleWithAggregateBuilders(title, ImmutableList.copyOf(sectionBuilders));
    }

    /**
     * Returns a CFSectionBuilder that builds a section with a title and then the sub sections indented under that title.
     *
     * @param title           the title of the section
     * @param sectionBuilders list of subsections to indent under that title
     * @return a CFSectionBuilder that builds a section with a title and then the sub sections indented under that title
     */
    public static CFSectionBuilder titleWithAggregateBuilders(final String title,
                                                              final List<CFSectionBuilder> sectionBuilders) {
        return new CFSectionBuilderWithTitle(title, aggregating(sectionBuilders));
    }

    /**
     * Returns a CFSectionBuilder that builds a cloud formation (YML) object, which is a single item in the list.
     * The object's contents is the aggregation of all the section builders passed in.
     *
     * @param sectionBuilders varargs/array of section builders representing the properties of the object
     * @return a CFSectionBuilder that contains a list item, with the passed in section builders aggregated.
     */
    public static CFSectionBuilder listItem(final CFSectionBuilder... sectionBuilders) {
        return new ListItemCFSectionBuilder(aggregating(sectionBuilders));
    }

    /**
     * Builds a CFSectionBuilder that returns aggregates the passed in section builders a list (YML list) in the
     * cloud formation template. Each section builder is aggregated as an individual list item.
     *
     * @param sectionBuilders varargs/array of the section builders to aggregate
     * @return a CFSectionBuilder that aggregates the passed in sectionBuilders in a cloud formation list.
     */
    public static CFSectionBuilder listOf(final CFSectionBuilder... sectionBuilders) {
        return listOf(ImmutableList.copyOf(sectionBuilders));
    }

    /**
     * Builds a CFSectionBuilder that returns aggregates the passed in section builders a list (YML list) in the
     * cloud formation template. Each section builder is aggregated as an individual list item.
     *
     * @param sectionBuilders the section builders to aggregate
     * @return a CFSectionBuilder that aggregates the passed in sectionBuilders in a cloud formation list.
     */
    public static CFSectionBuilder listOf(final List<? extends CFSectionBuilder> sectionBuilders) {
        return aggregating(
                sectionBuilders.stream()
                        .map(ListItemCFSectionBuilder::new)
                        .collect(Collectors.toList()));
    }

    /**
     * Maps a list of plain strings to a list of CFSectionBuilder.
     * Each CFSectionBuilder in the returned list simply returns the corresponding string of the input list,
     * using fromPlainString method.
     *
     * @param strings the list of strings to map to list of CFSectionBuilder
     * @return a list of CFSectionBuilder where each element corresponds to the element in the input string list
     */
    public static List<CFSectionBuilder> fromPlainStrings(final List<String> strings) {
        return strings.stream()
                .map(CFSectionBuilderFactory::fromPlainString)
                .collect(Collectors.toList());
    }

    /**
     * Returns a CFSectionBuilder that simply puts the passed string into a cloud formation template
     *
     * @param string the string to put in the section of the cloud formation template.
     * @return a CFSectionBuilder that puts the passed string into a cloud formation template
     */
    public static CFSectionBuilder fromPlainString(final String string) {
        return () -> string;
    }

    /**
     * Returns a CFSectionBuilder that creates a section with multiple key value pairs based on a map.
     *
     * @param map the map to use to create key value pairs in the cloud formation template
     * @return a CFSectionBuilder that creates a section with multiple key value pairs
     */
    public static List<CFSectionBuilder> fromKeyValueMap(final Map<String, String> map) {
        return map.keySet().stream()
                .map(key -> singleLine(key, map.get(key)))
                .collect(Collectors.toList());
    }

    /**
     * Returns a CFSectionBuilder that creates a single line with a key value pair.
     *
     * @param key   the key of the cloud formation Attribute
     * @param value the value of the cloud formation attribute
     * @return a CFSectionBuilder that creates a single line with a key value pair
     */
    public static CFSectionBuilder singleLine(final String key, final Object value) {
        return () -> (key + ": " + value);
    }

    /**
     * Returns a CFSectionBuilder that aggregates the passed in section builders, separated by a new line.
     *
     * @param sectionBuilders varargs/array of the section builders to aggregate
     * @return a CFSectionBuilder that aggregates the passed in section builders, separated by a new line
     */
    public static CFSectionBuilder aggregating(final CFSectionBuilder... sectionBuilders) {
        return aggregating(ImmutableList.copyOf(sectionBuilders));
    }

    /**
     * Returns a CFSectionBuilder that aggregates the passed in section builders, separated by a new line.
     *
     * @param sectionBuilders list of the section builders to aggregate
     * @return a CFSectionBuilder that aggregates the passed in section builders, separated by a new line
     */
    public static CFSectionBuilder aggregating(final List<? extends CFSectionBuilder> sectionBuilders) {
        return aggregating(sectionBuilders, DEFAULT_SPACE_BETWEEN_SECTIONS);
    }

    /**
     * Returns a CFSectionBuilder that aggregates the passed in section builders, separated by the passed in separator.
     *
     * @param sectionBuilders      list of the section builders to aggregate
     * @param spaceBetweenSections separator between the sections, when aggregating
     * @return a CFSectionBuilder that aggregates the passed in section builders, separated by the passed in space separator
     */
    public static CFSectionBuilder aggregating(final List<? extends CFSectionBuilder> sectionBuilders,
                                               final String spaceBetweenSections) {
        return new AggregatingCFSectionBuilder(sectionBuilders, spaceBetweenSections);
    }
}
