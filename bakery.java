
public class Bakery  {
    public static boolean[] flag;
    public static int [] label;
    public static int sharecounter=0;
    public static int thread_num=8;

    public static void main(String[] args) {
 
      int repeat = 0;
      flag = new boolean[thread_num];
      label = new int[thread_num];
      
      for (int i = 0; i < thread_num; i++) { flag[i] = false; label[i] = 0; }

      for(int i=0;i<thread_num;i++){
          repeat=100; // the number of repeat time can be set here
          BakeryThread bt = new BakeryThread(i,repeat,sharecounter);
          Thread t = new Thread(bt);
          t.start();
      }
   }

public static class BakeryThread implements Runnable {
    private int id;
    private int repeat;
    private int count;
    public BakeryThread(int id, int repeat, int count) {
        this.id = id;
        this.repeat=repeat;
        this.count = count;
    }

    public void lock(int pid) {
        flag[pid] = true;
        int max=0;
        for (int n : label) { if (n > max) { max = n; } }
        label[pid] = max + 1;
        for (int k=0; k<label.length;k++){
            while ((k != pid) && (flag[k] && ((label[k] < label[pid]) ||  ((label[pid] == label[k]) && k < pid) ))){
            Thread.yield();
            }
        }
    }

    public void unlock(int pid) {
      flag[pid] = false;
    }

    public void run() {
      for (int i = 0; i < repeat; i++) {
          lock(id);
          sharecounter+=1;
          System.out.println("previous counter: "+count+" shared counter: "+sharecounter);
          unlock(id);
     }
    } 
  }
}

