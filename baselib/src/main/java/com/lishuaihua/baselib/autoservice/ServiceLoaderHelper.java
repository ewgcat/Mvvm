package com.lishuaihua.baselib.autoservice;

import java.util.ServiceLoader;

public final class ServiceLoaderHelper {
    private ServiceLoaderHelper() {
    }

    public static <S> S load(Class<S> service) {
        try {
            return ServiceLoader.load(service).iterator().next();
        } catch (Exception e) {
            return null;
        }
    }
}
