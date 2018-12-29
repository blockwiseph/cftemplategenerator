package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Value class for APIGateway deployment resource of cloud formation template.
 *
 * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-deployment.html
 */
@Builder
public class APIGatewayDeployment extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String restAPIId;

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.API_GATEWAY_DEPLOYEMENT;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return singleLine("RestApiId", restAPIId);
    }
}
