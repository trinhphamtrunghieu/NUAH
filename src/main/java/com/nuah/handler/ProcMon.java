package com.nuah.handler;

public class ProcMon implements Runnable {

    private final Process _proc;
    private volatile boolean _complete;
  
    public boolean isComplete() { return _complete; }
  
    ProcMon(Process proc) {
      this._proc = proc;
      this._complete = false;
    }

    public void run() {
      try {
        _proc.waitFor();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      _complete = true;
    }
  
    public static ProcMon create(Process proc) {
      ProcMon procMon = new ProcMon(proc);
      Thread t = new Thread(procMon);
      t.start();
      return procMon;
    }
  }