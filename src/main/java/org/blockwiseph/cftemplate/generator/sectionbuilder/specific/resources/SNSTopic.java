package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;

import org.apache.commons.lang3.StringUtils;
import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Value class for SNS Topic of cloud formation template.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-sns-topic.html">AWS SNS Topic Documentation</a>
 */
@Builder
public class SNSTopic extends ResourceSectionBuilder {

    private static final int DISPLAY_NAME_MIN_SNS_LENGTH = 10;

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String topicName;
    private final String displayName;

    public SNSTopic(final String resourceId, final String topicName, final String displayName) {
        Preconditions.checkArgument(StringUtils.length(displayName) <= DISPLAY_NAME_MIN_SNS_LENGTH,
                "Display name length must be less than or equal to 10"
        );

        this.resourceId = resourceId;
        this.topicName = topicName;
        this.displayName = displayName;
    }

    @Override
    AWSResourceType getAWSResourceType() {
        return AWSResourceType.SNS_TOPIC;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        return aggregating(
                singleLine("TopicName", topicName),
                singleLine("DisplayName", displayName)
        );
    }
}