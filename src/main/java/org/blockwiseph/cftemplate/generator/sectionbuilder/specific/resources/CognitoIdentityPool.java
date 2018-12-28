package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

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
public class CognitoIdentityPool extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String identityPoolName;
    private final boolean allowUnauthenticatedIdentities;
    private final List<CognitoIdentityProvider> cognitoIdentityProviders;

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.IDENTITY_POOL;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("IdentityPoolName", identityPoolName),
                singleLine("AllowUnauthenticatedIdentities", allowUnauthenticatedIdentities),
                titleWithAggregateBuilders("CognitoIdentityProviders", listOf(cognitoIdentityProviders))
        );
    }

    @Builder
    public static class CognitoIdentityProvider extends DelegateCFSectionBuilder {

        private final String clientId;
        private final String providerName;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(
                    singleLine("ClientId", clientId),
                    singleLine("ProviderName", providerName)
            );
        }
    }
}