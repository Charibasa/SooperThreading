import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class GuessGameTrial extends Thread
{
	private HashMap<Integer, GuessGamePlayerThread> threadHash = new HashMap<Integer, GuessGamePlayerThread>();
	private int playerCount;
	private int trialCount;
	private CountDownLatch startThreads = new CountDownLatch(1);
	private CountDownLatch endThreads;
	private CountDownLatch startTrials;
	private CountDownLatch endTrials;

	GuessGameTrial(int playerCount, int trialCount, CountDownLatch startTrials, CountDownLatch endTrials)
	{
		this.playerCount = playerCount;
		this.trialCount = trialCount;
		this.endThreads = new CountDownLatch(playerCount);
		this.startTrials = startTrials;
		this.endTrials = endTrials;
		
		for(int p = 0; p < playerCount; p++)
		{
			GuessGamePlayerThread ggtp = new GuessGamePlayerThread(p+1, trialCount, startThreads, endThreads);
			threadHash.put(p+1, ggtp);
		}
	}
	
	public void run()
	{
		try
		{
			startTrials.await();
			beginTrial();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		endTrials.countDown();
	}
	
	public void beginTrial() throws InterruptedException
	{
		for(int p = 0; p < playerCount; p++)
		{
			threadHash.get(p+1).start();
		}		
		
		startThreads.countDown();
		endThreads.await();
		determineWinner();
	}
	
	public void determineWinner()
	{
		GuessGamePlayerThread winnerThread = threadHash.get(1);

		for(int p = 0; p < playerCount; p++)
		{
			if(winnerThread.getCount() > threadHash.get(p+1).getCount())
			{
				winnerThread = threadHash.get(p+1);
			}
		}
		
		threadHash.get(winnerThread.getID()).setWin();
		
		for(int p = 0; p < playerCount; p++)
		{
			if(winnerThread.getCount() == threadHash.get(p+1).getCount())
			{
				threadHash.get(p+1).setWin();
			}
		}
	}
	
	public void saveResults(File file) throws IOException
	{
		DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file, true));
		
		dOut.writeUTF("|-| Trial #" + trialCount + " |-|");
		
		for(int p = 0; p < playerCount; p++)
		{
			dOut.writeUTF(threadHash.get(p+1).getEndResults());
		}
		
		dOut.writeUTF("");
		dOut.close();
	}
	
	public HashMap<Integer, GuessGamePlayerThread> getThreadHash()
	{
		return threadHash;
	}
}