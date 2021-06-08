package com.lishuaihua.net.log;

import com.lishuaihua.logger.JackLogger;

import org.jetbrains.annotations.NotNull;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(@NotNull String message) {
        JackLogger.d(message);
    }
}

