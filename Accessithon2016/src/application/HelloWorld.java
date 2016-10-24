package application;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class HelloWorld {

	public String getExeName(String resultType) {
		String result = "";
		switch (resultType.toLowerCase()) {
		case "computer":
			result = "explorer";
			break;
		case "internet":
			result = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
			break;
		case "word":
			result = "C:\\Program Files (x86)\\Microsoft Office\\root\\Office16\\WINWORD.EXE";
			break;
		case "excel":
			result = "C:\\Program Files (x86)\\Microsoft Office\\root\\Office16\\EXCEL.EXE";
			break;
		case "popcorn":
			result = "C:\\Program Files (x86)\\Popcorn Time\\PopcornTimeDesktop.exe";
			break;
		default:
			result = "cmd";
			break;
		}
		return result;
	}
	
	public void run(){
		
	}

	/**
	 * Main method for running the HelloWorld demo.
	 */
	public static void main(String[] args) {
		try {
			URL url;
			if (args.length > 0) {
				url = new File(args[0]).toURI().toURL();
			} else {
				url = HelloWorld.class.getResource("helloworld.config.xml");
			}

			System.out.println("Loading...");

			ConfigurationManager cm = new ConfigurationManager(url);

			Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
			Microphone microphone = (Microphone) cm.lookup("microphone");

			/* allocate the resource necessary for the recognizer */
			recognizer.allocate();

			/* the microphone will keep recording until the program exits */
			if (microphone.startRecording()) {

				System.out.println("Say: (Computer) " + "( Internet | Computer | Music | Word | Excel | Popcorn )");

				while (true) {
					System.out.println("Start speaking. Press Ctrl-C to quit.\n");

					/*
					 * This method will return when the end of speech is
					 * reached. Note that the endpointer will determine the end
					 * of speech.
					 */
					Result result = recognizer.recognize();
					
					if (result != null) {
						String resultText = result.getBestFinalResultNoFiller();
//						if (resultText == "" || resultText == "cmd") {
//							System.out.println("I can't hear what you said.\n");
//							break;
//						}
						System.out.println("You said: " + resultText + "\n");

						String[] bob = resultText.split(" ");
						for(String s : bob){
							System.out.println(s);
						}
						HelloWorld hw = new HelloWorld();
						String test = hw.getExeName(bob[bob.length - 1]);
//						System.out.println("TEST IS : " + test);
						Runtime rt = Runtime.getRuntime();
						Process pr = rt.exec(test);
						if(pr.isAlive()){
							break;
						} else {
							continue;
						}
					} else {
						System.out.println("I can't hear what you said.\n");
					}
				}
			} else {
				System.out.println("Cannot start microphone.");
				recognizer.deallocate();
			}
		} catch (IOException e) {
			System.err.println("Problem when loading HelloWorld: " + e);
			e.printStackTrace();
		} catch (PropertyException e) {
			System.err.println("Problem configuring HelloWorld: " + e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.err.println("Problem creating HelloWorld: " + e);
			e.printStackTrace();
		}
	}
}
