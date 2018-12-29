package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Value class for Cognito IdentityPool Role Attachment section of cloud formation template.
 * This is to attach authenticated and unauthenticated IAM Roles to the identity pool.
 *
 * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-cognito-identitypoolroleattachment.html
 */
@Builder
public class CognitoIdentityPoolAttachment extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String identityPoolId;
    private final String authenticatedRoleId;
    private final String unauthenticatedRoleId;

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.IDENTITY_POOL_ROLE_ATTACHMENT;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("IdentityPoolId", identityPoolId),
                titleWithAggregateBuilders("Roles",
                        singleLine("authenticated", authenticatedRoleId),
                        singleLine("unauthenticated", unauthenticatedRoleId)
                ));
    }
}