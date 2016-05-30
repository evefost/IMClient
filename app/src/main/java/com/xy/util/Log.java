package com.xy.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * A more natural android logging facility. WARNING: CHECK OUT COMMON PITFALLS BELOW
 * Unlike {@link android.util.Log}, Log provides sensible defaults. Debug and Verbose
 * logging is enabled for applications that have "android:debuggable=true" in their
 * AndroidManifest.xml. For apps built using SDK Tools r8 or later, this means any debug
 * build. Release builds built with r8 or later will have verbose and debug log messages
 * turned off. The default tag is automatically set to your app's packagename, and the
 * current context (eg. activity, service, application, etc) is appended as well. You can
 * add an additional parameter to the tag using {@link android.util.Log (String)}. Log-levels can be
 * programatically overridden for specific instances using
 * {@link android.util.Log (String, boolean, boolean)}. Log messages may optionally use
 * {@link String#format(String, Object...)} formatting, which will not be evaluated unless
 * the log statement is output. Additional parameters to the logging statement are treated
 * as varrgs parameters to {@link String#format(String, Object...)} Also, the current file
 * and line is automatically appended to the tag (this is only done if debug is enabled
 * for performance reasons). COMMON PITFALLS: * Make sure you put the exception FIRST in
 * the call. A common mistake is to place it last as is the android.util.Log convention,
 * but then it will get treated as varargs parameter. * vararg parameters are not appended
 * to the log message! You must insert them into the log message using %s or another
 * similar format parameter Usage Examples: Ln.v("hello there"); Ln.d("%s %s", "hello",
 * "there"); Ln.e( exception, "Error during some operation"); Ln.w( exception,
 * "Error during %s operation", "some other");
 */
@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public final class Log {
    /**
     * config is initially set to BaseConfig() with sensible defaults, then replaced by
     * BaseConfig(ContextSingleton) during guice static injection pass.
     */
    private static final BaseConfig CONFIG = new BaseConfig();

    /**
     * print is initially set to Print(), then replaced by guice during static injection
     * pass. This allows overriding where the log message is delivered to.
     */
    private static Print print = new Print();

    private Log() {
    }

    public static int v(Throwable t) {
        return CONFIG.minimumLogLevel <= android.util.Log.VERBOSE ? print.println(android.util.Log.VERBOSE, android.util.Log.getStackTraceString(t)) : 0;
    }

    public static int v(Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.VERBOSE) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = args.length > 0 ? String.format(s, args) : s;
        return print.println(android.util.Log.VERBOSE, message);
    }



    public static int v(Throwable throwable, Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.VERBOSE) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = (args.length > 0 ? String.format(s, args) : s) + '\n'
                + android.util.Log.getStackTraceString(throwable);
        return print.println(android.util.Log.VERBOSE, message);
    }

    public static int d(Throwable t) {
        return CONFIG.minimumLogLevel <= android.util.Log.DEBUG ? print.println(android.util.Log.DEBUG, android.util.Log.getStackTraceString(t)) : 0;
    }

    public static int d(Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.DEBUG) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = args.length > 0 ? String.format(s, args) : s;
        return print.println(android.util.Log.DEBUG, message);
    }

    public static int d(Throwable throwable, Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.DEBUG) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = (args.length > 0 ? String.format(s, args) : s) + '\n'
                + android.util.Log.getStackTraceString(throwable);
        return print.println(android.util.Log.DEBUG, message);
    }

    public static int i(Throwable t) {
        return CONFIG.minimumLogLevel <= android.util.Log.INFO ? print.println(android.util.Log.INFO, android.util.Log.getStackTraceString(t)) : 0;
    }

    public static int i(Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.INFO) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = args.length > 0 ? String.format(s, args) : s;
        return print.println(android.util.Log.INFO, message);
    }

    public static int i(Throwable throwable, Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.INFO) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = (args.length > 0 ? String.format(s, args) : s) + '\n'
                + android.util.Log.getStackTraceString(throwable);
        return print.println(android.util.Log.INFO, message);
    }

    public static int w(Throwable t) {
        return CONFIG.minimumLogLevel <= android.util.Log.WARN ? print.println(android.util.Log.WARN, android.util.Log.getStackTraceString(t)) : 0;
    }

    public static int w(Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.WARN) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = args.length > 0 ? String.format(s, args) : s;
        return print.println(android.util.Log.WARN, message);
    }

    public static int w(Throwable throwable, Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.WARN) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = (args.length > 0 ? String.format(s, args) : s) + '\n'
                + android.util.Log.getStackTraceString(throwable);
        return print.println(android.util.Log.WARN, message);
    }

    public static int e(Throwable t) {
        return CONFIG.minimumLogLevel <= android.util.Log.ERROR ? print.println(android.util.Log.ERROR, android.util.Log.getStackTraceString(t)) : 0;
    }

    public static int e(Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.ERROR) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = args.length > 0 ? String.format(s, args) : s;
        return print.println(android.util.Log.ERROR, message);
    }

    public static int e(Throwable throwable, Object s1, Object... args) {
        if (CONFIG.minimumLogLevel > android.util.Log.ERROR) {
            return 0;
        }

        final String s = StringUtils.toString(s1);
        final String message = (args.length > 0 ? String.format(s, args) : s) + '\n'
                + android.util.Log.getStackTraceString(throwable);
        return print.println(android.util.Log.ERROR, message);
    }

    public static boolean isDebugEnabled() {
        return CONFIG.minimumLogLevel <= android.util.Log.DEBUG;
    }

    public static boolean isVerboseEnabled() {
        return CONFIG.minimumLogLevel <= android.util.Log.VERBOSE;
    }

    public static Config getConfig() {

        return CONFIG;
    }

    public static String logLevelToString(int loglevel) {
        switch (loglevel) {
            case android.util.Log.VERBOSE:
                return "VERBOSE";
            case android.util.Log.DEBUG:
                return "DEBUG";
            case android.util.Log.INFO:
                return "INFO";
            case android.util.Log.WARN:
                return "WARN";
            case android.util.Log.ERROR:
                return "ERROR";
            case android.util.Log.ASSERT:
                return "ASSERT";

            default:
                return "UNKNOWN";
        }

    }

    public static void setPrint(Print print) {
        Log.print = print;
    }

    public interface Config {
        int getLoggingLevel();

        void setLoggingLevel(int level);
    }

    public static class BaseConfig implements Config {
        protected int minimumLogLevel = android.util.Log.VERBOSE;
        protected String packageName = "";
        protected String scope = "";

        protected BaseConfig() {
        }

        public BaseConfig(Application context) {
            try {
                packageName = context.getPackageName();
                final int flags = context.getPackageManager().getApplicationInfo(packageName, 0).flags;
                minimumLogLevel = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 ? android.util.Log.VERBOSE : android.util.Log.INFO;
                scope = packageName.toUpperCase();
                Log.d("Configuring Logging, minimum log level is %s", logLevelToString(minimumLogLevel));
            } catch (Exception e) {
                android.util.Log.e(packageName, "Error configuring logger", e);
            }
        }

        @Override
        public int getLoggingLevel() {
            return minimumLogLevel;
        }

        @Override
        public void setLoggingLevel(int level) {
            minimumLogLevel = level;
        }
    }

    /**
     * Default implementation logs to android.util.Log
     */
    public static class Print {
        private static final int DEFAULT_STACK_TRACE_LINE_COUNT = 5;

        protected static String getScope() {
            if (CONFIG.minimumLogLevel <= android.util.Log.DEBUG) {
                final StackTraceElement trace = Thread.currentThread().getStackTrace()[DEFAULT_STACK_TRACE_LINE_COUNT];
                return CONFIG.scope + "(" + trace.getFileName() + ":" + trace.getLineNumber() + ")";
            }

            return CONFIG.scope;
        }

        public int println(int priority, String msg) {
            return android.util.Log.println(priority, getScope(), processMessage(msg));
        }

        protected String processMessage(String msg) {
            if (CONFIG.minimumLogLevel <= android.util.Log.DEBUG) {
                msg = String.format("%s %s", Thread.currentThread().getName(), msg);
            }
            return msg;
        }

    }

    /**
     * A simple event log with records containing a name, thread ID, and timestamp.
     */
    public static class MarkerLog {
        public static final boolean ENABLED = CONFIG.minimumLogLevel <= android.util.Log.DEBUG;

        /**
         * Minimum duration from first marker to last in an marker log to warrant logging.
         */
        private static final long MIN_DURATION_FOR_LOGGING_MS = 0;
        private final List<Marker> mMarkers = new ArrayList<Marker>();
        private boolean mFinished = false;

        /**
         * Adds a marker to this log with the specified name.
         */
        public synchronized void add(String name, long threadId) {
            if (mFinished) {
                throw new IllegalStateException("Marker added to finished log");
            }

            mMarkers.add(new Marker(name, threadId, SystemClock.elapsedRealtime()));
        }

        /**
         * Closes the log, dumping it to logcat if the time difference between
         * the first and last markers is greater than {@link #MIN_DURATION_FOR_LOGGING_MS}.
         *
         * @param header Header string to print above the marker log.
         */
        public synchronized void finish(String header) {
            mFinished = true;

            long duration = getTotalDuration();
            if (duration <= MIN_DURATION_FOR_LOGGING_MS) {
                return;
            }

            long prevTime = mMarkers.get(0).time;
            d("(%-4d ms) %s", duration, header);
            for (Marker marker : mMarkers) {
                long thisTime = marker.time;
                d("(+%-4d) [%2d] %s", (thisTime - prevTime), marker.thread, marker.name);
                prevTime = thisTime;
            }
        }

        @Override
        protected void finalize() throws Throwable {
            // Catch requests that have been collected (and hence end-of-lifed)
            // but had no debugging output printed for them.
            if (!mFinished) {
                finish("Request on the loose");
                e("Marker log finalized without finish() - uncaught exit point for request");
            }
        }

        /**
         * Returns the time difference between the first and last events in this log.
         */
        private long getTotalDuration() {
            if (mMarkers.size() == 0) {
                return 0;
            }

            long first = mMarkers.get(0).time;
            long last = mMarkers.get(mMarkers.size() - 1).time;
            return last - first;
        }

        private static class Marker {
            public final String name;
            public final long thread;
            public final long time;

            public Marker(String name, long thread, long time) {
                this.name = name;
                this.thread = thread;
                this.time = time;
            }
        }
    }
}
