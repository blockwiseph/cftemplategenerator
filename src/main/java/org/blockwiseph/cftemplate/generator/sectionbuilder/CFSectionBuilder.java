package org.blockwiseph.cftemplate.generator.sectionbuilder;

/**
 * Functional interface representing a builder that builds a certain portion of a cloud formation template.
 *
 * You can create custom instances of section builders, or you can use the
 * @see org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory to create instances.
 *
 * To create instances for each resource, you can use the resources section builders found in the
 * @see org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources
 *
 * You can also extend
 * @see org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder
 * to create an instance that delegates the building to another CFSectionBuilder that may potentially be
 * one created by using the CFSectionBuilderFactory.
 */
@FunctionalInterface
public interface CFSectionBuilder {

    /**
     * Returns the contents of the built section as string.
     *
     * @return the contents of the section to write to the cloud formation template.
     */
    String getSectionContents();
}
