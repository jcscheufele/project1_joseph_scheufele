package project1.jcscheufele.dbs2_d22.cs.wpi.java;
import java.util.Scanner;

/*
This class is for parsing the inputs, displaying the output and instantiating the bufferpool.

*/
public class App 
{
    public int poolSize;
    public boolean exit;
    public Scanner input;
    public BufferPool bP;

    public App(String[] args) {
        if (args.length > 0) {
            System.out.println("The command line arguments are:");
            poolSize = Integer.parseInt(args[0]);
            System.out.println(poolSize);
        } else {
            System.out.println("No command line arguments found.");
        }
        bP = new BufferPool(poolSize);
        exit = false;
        input = new Scanner(System.in);
    }

    public void run() {
        while (!exit) {
            System.out.println("The program is ready for the next command.");
            String command = input.nextLine();
            int[] actions = inputParser(command);
            String record = "";
            String print = "";

            switch (actions[0]) {
                case 1:
                    print = bP.GET(actions[1]);
                    break;
                case 2:
                    record = recordParser(command);
                    print = bP.SET(actions[1], record);
                    break;
                case 3:
                    print = bP.PIN(actions[1]);
                    break;
                case 4:
                    print = bP.UNPIN(actions[1]);
                    break;
                case 5:
                    exit = true;
                    print = "Exiting the program now.";
                    break;
            }
            System.out.println(print);
        }
    }

    public int[] inputParser(String command) {
        String[] words = command.split(" ", 3);
        int action;
        if      (words[0].toUpperCase().equals("GET"))   action = 1;
        else if (words[0].toUpperCase().equals("SET"))   action = 2;
        else if (words[0].toUpperCase().equals("PIN"))   action = 3; 
        else if (words[0].toUpperCase().equals("UNPIN")) action = 4;
        else                               action = 5; //exit
        
        int number = Integer.parseInt(words[1]);
        int ret[] = {action, number};
        return ret;
    }

    public String recordParser(String command) {
        String[] words = command.split(" ", 3);
        String ret = words[2];
        return ret;
    }

}
