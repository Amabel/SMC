package com.waseda.weibin.smc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author  Weibin Luo
 * @version Created on Dec 14, 2016 6:07:37 PM
 */
public interface Constants {
	public static final String TEMP_DIR_NAME = "temp/";
	public static final String LOG_DIR_NAME = "log/";
	public static final List<String> cacheFiles = new ArrayList<>(Arrays.asList(
			"slice.sh",
			"modex.sh",
			"spin.sh",
			"copy.sh",
			"pan",
			"_modex_.run"
			));
}
