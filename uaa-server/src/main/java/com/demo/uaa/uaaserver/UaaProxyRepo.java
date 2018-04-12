package com.demo.uaa.uaaserver;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UaaProxyRepo extends MongoRepository<UaaModel, String> {

	public List<UaaModel> findByToken(String token);

}
