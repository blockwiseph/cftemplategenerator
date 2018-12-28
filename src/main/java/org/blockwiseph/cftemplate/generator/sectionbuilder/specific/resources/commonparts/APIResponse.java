package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources.commonparts;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromKeyValueMap;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import lombok.Builder;

@Builder
public class APIResponse extends DelegateCFSectionBuilder {

    private final int statusCode;
    private final String selectionPattern;
    private final APIResponseParameters responseParameters;
    private final Map<String, String> responseModels;
    private final Map<String, String> responseTemplates;

    @Override
    protected CFSectionBuilder delegate() {
        return aggregating(new ArrayList<CFSectionBuilder>() {{
            add(singleLine("StatusCode", statusCode));

            Optional.ofNullable(selectionPattern).ifPresent(s ->
                    add(singleLine("SelectionPattern", selectionPattern)));

            Optional.ofNullable(responseParameters).ifPresent(
                    this::add);

            Optional.ofNullable(responseModels).ifPresent(m ->
                    add(titleWithAggregateBuilders("ResponseModels", fromKeyValueMap(responseModels))));

            Optional.ofNullable(responseTemplates).ifPresent(m ->
                    add(titleWithAggregateBuilders("ResponseTemplates", fromKeyValueMap(responseTemplates))));
        }});
    }

    @Builder
    public static class APIResponseParameters extends DelegateCFSectionBuilder {

        private static final String ACCESS_CONTROL_HEADER_PREFIX = "method.response.header.Access-Control-Allow-";

        private final String allowHeaders;
        private final String allowMethods;
        private final String allowOrigins;

        @Override
        protected CFSectionBuilder delegate() {
            return titleWithAggregateBuilders("ResponseParameters",
                    singleLine(ACCESS_CONTROL_HEADER_PREFIX + "Headers", allowHeaders),
                    singleLine(ACCESS_CONTROL_HEADER_PREFIX + "Methods", allowMethods),
                    singleLine(ACCESS_CONTROL_HEADER_PREFIX + "Origin", allowOrigins)
            );
        }
    }
}
