package com.wpj.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface TaegerI {
    void say();
}

/**
 * @author wangpejian
 * @date 19-7-30 下午4:30
 */
@Slf4j
public class Main {

    public static void main(String[] args) {

        TaegerI proxy = new JDynamicAopProxy(new Target()).getProxy();
        proxy.say();

        TaegerI proxy2 = new GCLibAopProxy(new Target()).getProxy();
        proxy2.say();

        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new MyMethodInterceptor());
        enhancer.setSuperclass(Target.class);
        TaegerI proxy3 = (TaegerI) enhancer.create();
        proxy3.say();
    }

}

class Target implements TaegerI {

    @Override
    public void say() {
        System.out.println("target invoke");
    }

}

@Slf4j
class JDynamicAopProxy implements InvocationHandler {

    private Target target;

    JDynamicAopProxy(Target target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("JDynamicAopProxy before invoke");
        method.invoke(target, args);
        System.out.println("JDynamicAopProxy after invoke");

        return null;
    }

    <T> T getProxy() {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
    }
}

class GCLibAopProxy implements MethodInterceptor {

    private Target target;

    GCLibAopProxy(Target target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("GCLibAopProxy before invoke");
        Object res = methodProxy.invokeSuper(o, objects);
        System.out.println("GCLibAopProxy after invoke");

        return res;
    }

    <T> T getProxy() {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);

        return (T) enhancer.create();
    }
}


class MyMethodInterceptor implements MethodInterceptor {


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("GCLibAopProxy before invoke");
        Object res = methodProxy.invokeSuper(o, objects);
        System.out.println("GCLibAopProxy after invoke");

        return res;
    }

}