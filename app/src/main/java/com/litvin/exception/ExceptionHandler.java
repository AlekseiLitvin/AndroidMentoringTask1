package com.litvin.exception;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context;

    public ExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(context, ExceptionActivity.class);
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringJoiner joiner = new StringJoiner("\n");
        for (StackTraceElement stackTraceElement : stackTrace) {
            String toString = stackTraceElement.toString();
            joiner.add(toString);
        }
        String stacktraceString = joiner.toString();
        intent.putExtra(ExceptionActivity.STACKTRACE, stacktraceString);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
