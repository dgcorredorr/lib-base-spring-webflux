package com.fstech.provider.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.fstech.provider.model.TraceabilityModel;

/**
 * Repositorio de datos para la entidad TraceabilityModel.
 * Proporciona métodos para acceder y administrar registros de trazabilidad en la base de datos MongoDB de manera reactiva.
 */
@Repository
public interface TraceabilityRepository extends ReactiveMongoRepository<TraceabilityModel, String> {}
