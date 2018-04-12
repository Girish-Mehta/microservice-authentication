package com.offershopper.registrationandloginservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.offershopper.registrationandloginservice.bean.RegisterInfoBean;

public interface RegisterProxyRepository extends MongoRepository<RegisterInfoBean, String>{

}
