package com.waseda.weibin.smc.entry;

import com.waseda.weibin.smc.controller.ViewController;
import com.waseda.weibin.smc.util.Command;
import com.waseda.weibin.smc.util.FileProcessor;

public class SMC {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ViewController().launch();
		
//		String cmd = "pwd";
//		Command.execute(cmd);
//		FileProcessor.deleteFile("shell.sh");
//		String contents = "cd ../ && pwd\npwd";
//		FileProcessor.createFile("shell.sh", contents);
//		Command.execute("chmod +x ./shell.sh");
//		Command.execute("./shell.sh");
	}
}
