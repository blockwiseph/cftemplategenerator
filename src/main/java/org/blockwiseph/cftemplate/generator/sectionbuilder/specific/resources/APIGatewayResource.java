package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.AWSResourceType.API_GATEWAY_RESOURCE;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value class for APIGatewayResource of cloud formation template. This represents an API Entity.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-resource.html">AWS APIGateway Resource Documentation</a>
 */
@Builder
@EqualsAndHashCode
public class APIGatewayResource extends ResourceSectionBuilder {

    @Getter
    private final String resourceId;

    private final String parentId;
    private final String restApiId;
    private final String pathPart;

    @Override
    AWSResourceType getAWSResourceType() {
        return API_GATEWAY_RESOURCE;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("ParentId", parentId),
                singleLine("RestApiId", restApiId),
                singleLine("PathPart", pathPart)
        );
    }
}