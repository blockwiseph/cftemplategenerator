package org.blockwiseph.cftemplate.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class CFTemplateFileSaver implements Consumer<String> {

    private final String cfTemplateFilePath;

    @Override
    public void accept(String s) {
        try {
            Files.write(Paths.get(cfTemplateFilePath), s.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Unable to save yaml file");
        }
    }
}
