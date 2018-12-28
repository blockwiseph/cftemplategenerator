package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.AWSResourceType.CLOUDFRONT_DISTRIBUTION;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.listOf;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import java.util.List;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder
public class CloudFrontDistribution extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final List<Origin> origins;
    private final List<ErrorResponse> errorResponses;
    private final String rootObject;
    private final String targetOriginId;

    @Override
    AWSResourceType getAWSResourceType() {
        return CLOUDFRONT_DISTRIBUTION;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return titleWithAggregateBuilders("DistributionConfig", aggregating(
                titleWithAggregateBuilders("Origins", listOf(origins)),
                titleWithAggregateBuilders("CustomErrorResponses", listOf(errorResponses)),
                titleWithAggregateBuilders("DefaultCacheBehavior", getDefaultCacheBehavior()),
                singleLine("DefaultRootObject", rootObject),
                singleLine("HttpVersion", "http2"),
                singleLine("Enabled", "true")
        ));
    }

    private CFSectionBuilder getDefaultCacheBehavior() {
        return aggregating(
                titleWithAggregateBuilders("ForwardedValues",
                        singleLine("QueryString", "false")
                ),
                singleLine("TargetOriginId", targetOriginId),
                singleLine("ViewerProtocolPolicy", "allow-all")
        );
    }

    @Builder
    public static class Origin extends DelegateCFSectionBuilder {
        private final String id;
        private final String domainName;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(
                    titleWithAggregateBuilders("CustomOriginConfig",
                            singleLine("OriginProtocolPolicy", "http-only")
                    ),
                    singleLine("DomainName", domainName),
                    singleLine("Id", id)
            );
        }
    }

    @Builder
    public static class ErrorResponse extends DelegateCFSectionBuilder {
        private final int errorCode;
        private final String responsePagePath;
        private final int responseCode;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(
                    singleLine("ErrorCode", errorCode),
                    singleLine("ResponsePagePath", responsePagePath),
                    singleLine("ResponseCode", responseCode)
            );
        }
    }
}