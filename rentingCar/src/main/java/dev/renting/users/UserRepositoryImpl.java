// UserRepositoryImpl.java
package dev.renting.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final String tableName = "Users";

    @Autowired
    public UserRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
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
}
