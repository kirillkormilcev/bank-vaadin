package dev.kormilcev.bank.repository;

import java.util.List;
import java.util.Optional;

public interface Dao<T, K> {

  T create(T t);

  T update(T t);

  Optional<T> findById(K id);

  List<T> findAll();
}
