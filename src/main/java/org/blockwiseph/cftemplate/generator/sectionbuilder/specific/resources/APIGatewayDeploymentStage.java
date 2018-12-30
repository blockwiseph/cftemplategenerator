package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Value class for APIGateway deployment stage of cloud formation template.
 * This can be used hand in hand with
 * @see org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources.APIGatewayDeployment
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-stage.html">AWS APIGateway Deployment Stage Documentation</a>
 */
@Builder
public class APIGatewayDeploymentStage extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String restAPIId;
    private final String stageName;
    private final String deploymentId;

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.API_GATEWAY_STAGE;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("RestApiId", restAPIId),
                singleLine("StageName", stageName),
                singleLine("DeploymentId", deploymentId)
        );
    }
}
