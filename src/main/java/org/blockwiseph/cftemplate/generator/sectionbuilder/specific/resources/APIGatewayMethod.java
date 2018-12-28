package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromKeyValueMap;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainString;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.fromPlainStrings;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.listOf;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources.commonparts.APIResponse;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import com.amazonaws.http.HttpMethodName;

@Builder
public class APIGatewayMethod extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String operationName;
    private final String restAPIId;
    private final String apiResourceId;
    private final String authorizerId;
    private final AuthorizationType authorizationType;
    private final HttpMethodName httpMethod;
    private final IntegrationType integrationType;
    private final HttpMethodName integrationHttpMethod;
    private final String lambdaArn;
    private final Map<String, String> requestTemplates;
    private final PassthroughBehavior passthroughBehavior;
    private final List<APIResponse> integrationResponses;
    private final List<APIResponse> methodResponses;

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.API_GATEWAY_METHOD;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(new ArrayList<CFSectionBuilder>() {{
            Optional.ofNullable(operationName).ifPresent(str ->
                    add(singleLine("OperationName", operationName)));

            add(singleLine("ResourceId", apiResourceId));
            add(singleLine("RestApiId", restAPIId));

            Optional.ofNullable(authorizerId).ifPresent(str ->
                    add(singleLine("AuthorizerId", authorizerId)));

            add(singleLine("AuthorizationType", authorizationType));

            add(singleLine("HttpMethod", httpMethod));
            add(titleWithAggregateBuilders("Integration", new ArrayList<CFSectionBuilder>() {{
                        add(singleLine("Type", integrationType));

                        Optional.ofNullable(integrationHttpMethod).ifPresent(method ->
                                add(singleLine("IntegrationHttpMethod", integrationHttpMethod)));

                        add(titleWithAggregateBuilders("RequestTemplates", fromKeyValueMap(requestTemplates)));

                        add(singleLine("PassthroughBehavior", passthroughBehavior));

                        Optional.ofNullable(lambdaArn).ifPresent(arn ->
                                add(titleWithAggregateBuilders("Uri",
                                        titleWithAggregateBuilders("Fn::Join",
                                                listOf(fromPlainString("\"\""),
                                                        listOf(fromPlainStrings(ImmutableList.of(
                                                                "\"arn:aws:apigateway:\"",
                                                                "!Ref AWS::Region",
                                                                "\":lambda:path/2015-03-31/functions/\"",
                                                                lambdaArn,
                                                                "/invocations"
                                                        )))
                                                )
                                        )
                                ))
                        );

                        add(titleWithAggregateBuilders("IntegrationResponses", listOf(integrationResponses)));
                    }}
            ));
            add(titleWithAggregateBuilders("MethodResponses", listOf(methodResponses)));
        }});
    }

    public enum AuthorizationType {
        NONE,
        AWS_IAM,
        CUSTOM,
        COGNITO_USER_POOLS,
    }

    public enum IntegrationType {
        AWS,
        AWS_PROXY,
        MOCK
    }

    public enum PassthroughBehavior {
        WHEN_NO_MATCH,
        WHEN_NO_TEMPLATES,
        NEVER
    }
}
