package org.mbds.clients.mapper;

public interface IClientMapper<T,K> {
    K map(T value);
}
