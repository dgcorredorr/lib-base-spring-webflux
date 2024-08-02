package com.fstech.provider.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.fstech.provider.model.ServiceErrorModel;

/**
 * Repositorio de datos para la entidad ServiceErrorModel.
 * Proporciona métodos para acceder y administrar errores de servicio en la base de datos MongoDB de manera reactiva.
 */
@Repository
public interface ServiceErrorRepository extends ReactiveMongoRepository<ServiceErrorModel, String> {}
