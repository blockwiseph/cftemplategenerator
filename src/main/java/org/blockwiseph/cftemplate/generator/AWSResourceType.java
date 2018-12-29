package org.blockwiseph.cftemplate.generator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Enum containing the different AWS Resource types that can be put in a cloud formation template.
 * The types here are the ones supported by the template generator.
 *
 * @link https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html for more.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AWSResourceType {

    S3_BUCKET("AWS::S3::Bucket"),
    CLOUDFRONT_DISTRIBUTION("AWS::CloudFront::Distribution"),
    LAMBDA_FUNCTION("AWS::Lambda::Function"),
    DYNAMO_DB_TABLE("AWS::DynamoDB::Table"),
    USER_POOL("AWS::Cognito::UserPool"),
    USER_POOL_CLIENT("AWS::Cognito::UserPoolClient"),
    IDENTITY_POOL("AWS::Cognito::IdentityPool"),
    IDENTITY_POOL_ROLE_ATTACHMENT("AWS::Cognito::IdentityPoolRoleAttachment"),
    SNS_TOPIC("AWS::SNS::Topic"),
    API_GATEWAY_REST_API("AWS::ApiGateway::RestApi"),
    API_GATEWAY_AUTHORIZER("AWS::ApiGateway::Authorizer"),
    API_GATEWAY_RESOURCE("AWS::ApiGateway::Resource"),
    API_GATEWAY_METHOD("AWS::ApiGateway::Method"),
    API_GATEWAY_DEPLOYEMENT("AWS::ApiGateway::Deployment"),
    API_GATEWAY_STAGE("AWS::ApiGateway::Stage"),
    IAM_ROLE("AWS::IAM::Role");

    private final String typeRepresentation;

    @Override
    public String toString() {
        return typeRepresentation;
    }
}
