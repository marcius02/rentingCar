package dev.renting;


import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.hilla.Endpoint;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

@Endpoint
@AnonymousAllowed
public class CarEndpoint {

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public CarEndpoint(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void saveCar(Map<String, Object> carJson) {
        System.out.println("carJson:" + carJson);
        Map<String, AttributeValue> itemValues = new HashMap<>();

        carJson.forEach((key, value) -> itemValues.put(key, AttributeValue.builder().s(value.toString()).build()));
        System.out.println("itemValues:" + itemValues);
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("Cars")
                .item(itemValues)
                .build();

        dynamoDbClient.putItem(putItemRequest);
    }
}