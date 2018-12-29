package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Base class for building a cloud formation resource in the cloud formation template.
 *
 * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/resources-section-structure.html
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
abstract class ResourceSectionBuilder extends DelegateCFSectionBuilder {

    @Override
    public final CFSectionBuilder delegate() {
        return titleWithAggregateBuilders(getResourceId(),
                singleLine("Type", getAWSResourceType().toString()),
                titleWithAggregateBuilders("Properties", resourceProperties())
        );
    }

    abstract String getResourceId();

    abstract AWSResourceType getAWSResourceType();

    abstract CFSectionBuilder resourceProperties();
}
