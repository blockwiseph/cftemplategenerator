package org.blockwiseph.cftemplate.generator.sectionbuilder.specific;

import lombok.Builder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainString;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

/**
 * Value class for a single condition of the Conditions Section of Cloud formation template.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/conditions-section-structure.html">AWS CloudFormation Conditions Documentation</a>
 */
@Builder
public class ConditionCFSectionBuilder extends DelegateCFSectionBuilder {

    private final String conditionId;
    private final String conditionFunction;

    @Override
    public CFSectionBuilder delegate() {
        return titleWithAggregateBuilders(conditionId,
                fromPlainString(conditionFunction)
        );
    }
}
