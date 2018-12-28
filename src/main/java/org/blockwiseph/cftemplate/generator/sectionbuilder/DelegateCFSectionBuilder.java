package org.blockwiseph.cftemplate.generator.sectionbuilder;

public abstract class DelegateCFSectionBuilder implements CFSectionBuilder {

    @Override
    public final String getSectionContents() {
        return delegate().getSectionContents();
    }

    protected abstract CFSectionBuilder delegate();
}
