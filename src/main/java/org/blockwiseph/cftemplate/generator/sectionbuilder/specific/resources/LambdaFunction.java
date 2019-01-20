package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.AWSResourceType.LAMBDA_FUNCTION;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainString;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Value class for Lambda Function of cloud formation template. Environment variables are optional.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-lambda-function.html">AWS Lambda Documentation</a>
 */
@Builder
public class LambdaFunction extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String s3Bucket;
    private final String s3Key;
    private final String handler;
    private final String runtime;
    private final int memory;
    private final String role;
    private final String description;
    private final int timeout;
    private final Map<String, String> environmentVariables;

    @Override
    AWSResourceType getAWSResourceType() {
        return LAMBDA_FUNCTION;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(new ArrayList<CFSectionBuilder>() {{
            add(titleWithAggregateBuilders("Code",
                    singleLine("S3Bucket", s3Bucket),
                    singleLine("S3Key", s3Key)
            ));
            add(singleLine("Handler", handler));
            add(singleLine("Runtime", runtime));
            add(singleLine("MemorySize", memory));
            add(titleWithAggregateBuilders("Role", fromPlainString(role)));
            add(singleLine("Description", description));
            add(singleLine("Timeout", timeout));

            if (!isNullOrEmpty(environmentVariables)) {
                add(titleWithAggregateBuilders("Environment",
                        titleWithAggregateBuilders("Variables",
                                environmentVariables.entrySet().stream()
                                        .map(entry -> singleLine(entry.getKey(), entry.getValue()))
                                        .collect(Collectors.toList()))
                ));
            }
        }});
    }

    private static boolean isNullOrEmpty(final Map map) {
        return map == null || map.isEmpty();
    }
}