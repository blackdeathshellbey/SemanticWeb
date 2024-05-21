package fr.emse.master;
class Help implements Runnable {
    /**This dummy prevents the code from exit 0*/
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Delay for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}