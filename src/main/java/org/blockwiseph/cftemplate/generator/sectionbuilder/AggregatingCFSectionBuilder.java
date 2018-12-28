package org.blockwiseph.cftemplate.generator.sectionbuilder;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class AggregatingCFSectionBuilder implements CFSectionBuilder {

    private final List<? extends CFSectionBuilder> subSectionBuilders;
    private final String spaceBetweenSections;

    @Override
    public String getSectionContents() {
        return subSectionBuilders.stream()
                .map(CFSectionBuilder::getSectionContents)
                .collect(Collectors.joining(spaceBetweenSections));
    }
}
