package org.blockwiseph.cftemplate.generator.sectionbuilder;

import static org.blockwiseph.cftemplate.generator.util.CFGeneratorUtils.ymlIndent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ListItemCFSectionBuilder implements CFSectionBuilder {

    private final CFSectionBuilder cfSectionBuilder;

    @Override
    public final String getSectionContents() {
        return ymlIndent(cfSectionBuilder.getSectionContents())
                .replaceFirst(" ", "-");
    }
}