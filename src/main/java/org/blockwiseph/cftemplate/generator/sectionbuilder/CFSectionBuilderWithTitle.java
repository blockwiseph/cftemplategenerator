package org.blockwiseph.cftemplate.generator.sectionbuilder;

import static org.blockwiseph.cftemplate.generator.util.CFGeneratorUtils.ymlIndent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class CFSectionBuilderWithTitle implements CFSectionBuilder {

    private final String title;
    private final CFSectionBuilder cfSectionBuilder;

    @Override
    public final String getSectionContents() {
        return title + ":\n" +
                ymlIndent(cfSectionBuilder.getSectionContents());
    }
}