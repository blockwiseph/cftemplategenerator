package org.blockwiseph.cftemplate.generator;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CFTemplateGeneratorModule extends AbstractModule {

    private final String cfTemplateFilePath;
    private final List<CFSectionBuilder> topLevelSectionBuilders;

    @Provides
    Consumer<String> getCFTemplateSaver() {
        return new CFTemplateFileSaver(cfTemplateFilePath);
    }

    @Provides
    Supplier<String> getCFTemplateSupplier() {
        return () -> aggregating(topLevelSectionBuilders, "\n\n").getSectionContents();
    }
}
