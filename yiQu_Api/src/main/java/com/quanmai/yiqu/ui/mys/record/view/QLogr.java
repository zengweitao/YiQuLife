package com.quanmai.yiqu.ui.mys.record.view;

/** Log utility class to handle the log tag and DEBUG-only logging. */
public final class QLogr {
	public static void d(String message) {
		// if (BuildConfig.DEBUG) {
		// Log.d("TimesSquare", message);
		// }
	}

	public static void d(String message, Object... args) {
		// if (BuildConfig.DEBUG) {
		// d(String.format(message, args));
		// }
	}
}
