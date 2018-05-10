package dmx.OpenLyght.Utils;

import dmx.OpenLyght.App;
import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;

public class FadeEngine implements Runnable {
	
	private int waitTime;
	private boolean priority;
	private Thread currentThread;
	private Channel referredChannel;
	private BasicChannel currentStep, nextStep;
	
	public FadeEngine(int speed, BasicChannel currentStep, BasicChannel nextStep, Channel ch, boolean priority) {
		this.currentStep = currentStep;
		this.nextStep = nextStep;
		this.priority = priority;
		referredChannel = ch;
		setSpeed(speed);
		
		currentThread = new Thread(this);
		currentThread.start();
	}
	
	public void setSpeed(int speed){
		int diff = Math.abs(nextStep.getValue() - currentStep.getValue());
		if(diff > 0) waitTime = speed / diff;
			else diff = 0;
	}
	
	public Thread getThread(){
		return currentThread;
	}
	
	@Override
	public void run() {
		boolean b = !Thread.interrupted();
		while(waitTime > 0 && currentStep.getValue() != nextStep.getValue() && b){
			if(currentStep.getValue() < nextStep.getValue()) currentStep.addValue((short) 1);
				else currentStep.addValue((short) -1);
			
			if(priority) referredChannel.setOriginalValue(currentStep.getValue());
			referredChannel.reportReload();

			App.wait(waitTime);
			b = !Thread.interrupted();
		}

		if(b){
			currentStep.setValue(nextStep.getValue());
			if(priority) referredChannel.setOriginalValue(currentStep.getValue());
			referredChannel.reportReload();
		}
	}

}
