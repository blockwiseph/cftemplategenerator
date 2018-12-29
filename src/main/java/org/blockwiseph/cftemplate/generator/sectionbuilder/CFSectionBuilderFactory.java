package org.blockwiseph.cftemplate.generator.sectionbuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.blockwiseph.cftemplate.generator.sectionbuilder.specific.OutputCFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.specific.ParameterCFSectionBuilder;
import com.google.common.collect.ImmutableList;

public class CFSectionBuilderFactory {

    private static final String DEFAULT_SPACE_BETWEEN_SECTIONS = "\n";

    /**
     * Returns a CFSectionBuilder for the description section of the cloud formation template.
     * This is just a key value pair, with "Description" as they key, and the passed in description as the value.
     *
     * @param description the value of the description of the cloud formation template
     * @return a CFSectionBuilder that builds the description section of the cloud formation template.
     * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-description-structure.html
     */
    public static CFSectionBuilder description(final String description) {
        return singleLine("Description", description);
    }

    /**
     * Returns a CFSectionBuilder for the parameters section of the cloud formation template.
     *
     * @param parameterCFSectionBuilders varargs/array of the parameters to include in the cloud formation template.
     * @return a CFSectionBuilder that builds the parameters section of the cloud formation template with the given outputs.
     * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/parameters-section-structure.html
     */
    public static CFSectionBuilder parameters(final ParameterCFSectionBuilder... parameterCFSectionBuilders) {
        return titleWithAggregateBuilders("Parameters", parameterCFSectionBuilders);
    }

    /**
     * Returns a CFSectionBuilder for the resources section of the cloud formation template.
     *
     * @param resourcesBuilders varargs/array of the resource builders to include in the cloud formation template.
     * @return a CFSectionBuilder that builds the resources section of the cloud formation template with the given resources.
     * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/resources-section-structure.html
     */
    public static CFSectionBuilder resources(final CFSectionBuilder... resourcesBuilders) {
        return titleWithAggregateBuilders("Resources", resourcesBuilders);
    }

    /**
     * Returns a CFSectionBuilder for the outputs section of the cloud formation template.
     *
     * @param outputCFSectionBuilders varargs/array of the outputs to include in the cloud formation template.
     * @return a CFSectionBuilder that builds the parameters section of the cloud formation template with the given parameters.
     * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/outputs-section-structure.html
     */
    public static CFSectionBuilder outputs(final OutputCFSectionBuilder... outputCFSectionBuilders) {
        return titleWithAggregateBuilders("Outputs", outputCFSectionBuilders);
    }

    public static CFSectionBuilder titleWithAggregateBuilders(final String title,
                                                              final CFSectionBuilder... sectionBuilders) {
        return titleWithAggregateBuilders(title, ImmutableList.copyOf(sectionBuilders));
    }

    public static CFSectionBuilder titleWithAggregateBuilders(final String title,
                                                              final List<CFSectionBuilder> sectionBuilders) {
        return new CFSectionBuilderWithTitle(title, aggregating(sectionBuilders));
    }

    public static CFSectionBuilder listItem(final CFSectionBuilder... sectionBuilders) {
        return new ListItemCFSectionBuilder(aggregating(sectionBuilders));
    }

    public static CFSectionBuilder listOf(final CFSectionBuilder... sectionBuilders) {
        return listOf(ImmutableList.copyOf(sectionBuilders));
    }

    public static CFSectionBuilder listOf(final List<? extends CFSectionBuilder> sectionBuilders) {
        return aggregating(
                sectionBuilders.stream()
                        .map(ListItemCFSectionBuilder::new)
                        .collect(Collectors.toList()));
    }

    public static List<CFSectionBuilder> fromPlainStrings(final List<String> strings) {
        return strings.stream()
                .map(CFSectionBuilderFactory::fromPlainString)
                .collect(Collectors.toList());
    }

    public static CFSectionBuilder fromPlainString(final String string) {
        return () -> string;
    }

    public static List<CFSectionBuilder> fromKeyValueMap(final Map<String, String> map) {
        return map.keySet().stream()
                .map(key -> singleLine(key, map.get(key)))
                .collect(Collectors.toList());
    }

    public static CFSectionBuilder singleLine(final String key, final Object value) {
        return () -> (key + ": " + value);
    }

    public static CFSectionBuilder aggregating(final CFSectionBuilder... sectionBuilders) {
        return aggregating(ImmutableList.copyOf(sectionBuilders));
    }

    public static CFSectionBuilder aggregating(final List<? extends CFSectionBuilder> sectionBuilders) {
        return aggregating(sectionBuilders, DEFAULT_SPACE_BETWEEN_SECTIONS);
    }

    public static CFSectionBuilder aggregating(final List<? extends CFSectionBuilder> sectionBuilders,
                                               final String spaceBetweenSections) {
        return new AggregatingCFSectionBuilder(sectionBuilders, spaceBetweenSections);
    }
}
