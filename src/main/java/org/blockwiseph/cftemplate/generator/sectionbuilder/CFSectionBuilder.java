package org.blockwiseph.cftemplate.generator.sectionbuilder;

/**
 * Functional interface representing a builder that builds a certain portion of a cloud formation template.
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
