package org.blockwiseph.cftemplate.generator.sectionbuilder.specific;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import lombok.Builder;

/**
 * Value class for one output of the Outputs Section of Cloud formation template.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/outputs-section-structure.html">AWS CloudFormation Outputs Documentation</a>
 */
@Builder
public class OutputCFSectionBuilder extends DelegateCFSectionBuilder {

    private final String name;
    private final String description;
    private final String value;

    @Override
    public CFSectionBuilder delegate() {
        return titleWithAggregateBuilders(name,
                singleLine("Description", description),
                singleLine("Value", value)
        );
    }
}