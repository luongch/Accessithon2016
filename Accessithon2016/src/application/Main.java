package application;

import com.leapmotion.leap.Controller;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.IOException;
import java.net.URL;


class MyRunnable implements Runnable {

	String [] args;

    public MyRunnable(String[] args) {
    	this.args = args;
    }

    public void run() {
    	HelloWorld voiceController = new HelloWorld();
        voiceController.main(args);
    }
}

public class Main {
	public static void main(final String[] args) {
	//	 Code for Voice Activatiom
//		Thread one = new Thread() {
//			public void run() {
//				HelloWorld voiceController = new HelloWorld();
//				voiceController.main(args);
//			}
//		};
//		try{
//			one.sleep(250);
//		} catch (InterruptedException IE) { IE.printStackTrace(); }
//		
		MyRunnable runnable = new MyRunnable(args);
		Thread th = new Thread(runnable);
		th.start();

	//Code for Leap Mouse
		CustomerListener cl = new CustomerListener();
		Controller controller = new Controller();
		controller.addListener(cl);
		try {
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		controller.removeListener(cl);
	}
}