package common;

public interface ProgressDialog {

	/**
	 * Invoked when the first operation starts
	 */
	public void operationsStarted();

	/**
	 * Invoked when progress changes
	 * 
	 * @param operationsDone
	 *            Specifies how many operations have been completed
	 * @param totalOperationsToDo
	 *            Specifies the total number of operations to do.
	 */
	public void progressChanged(double operationsDone, double totalOperationsToDo);

	/**
	 * Invoked when all operations are finished.
	 */
	public void operationsFinished();
}
