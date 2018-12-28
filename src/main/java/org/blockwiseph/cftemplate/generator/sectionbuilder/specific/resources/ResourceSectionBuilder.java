package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
