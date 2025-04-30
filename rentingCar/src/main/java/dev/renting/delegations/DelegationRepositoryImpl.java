package dev.renting.delegations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DelegationRepositoryImpl implements DelegationRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final String tableName = "Delegations";

    @Autowired
    public DelegationRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @Override
    public <T> void save(T item) {
        DynamoDbTable<T> table =
                enhancedClient.table(
                        tableName,
                        TableSchema.fromBean((Class<T>) item.getClass()));
        table.putItem(item);
    }

    @Override
    public <T> T get(String partitionKey, String sortKey, Class<T> clazz) {
        DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
        Key key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();
        return table.getItem(key);
    }

    @Override
    public <T> List<T> listByPartitionKey(String partitionKey, Class<T> clazz) {
        DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(partitionKey));
        List<T> items = new ArrayList<>();
        table.query(queryConditional).items().forEach(items::add);
        return items;
    }

    @Override
    public List<Car> listAllCars() {
        DynamoDbTable<Car> table = enhancedClient.table(tableName, TableSchema.fromBean(Car.class));
        List<Car> cars = new ArrayList<>();
        // You can add a filterExpression if you want only items of type Car
        table.scan(ScanEnhancedRequest.builder().build()).items().forEach(cars::add);
        return cars;
    }
}
