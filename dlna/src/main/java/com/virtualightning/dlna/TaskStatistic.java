package com.virtualightning.dlna;

public class TaskStatistic {
    private final ICompletedListener listener;
    private final int goalCount;
    private final int completeFlag;
    private final int errorFlag;

    private int curCount;

    private boolean isFinished;

    public TaskStatistic(ICompletedListener listener, int goalCount, int completeFlag, int errorFlag) {
        this.listener = listener;
        this.goalCount = goalCount;
        this.completeFlag = completeFlag;
        this.errorFlag = errorFlag;

        this.isFinished = false;
        this.curCount = 0;
    }

    public void complete() {
        synchronized (this) {
            if(isFinished)
                return;

            curCount ++;
            if(curCount >= goalCount) {
                listener.onTaskCompleted(completeFlag);
                isFinished = true;
            }
        }
    }

    public void failed() {
        synchronized (this) {
            if(isFinished)
                return;

            listener.onTaskCompleted(errorFlag);
            isFinished = true;
        }
    }

    public void finish() {
        synchronized (this) {
            isFinished = true;
        }
    }

    public interface ICompletedListener {
        void onTaskCompleted(int flag);
    }
}
