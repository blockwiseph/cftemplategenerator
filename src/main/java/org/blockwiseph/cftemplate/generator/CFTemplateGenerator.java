package org.blockwiseph.cftemplate.generator;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * The main Generator class. It takes in a template supplier and template saver.
 * It calls the supplier to get the template, and then invokes the saved with the returned template.
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
