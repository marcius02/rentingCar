// UserRepositoryImpl.java
package dev.renting.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DynamoDbTable<User> userTable;

    @Autowired
    public UserRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table(
                        "Users",
                        TableSchema.fromBean(User.class));
    }

    @Override
    public void saveUser(User user) {
        userTable.putItem(user);
    }
}
