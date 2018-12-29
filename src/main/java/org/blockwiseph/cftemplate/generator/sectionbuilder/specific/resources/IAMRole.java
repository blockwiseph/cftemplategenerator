package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.AWSResourceType.IAM_ROLE;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainStrings;
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

/**
 * Value class for IAM Role of cloud formation template.
 *
 * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-role.html
 */
@Builder
public class IAMRole extends ResourceSectionBuilder {

    private static final String POLICY_DOCUMENT_VERSION = "\"2012-10-17\"";

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final PolicyStatement assumableServicesStatement;
    private final List<String> allowedActions;

    @Override
    AWSResourceType getAWSResourceType() {
        return IAM_ROLE;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                iamPolicyDocument("AssumeRolePolicyDocument", assumableServicesStatement),
                singleLine("Path", "/"),
                titleWithAggregateBuilders("Policies",
                        listOf(aggregating(
                                singleLine("PolicyName", "root"),
                                iamPolicyDocument("PolicyDocument", allowedActionsStatement())
                        ))
                )
        );
    }

    private CFSectionBuilder iamPolicyDocument(final String policyDocumentName,
                                               final CFSectionBuilder... policyStatements) {
        return titleWithAggregateBuilders(policyDocumentName,
                singleLine("Version", POLICY_DOCUMENT_VERSION),
                titleWithAggregateBuilders("Statement", listOf(policyStatements))
        );
    }

    private CFSectionBuilder allowedActionsStatement() {
        return aggregating(
                singleLine("Effect", "Allow"),
                titleWithAggregateBuilders("Action", listOf(fromPlainStrings(allowedActions))),
                singleLine("Resource", "\"*\"")
        );
    }

    /**
     * Value class that represents an IAM Policy Statement, that can be used within the definition of an IAM Role
     * in the cloud formation template.
     *
     * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-role.html#cfn-iam-role-assumerolepolicydocument
     */
    @Builder
    public static class PolicyStatement extends DelegateCFSectionBuilder {

        private final String principal;
        private final String actionName;
        private final List<String> allowedServices;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(
                    singleLine("Effect", "Allow"),
                    titleWithAggregateBuilders("Principal",
                            titleWithAggregateBuilders(principal,
                                    listOf(fromPlainStrings(allowedServices)))),
                    singleLine("Action", "sts:" + actionName)
            );
        }
    }
}