

class Main {
    public static void main(String[] args) {

        Integer timeout = 5000; // 5s timeout
        TaskThatMighHang taskThatMighHang = new TaskThatMighHang();

        System.out.println(System.currentTimeMillis() + ": Starting delegator thread");
        Thread delegator = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println(System.currentTimeMillis() + ": joining taskThatMightHang thread");
                    taskThatMighHang.start();
                    taskThatMighHang.join(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (taskThatMighHang.isAlive()) {
                        taskThatMighHang.interrupt();
                        // maybe do more?
                       // taskThatMighHang.getThreadGroup().destroy();
                    }
                }
                System.out.println(System.currentTimeMillis() + ": done with taskThatMightHang thread");
            }
        };

        // this will never take more than 5s
        delegator.run();
        System.out.println(System.currentTimeMillis() + ": Finished delegator thread");
    }
}

class TaskThatMighHang extends Thread {

    @Override
    public void run() {
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}