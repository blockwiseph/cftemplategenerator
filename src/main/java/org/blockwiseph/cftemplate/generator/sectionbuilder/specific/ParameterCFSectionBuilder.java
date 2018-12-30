package org.blockwiseph.cftemplate.generator.sectionbuilder.specific;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import lombok.Builder;

/**
 * Value class for a single parameter of the Parameters Section of Cloud formation template.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/parameters-section-structure.html">AWS CloudFormation Parameters Documentation</a>
 */
@Builder
public class ParameterCFSectionBuilder extends DelegateCFSectionBuilder {

    private final String name;
    private final String type;
    private final String description;

    @Override
    public CFSectionBuilder delegate() {
        return titleWithAggregateBuilders(name,
                singleLine("Type", type),
                singleLine("Description", description)
        );
    }
}
