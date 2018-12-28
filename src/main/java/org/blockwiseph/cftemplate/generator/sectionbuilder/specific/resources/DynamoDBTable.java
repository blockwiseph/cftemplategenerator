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