import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class GuessGamePlayerThread extends Thread
{
	private Random r = new Random();
	private int counter = 0;
	private int playerID;
	private int trialCount;
	private boolean winner = false;
	private CountDownLatch start;
	private CountDownLatch end;
	
	GuessGamePlayerThread(int playerID, int trialCount, CountDownLatch start, CountDownLatch end)
	{
		this.trialCount = trialCount;
		this.playerID = playerID;
		this.start = start;
		this.end = end;
	}
	
	public void run()
	{
		int guess;
		
		try
		{
			start.await();
			do
			{
				guess = r.nextInt(10);
				System.out.println("Trial " + trialCount + ": Player " + playerID + " guesses " + guess);
				counter++;
				sleep(1);
			}
			while(guess!=GuessGameMain.goalGuess);
			
			System.out.println("Trial " + trialCount + ": Player " + playerID + " guessed correctly!");
			end.countDown();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public int getID()
	{
		return playerID;
	}
	
	public int getCount()
	{
		return counter;
	}
	
	public void setWin()
	{
		winner = true;
	}
	
	public boolean getWin()
	{
		return winner;
	}
	public String getEndResults()
	{
		String endResults = "Player " + playerID + " - Guesses: " + counter;
		
		if(winner)
		{
			endResults += " - WINNER!";
		}
		
		return endResults;
	}
}