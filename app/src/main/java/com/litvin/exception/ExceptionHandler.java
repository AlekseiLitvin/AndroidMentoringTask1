package com.litvin.exception;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context;

    public ExceptionHandler(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(context, ExceptionActivity.class);
        StackTraceElement[] stackTrace = e.getStackTrace();
        String stacktraceString = Arrays.stream(stackTrace)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        intent.putExtra(ExceptionActivity.STACKTRACE, stacktraceString);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
