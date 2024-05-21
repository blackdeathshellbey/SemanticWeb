package fr.emse.master;

import java.io.File;
/**Crates server and executes it from terminal*/
class FusekiServerThread extends Thread {
    @Override
    public void run() {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "fuseki-server.jar", "--port", "6969", "--update", "--mem", "/Root");
        pb.directory(new File(Main.fuseki));

        System.out.printf("Fuseki Server Created.\n");
        try {
            Process process = pb.start();
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
