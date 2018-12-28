package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainStrings;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.listOf;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import java.util.List;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder
public class APIGatewayAuthorizer extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String restAPIId;
    private final String authorizerName;
    private final String identitySource;
    private final AuthorizerType authorizerType;
    private final List<String> providerArns;

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.API_GATEWAY_AUTHORIZER;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("Name", authorizerName),
                singleLine("IdentitySource", identitySource),
                singleLine("Type", authorizerType),
                titleWithAggregateBuilders("ProviderARNs", listOf(fromPlainStrings(providerArns))),
                singleLine("RestApiId", restAPIId)
        );
    }

    public enum AuthorizerType {
        COGNITO_USER_POOLS,
        TOKEN,
        REQUEST
    }
}
