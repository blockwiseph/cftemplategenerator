package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

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

@Builder
public class CognitoUserPool extends ResourceSectionBuilder {

    private static final int DEFAULT_VALIDITY_DAYS = 7;

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String userPoolName;
    private final boolean onlyAdminAllowedToCreateUser;
    private final int unusedAccountValidityDays;
    private final List<String> userNameAttributes;
    private final String mfaConfiguration;
    private final List<String> autoVerifiedAttributes;
    private final String emailVerificationMessage;
    private final String emailVerificationSubject;
    private final PasswordPolicy passwordPolicy;
    private final List<AttributeSchema> attributeSchemas;

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.USER_POOL;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("UserPoolName", userPoolName),
                titleWithAggregateBuilders("AdminCreateUserConfig",
                        singleLine("AllowAdminCreateUserOnly", onlyAdminAllowedToCreateUser),
                        singleLine("UnusedAccountValidityDays", DEFAULT_VALIDITY_DAYS)
                ),
                titleWithAggregateBuilders("UsernameAttributes",
                        listOf(fromPlainStrings(userNameAttributes))
                ),
                singleLine("MfaConfiguration", "\"" + mfaConfiguration + "\""),
                titleWithAggregateBuilders("AutoVerifiedAttributes",
                        listOf(fromPlainStrings(autoVerifiedAttributes))
                ),
                singleLine("EmailVerificationMessage", emailVerificationMessage),
                singleLine("EmailVerificationSubject", emailVerificationSubject),
                titleWithAggregateBuilders("Policies",
                        titleWithAggregateBuilders("PasswordPolicy", passwordPolicy)
                ),
                titleWithAggregateBuilders("Schema", listOf(attributeSchemas))
        );
    }

    @Builder
    public static class PasswordPolicy extends DelegateCFSectionBuilder {
        private final int minimumLength;
        private final boolean requireLowerCase;
        private final boolean requireNumbers;
        private final boolean requireUpperCase;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(
                    singleLine("MinimumLength", minimumLength),
                    singleLine("RequireLowercase", requireLowerCase),
                    singleLine("RequireNumbers", requireNumbers),
                    singleLine("RequireUppercase", requireUpperCase)
            );
        }
    }

    @Builder(toBuilder = true)
    public static class AttributeSchema extends DelegateCFSectionBuilder {
        private final String name;
        private final String attributeDataType;
        private final boolean mutable;
        private final boolean required;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(
                    singleLine("Name", name),
                    singleLine("AttributeDataType", attributeDataType),
                    singleLine("Mutable", mutable),
                    singleLine("Required", required)
            );
        }
    }
}