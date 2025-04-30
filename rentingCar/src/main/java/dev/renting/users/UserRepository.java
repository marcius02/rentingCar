// UserRepository.java
package dev.renting.users;

public interface UserRepository {
    <T> void save(T item);
}
