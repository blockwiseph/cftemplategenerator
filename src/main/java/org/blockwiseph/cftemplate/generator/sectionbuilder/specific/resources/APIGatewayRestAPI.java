package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Value class for APIGateway RestAPI of cloud formation template.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-restapi.html">AWS APIGateway RestAPI Documentation</a>
 */
@Builder
public class APIGatewayRestAPI extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String name;
    private final String description;

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.API_GATEWAY_REST_API;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("Name", name),
                singleLine("Description", description)
        );
    }
}
