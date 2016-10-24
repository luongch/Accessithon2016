package application;

import com.leapmotion.leap.*;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

class CustomerListener extends Listener {

	public Robot robot;

	public void onConnect(Controller controller) {
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		System.out.println("Connected");
	}

	public void onFrame(Controller controller) {
		try {
			robot = new Robot();
		} catch (Exception e) {
		}
		Frame frame = controller.frame();
		InteractionBox box = frame.interactionBox();
		for (Finger f : frame.fingers()) {
			if (f.type() == Finger.Type.TYPE_INDEX) {
				Vector fingerPos = f.stabilizedTipPosition();
				// Get Coordinates based on Interaction Box (Normalize)
				Vector boxFingerPos = box.normalizePoint(fingerPos);
				//ALLOWS BACKGROUND MOBILITY
				controller.setPolicyFlags(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);
				Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				robot.mouseMove((int) (screen.width * boxFingerPos.getX()),
						(int) (screen.height - boxFingerPos.getY() * screen.height));
			}
		}

		for (Gesture g : frame.gestures()) {
			if (g.type() == g.type().TYPE_CIRCLE) {
				CircleGesture c = new CircleGesture(g);
				if (c.pointable().direction().angleTo(c.normal()) <= Math.PI / 4) {
					robot.mouseWheel(1);
					try {
						Thread.sleep(50);
					} catch (Exception e) {
					}
				} else {
					robot.mouseWheel(-1);
					try {
						Thread.sleep(50);
					} catch (Exception e) {
					}
				}
			} else if (g.type() == g.type().TYPE_SCREEN_TAP || g.type() == g.type().TYPE_KEY_TAP) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			} else if (g.type() == g.type().TYPE_SWIPE && g.state() == g.state().STATE_START) {
				robot.keyPress(KeyEvent.VK_WINDOWS);
				robot.keyRelease(KeyEvent.VK_WINDOWS);
			}
		}
	}
}

public class LeapMouse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CustomerListener cl = new CustomerListener();
		Controller controller = new Controller();
		controller.addListener(cl);
//		try {
//			if (Desktop.isDesktopSupported()) {
//				Desktop.getDesktop().browse(new URI("http://www.youtube.com"));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (URISyntaxException use) {
//			use.printStackTrace();
//		}

		try {
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		controller.removeListener(cl);
	}

}
