package chapter5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by xdcao on 2017/5/8.
 */
public class TwinsLock implements Lock {

    private final  Sync sync=new Sync(2);

    private static final class Sync extends AbstractQueuedSynchronizer{

        Sync(int count){
            if (count<0){
                throw new IllegalArgumentException("count must larger than 0");
            }
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int reduceCount) {
            for (;;){
                int current=getState();
                int newCount=current-reduceCount;
                if(newCount<0||compareAndSetState(current,newCount)){
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int returnCount) {
            for(;;){
                int current=getState();
                int newCount=current+returnCount;
                if (compareAndSetState(current,newCount)){
                    return true;
                }
            }
        }
    }


    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        int result=sync.tryAcquireShared(1);
        if (result>0&&result<3){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
