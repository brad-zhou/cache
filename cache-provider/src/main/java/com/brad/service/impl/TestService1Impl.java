package com.brad.service.impl;

import com.brad.anno.Cache;
import com.brad.anno.Cacheable;
import com.brad.service.Test1Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * TestService1Impl
 **/
@Service
@Cache
public class TestService1Impl implements Test1Service {

    @Value("${saas.enable:false}")
    private boolean sassEnable;

    @Cacheable
    @Override
    public void test() {

    }
}
