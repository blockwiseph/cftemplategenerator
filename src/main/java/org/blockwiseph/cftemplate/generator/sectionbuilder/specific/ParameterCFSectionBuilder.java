package org.blockwiseph.cftemplate.generator.sectionbuilder.specific;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import lombok.Builder;

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