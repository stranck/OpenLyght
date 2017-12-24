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
	//private static int i = 0;
	
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
		//System.out.println(speed + " " + diff);
		if(diff > 0) waitTime = speed / diff;
			else diff = 0;
	}
	
	public Thread getThread(){
		return currentThread;
	}
	
	@Override
	public void run() {
		boolean b = !Thread.interrupted();
		//int n = i++;
		//System.out.println("ok1 " + n + " " + currentStep.getValue()
		//	+ " " + nextStep.getValue() + " " + waitTime + " " + !Thread.interrupted());
		while(waitTime > 0 && currentStep.getValue() != nextStep.getValue() && b){
			if(currentStep.getValue() < nextStep.getValue()) currentStep.addValue((short) 1);
				else currentStep.addValue((short) -1);
			//System.out.println("ok3 " + waitTime);
			if(priority) referredChannel.setOriginalValue(currentStep.getValue());
			referredChannel.reportReload();
			//System.out.println("ok4 " + n);
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
