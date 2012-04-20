package util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * Encapsulates logic for code that you want run on a separate thread,
 * and then another bit of code that you want to run in the Event
 * Dispatch Thread when the first thread finishes. Allows for synchronous
 * and time-consuming actions to happen without consuming the UI thread.
 * @author krm
 *
 */
public class PostAction<T> {
	private Callable<T> threaded;
	private Runnable postToEDT;
	
	public PostAction(Callable<T> threaded, Runnable postToEDT){
		this.threaded = threaded;
		this.postToEDT = postToEDT;
	}
	
	public T start(){
		
		SwingWorker<T, Void> worker = new SwingWorker<T, Void>(){


			protected T doInBackground() throws Exception {
				
				T result = threaded.call();
				
				SwingUtilities.invokeAndWait(postToEDT);
				return result;
			}
			
		};
		worker.execute();
		try {
			return worker.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
