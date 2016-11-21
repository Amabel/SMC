package entry;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteShellCommand {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecuteShellCommand obj = new ExecuteShellCommand();
		
		String cmd = "verify inc.c";
		obj.executeCommand(cmd);

	}
	
	private void executeCommand(String command) {
		
		StringBuffer output = new StringBuffer();
		
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		return output.toString();
		System.out.println(output);
	}
}
