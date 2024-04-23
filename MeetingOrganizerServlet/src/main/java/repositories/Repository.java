package repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<K, E> {
    E create(E e);
    List<E> getAll();
    Optional<E> getById(K id);
    boolean update(E e);
    boolean delete(K id);
}
