package com.offershopper.registrationandloginservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.offershopper.registrationandloginservice.bean.LoginInfoBean;

public interface LoginProxyRepository extends MongoRepository<LoginInfoBean, String>{

}
