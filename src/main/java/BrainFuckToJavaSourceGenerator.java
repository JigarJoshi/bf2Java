import com.github.abrarsyed.jastyle.ASFormatter;
import com.github.abrarsyed.jastyle.FormatterHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jigar.joshi on 5/25/15.
 */
public class BrainFuckToJavaSourceGenerator {
	private static Map<Character, String> bfInstructionToJavaStatement = new HashMap<Character, String>() {{
		put('+', "arr[p]++;");
		put('-', "arr[p]--;");

		put('>', "p++;");
		put('<', "p--;");

		put('[', "while(arr[p] > 0) {");
		put(']', "}");

		put('.', "System.out.print((char)arr[p]);");
		put(',', "System.in.read(mem, p, 1);");

	}};

	private static final String JAVA_SOURCE_TEMPLATE = "public class Compiled {\n" +
			"\tpublic static void main(String[] args) {" +
			"byte[] arr = new byte[10000];" +
			"\nint p = 0;\n" +
			"%s}\n" +
			"}\n";

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Error: No argument passed\n\nUsage: \njava BrainFuckToJavaSourceGenerator bfSourceFile\n\nExample:\njava BrainFuckToJavaSourceGenerator /home/admin/sources/bf/hello.bf");
			System.exit(1);
		}
		File sourceFile = new File(args[0]);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile));
		String line = null;
		StringBuilder javaSource = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
			if (!line.isEmpty()) {
				for (char instruction : line.toCharArray()) {
					if (instruction == ' ') {
						continue;
					}
					javaSource.append(bfInstructionToJavaStatement.get(instruction));
				}
			}
		}
		System.out.println(FormatterHelper.format(new StringReader(String.format(JAVA_SOURCE_TEMPLATE, javaSource)), new ASFormatter()));
	}
}
