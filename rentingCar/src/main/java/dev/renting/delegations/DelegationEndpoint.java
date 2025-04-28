package dev.renting.delegations;


import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.hilla.Endpoint;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;



@Endpoint
@AnonymousAllowed
public class DelegationEndpoint {

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public DelegationEndpoint(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void saveDelegation(Delegation delegation) {

        Map<String, AttributeValue> delegationMap = new HashMap<>();
        delegationMap.put("id", AttributeValue.builder().s(delegation.getId()).build());
        delegationMap.put("name", AttributeValue.builder().s(delegation.getName()).build());
        delegationMap.put("city", AttributeValue.builder().s(delegation.getCity()).build());
        delegationMap.put("address", AttributeValue.builder().s(delegation.getAddress()).build());
        delegationMap.put("phone", AttributeValue.builder().s(delegation.getPhone()).build());
        delegationMap.put("email", AttributeValue.builder().s(delegation.getEmail()).build());
        delegationMap.put("availableCarQty", AttributeValue.builder().n(String.valueOf(delegation.getAvailableCarQty())).build());

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName("Delegations")
                .item(delegationMap)
                .build());
    }
}
