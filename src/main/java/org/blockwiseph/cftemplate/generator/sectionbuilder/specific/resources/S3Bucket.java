package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static com.amazonaws.util.CollectionUtils.isNullOrEmpty;
import static org.blockwiseph.cftemplate.generator.AWSResourceType.S3_BUCKET;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainStrings;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.listOf;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import com.amazonaws.http.HttpMethodName;

/**
 * Value class for S3 Bucket of cloud formation template. Only resourceId and bucketName is required.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html">AWS S3 Bucket Documentation</a>
 */
@Builder
public class S3Bucket extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String bucketName;
    private final String accessControl;
    private final String indexDocument;
    private final List<CorsRule> corsRules;

    @Override
    AWSResourceType getAWSResourceType() {
        return S3_BUCKET;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(new ArrayList<CFSectionBuilder>() {{
            add(singleLine("BucketName", bucketName));

            Optional.ofNullable(accessControl).ifPresent(str ->
                    add(singleLine("AccessControl", accessControl)));

            Optional.ofNullable(indexDocument).ifPresent(str ->
                    add(titleWithAggregateBuilders("WebsiteConfiguration",
                            singleLine("IndexDocument", indexDocument))));

            if (!isNullOrEmpty(corsRules)) {
                add(titleWithAggregateBuilders("CorsConfiguration",
                        titleWithAggregateBuilders("CorsRules", listOf(corsRules))));
            }
        }});
    }

    /**
     * Value class for a single CorsRule in the list of CorsRules of the S3 Bucket in the Cloud Formation Template.
     * AllowedMethods and AllowedOrigins are the required fields.
     *
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket-cors-corsrule.html">AWS S3 Bucket CorsRule Documentation</a>
     */
    @Builder
    public static class CorsRule extends DelegateCFSectionBuilder {

        private final String id;
        private final Integer maxAge;
        private final List<String> allowedHeaders;
        private final List<HttpMethodName> allowedMethods;
        private final List<String> allowedOrigins;
        private final List<String> exposedHeaders;

        @Override
        protected CFSectionBuilder delegate() {
            final List<String> allowedMethodStrings = allowedMethods.stream()
                    .map(HttpMethodName::toString)
                    .collect(Collectors.toList());

            return aggregating(new ArrayList<CFSectionBuilder>() {{
                Optional.ofNullable(id).ifPresent(i ->
                        add(singleLine("Id", id)));

                Optional.ofNullable(maxAge).ifPresent(age ->
                        add(singleLine("MaxAge", maxAge)));

                if (!isNullOrEmpty(allowedHeaders)) {
                    add(titleWithAggregateBuilders("AllowedHeaders", listOf(fromPlainStrings(allowedHeaders))));
                }

                add(titleWithAggregateBuilders("AllowedMethods", listOf(fromPlainStrings(allowedMethodStrings))));
                add(titleWithAggregateBuilders("AllowedOrigins", listOf(fromPlainStrings(allowedOrigins))));

                if (!isNullOrEmpty(exposedHeaders)) {
                    add(titleWithAggregateBuilders("ExposedHeaders", listOf(fromPlainStrings(exposedHeaders))));
                }
            }});
        }
    }
}
