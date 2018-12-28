package org.blockwiseph.cftemplate.generator;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * This project should be in a separate repository and package altogether.
 * However, for now, we are keeping it here. As we evolve, we can move this
 * to its own repository, and import into this project, or any other project.
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__({@Inject}))
public class CFTemplateGenerator {

    private final Supplier<String> cfTemplateSupplier;
    private final Consumer<String> cfTemplateSaver;

    public void generateCFTemplate() {
        final String cfTemplate = cfTemplateSupplier.get();
        cfTemplateSaver.accept(cfTemplate);
    }
}
