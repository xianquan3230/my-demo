package com.lcoil.drools.demo.test;

import com.alibaba.fastjson.JSON;
import com.lcoil.drools.demo.Results;
import com.lcoil.drools.demo.model.Policys;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @Classname ApiTest
 * @Description TODO
 * @Date 2022/1/15 3:51 PM
 * @Created by l-coil
 */
public class ApiTest {

    private KieContainer kieContainer;
    private Policys policy;

    @Before
    public void init() {
        // 构建KieServices
        KieServices kieServices = KieServices.Factory.get();
        kieContainer = kieServices.getKieClasspathContainer();

        policy = new Policys();
        policy.setSex("男");
        policy.setAge(16);
        policy.setUserSingle(false);
        policy.setUserMarry(false);
        policy.setUserParenting(false);
        System.out.println("决策请求：" + JSON.toJSONString(policy));
    }

    @Test
    public void test_drools() {
        KieSession kieSession = kieContainer.newKieSession("all-rules");
        kieSession.insert(policy);
        Results result = new Results();
        kieSession.setGlobal("res", result);
        int count = kieSession.fireAllRules();

        System.out.println("Fire rule(s)：" + count);
        System.out.println("决策结果(Drools)：" + result);

        kieSession.dispose();
    }

}

