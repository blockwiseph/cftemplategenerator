package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.AWSResourceType.USER_POOL_CLIENT;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Value class for a CognitoUserPool Client in the cloud formation template.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-cognito-userpoolclient.html">AWS CognitoUserPoolClient Documentation</a>
 */
@Builder
public class CognitoUserPoolClient extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String clientName;
    private final boolean generateSecret;
    private final String userPoolId;

    @Override
    AWSResourceType getAWSResourceType() {
        return USER_POOL_CLIENT;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("ClientName", clientName),
                singleLine("GenerateSecret", generateSecret),
                singleLine("UserPoolId", userPoolId)
        );
    }
}