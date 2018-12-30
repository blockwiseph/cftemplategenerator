package org.blockwiseph.cftemplate.generator.sectionbuilder.specific.resources;

import static com.amazonaws.util.CollectionUtils.isNullOrEmpty;
import static org.blockwiseph.cftemplate.generator.AWSResourceType.DYNAMO_DB_TABLE;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.aggregating;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.listItem;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.listOf;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.singleLine;
import static org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilderFactory.titleWithAggregateBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.blockwiseph.cftemplate.generator.AWSResourceType;
import org.blockwiseph.cftemplate.generator.sectionbuilder.CFSectionBuilder;
import org.blockwiseph.cftemplate.generator.sectionbuilder.DelegateCFSectionBuilder;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.StreamViewType;

/**
 * Value class for a DynamoDbTable in the cloud formation template.
 * globalSecondaryIndexes are optional attribute.
 *
 * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-dynamodb-table.html">AWS DynamoDB Table Documentation</a>
 */
@Builder
public class DynamoDBTable extends ResourceSectionBuilder {

    @Getter(AccessLevel.PACKAGE)
    private final String resourceId;

    private final String tableName;
    private final Key partitionKey;
    private final Provision provisionedThroughput;
    private final List<GlobalSecondaryIndex> globalSecondaryIndexes;
    private final StreamViewType streamViewType;

    @Override
    AWSResourceType getAWSResourceType() {
        return DYNAMO_DB_TABLE;
    }

    @Override
    CFSectionBuilder resourceProperties() {
        final List<CFSectionBuilder> allKeyAttributes =
                Stream.concat(
                        //Add the partition key attributes
                        Stream.of(partitionKey.getHashKey(), partitionKey.getRangeKey()),

                        //Add all the GSI Attributes
                        Optional.ofNullable(globalSecondaryIndexes)
                                .orElse(ImmutableList.of())
                                .stream()
                                .map(GlobalSecondaryIndex::getKey)
                                .map(key -> Stream.of(key.getHashKey(), key.getRangeKey()))
                                .flatMap(Function.identity())

                ).filter(Objects::nonNull).collect(Collectors.toList());

        return aggregating(new ArrayList<CFSectionBuilder>() {{
            add(singleLine("TableName", tableName));
            add(provisionedThroughput);
            add(titleWithAggregateBuilders("StreamSpecification",
                    singleLine("StreamViewType", streamViewType.name())
            ));
            add(titleWithAggregateBuilders("AttributeDefinitions", listOf(allKeyAttributes)));
            add(titleWithAggregateBuilders("KeySchema", partitionKey));

            if (!isNullOrEmpty(globalSecondaryIndexes)) {
                add(titleWithAggregateBuilders("GlobalSecondaryIndexes", listOf(globalSecondaryIndexes)));
            }
        }});
    }

    /**
     * Value class for a single DynamoDB Global Secondary Index in the list of GSIs of the DynamoDB Table in the
     * Cloud Formation Template.
     *
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-dynamodb-gsi.html">AWS DynamoDB GlobalSecondaryIndex Documentation</a>
     */
    @Builder
    @Getter
    public static class GlobalSecondaryIndex extends DelegateCFSectionBuilder {

        private final String name;
        private final Key key;
        private final ProjectionType projectionType;
        private final Provision provisionedThroughput;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(
                    singleLine("IndexName", name),
                    titleWithAggregateBuilders("KeySchema", key),
                    titleWithAggregateBuilders("Projection",
                            singleLine("ProjectionType", projectionType)),
                    provisionedThroughput
            );
        }
    }

    /**
     * Value class representing a DynamoDB key. It contains a pair of hash key and range key.
     * This can be used to compose DynamoDB table partition key and GlobalSecondaryIndex keys.
     * The range key is optional.
     *
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-dynamodb-keyschema.html">AWS DynamoDB KeySchema Documentation</a>
     */
    @Builder
    @Getter
    public static class Key extends DelegateCFSectionBuilder {

        private final Attribute hashKey;
        private final Attribute rangeKey;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(new ArrayList<CFSectionBuilder>() {{
                add(keyListItem(hashKey.getName(), "HASH"));

                Optional.ofNullable(rangeKey).ifPresent(key ->
                        add(keyListItem(rangeKey.getName(), "RANGE")));
            }});
        }

        private CFSectionBuilder keyListItem(final String keyName, final String keyType) {
            return listItem(
                    singleLine("AttributeName", keyName),
                    singleLine("KeyType", keyType));
        }
    }

    /**
     * Value class that represents a single DynamoDB Attribute in the list of attribute defintions
     * for a DynamoDB Table Cloud formation resource.
     *
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-dynamodb-attributedef.html">AWS DynamoDB Attribute Documentation</a>
     */
    @Builder
    @Getter
    public static class Attribute extends DelegateCFSectionBuilder {

        private final String name;
        private final Type type;

        @Override
        protected CFSectionBuilder delegate() {
            return aggregating(
                    singleLine("AttributeName", name),
                    singleLine("AttributeType", type));
        }
    }

    /**
     * Value class that represents read and write capacity provisioning for a DynamoDB table, or a DynamoDB GlobalSecondaryIndex,
     * in the DynamoDB table resource section of the cloud formation template.
     *
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-dynamodb-provisionedthroughput.html">AWS DynamoDB ProvisionedThroughput Documentation</a>
     */
    @Builder
    public static class Provision extends DelegateCFSectionBuilder {

        private final int readCapacity;
        private final int writeCapacity;

        @Override
        protected CFSectionBuilder delegate() {
            return titleWithAggregateBuilders("ProvisionedThroughput",
                    singleLine("ReadCapacityUnits", readCapacity),
                    singleLine("WriteCapacityUnits", writeCapacity)
            );
        }
    }

    /**
     * Value class that represents the DynamoDB Attribute types, to be used in the attribute schema,
     * when defining attributes of the DynamoDB Table resource in the cloud formation template.
     *
     * @see <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-dynamodb-attributedef.html#cfn-dynamodb-attributedef-attributename-attributetype">AWS DynamoDB AttributeType Documentation</a>
     */
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Type {
        STRING("S"),
        NUMBER("N"),
        BINARY("B"),
        BOOLEAN("BOOL"),
        STRING_SET("SS");

        private final String type;

        @Override
        public String toString() {
            return type;
        }
    }
}