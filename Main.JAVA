package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final ArrayList<String> strings = new ArrayList<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input;
        Map<String, String> cards = new HashMap<>();
        Map<String, Integer> hard = new HashMap<>();
        AtomicInteger m = new AtomicInteger(0);
        boolean argcheck=false;
        //TODO:Args
        if(args.length>0){
            for (int j = 0; j < args.length ; j+=2) {
                //TODO:ArgsImport
                if(args[j].equals("-import")){
                    String path = args[j+1];
                    try {
                        Scanner scanner1 = new Scanner(new File(path));
                        int i = 0;
                        while (scanner1.hasNext()) {
                            String[] temp = scanner1.nextLine().split(":");
                            if (!cards.containsKey(temp[0])) {
                                cards.put(temp[0], temp[1]);
                                hard.put(temp[0],Integer.parseInt(temp[2]));
                            }
                            else
                                cards.replace(temp[0], temp[1]);
                            i++;
                        }
                        if(i>0)
                            System.out.println(String.format("%d cards have been loaded.\n \n", i));
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found.");
                    }
                }//TODO:ArgsExport
            }
        }


        do {
            System.out.println(log("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):"));
            input = log(scanner.nextLine());
            //TODO:Exit
            if (input.compareTo("exit") == 0) {
                System.out.println(log("Bye bye!"));
                break;
            } //TODO:Add
            else if (input.compareTo("add") == 0) {
                System.out.println(log("The card:"));
                String term = log(scanner.nextLine());
                if (!cards.containsKey(term)) {
                    System.out.println(log("The definition of the card:"));
                    String def = log(scanner.nextLine());
                    if (!cards.containsValue(def)) {
                        cards.put(term, def);
                        hard.put(term, 0);
                        System.out.println(log(String.format("The pair (\"%s\":\"%s\") has been added.\n", term, def)));
                    } else
                        System.out.println(log(String.format("The definition \"%s\" already exists.\n", def)));
                } else
                    System.out.println(log(String.format("The card \"%s\" already exists.\n", term)));
            } //TODO:Remove
            else if (input.compareTo("remove") == 0) {
                System.out.println(log("The card:"));
                String in = log(scanner.nextLine());
                if (cards.containsKey(in)) {
                    cards.remove(in);
                    hard.remove(in);
                    System.out.println(log("The card has been removed.\n"));
                } else
                    System.out.println(log(String.format("Can't remove \"%s\": there is no such card.\n", in)));
            } //TODO:Import
            else if (input.compareTo("import") == 0) {
                System.out.println(log("File name:"));
                String path = log(scanner.nextLine());
                try {
                    Scanner scanner1 = new Scanner(new File(path));
                    int i = 0;
                    while (scanner1.hasNext()) {
                        String[] temp = scanner1.nextLine().split(":");
                        if (!cards.containsKey(temp[0])) {
                            cards.put(temp[0], temp[1]);
                            hard.put(temp[0],Integer.parseInt(temp[2]));
                        }
                        else
                            cards.replace(temp[0], temp[1]);
                        i++;
                    }

                    System.out.println(log(String.format("%d cards have been loaded.\n \n", i)));
                } catch (FileNotFoundException e) {
                    System.out.println(log("File not found."));
                }
            } //TODO:Export
            else if (input.compareTo("export") == 0) {
                System.out.println(log("File name:"));
                String path = log(scanner.nextLine());
                try {
                    PrintWriter printWriter = new PrintWriter(new File(path));
                    AtomicInteger i = new AtomicInteger();

                    cards.forEach((k, v) -> {
                        printWriter.println(k + ":" + v +":"+hard.get(k));
                        i.getAndIncrement();
                    });
                    System.out.println(log(String.format("%s cards have been saved.\n", i.toString())));
                    printWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } //TODO:Ask
            else if (input.compareTo("ask") == 0) {
                System.out.println(log("How many times to ask?"));
                int a = scanner.nextInt();
                log(String.valueOf(a));
                scanner.nextLine();
                Random random = new Random();
                Object[] s = cards.keySet().toArray();
                for (int i = 0; i < a; i++) {
                    String term = (String) s[random.nextInt(s.length)];
                    System.out.println(log(String.format("Print the definition of \"%s\":", term)));
                    String def = log(scanner.nextLine());
                    if (cards.get(term).equals(def))
                        System.out.println(log("Correct answer.\n"));
                    else {
                        hard.replace(term, hard.get(term) + 1);
                        if (cards.containsValue(def))
                            cards.forEach((k, v) -> {
                                if (v.equals(def))
                                    System.out.println(log(String.format("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".\n", cards.get(term), k)));
                            });
                        else
                            System.out.println(log(String.format("Wrong answer. The correct one is \"%s\".\n", cards.get(term))));
                    }
                }
            }
            //TODO:Log
            else if (input.compareTo("log") == 0) {
                System.out.println(log("File name:"));
                String path = log(scanner.nextLine());
                try {
                    PrintWriter printWriter = new PrintWriter(new File(path));
                    System.out.println(log("The log has been saved.\n"));
                    strings.forEach(printWriter::println);
                    printWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            } //TODO:hardest card
            else if (input.compareTo("hardest card") == 0) {
                ArrayList<String> strings=new ArrayList<>();
                AtomicInteger x = new AtomicInteger(0);
                hard.forEach((k, v) -> {
                    if (v > x.get()) {
                        x.set(v);
                    }
                });
                if(x.get()>0){
                    hard.forEach((k,v)->{
                        if(v==x.get()){
                            strings.add(k);
                        }
                    });
                    if(strings.size()>1){
                        String s="";
                        for (String v:strings)
                            s=s+String.format("\"%s\", ",v);
                        s=s.substring(0,s.length()-2);
                        System.out.println(log(String.format("The hardest cards are %s. You have %d errors answering them.\n",s,x.get())));
                    }
                    else {
                        System.out.println(log(String.format("The hardest card is \"%s\". You have %d errors answering it.\n",strings.get(0),x.get())));
                    }
                }else {
                    System.out.println(log("There are no cards with errors.\n"));
                }


            } //TODO:reset stats
            else if (input.compareTo("reset stats") == 0) {
                hard.forEach((k, v) -> hard.replace(k, 0));
                System.out.println(log("Card statistics has been reset.\n"));
            } else {
                System.out.println("");
            }
        } while (true);
        if(args.length>0){
            for (int j = 0; j < args.length ; j+=2) {
                //TODO:ArgsExport
                if(args[j].equals("-export")){
                    String path = args[j+1];
                    argcheck=true;
                    try {
                        PrintWriter printWriter = new PrintWriter(new File(path));
                        cards.forEach((k, v) -> {
                            printWriter.println(k + ":" + v +":"+hard.get(k));
                            m.getAndIncrement();
                        });
                        printWriter.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        if(argcheck)
            System.out.println(log(String.format("%s cards have been saved.\n", m.toString())));

    }

    static String log(String io) {
        strings.add(io);
        return io;
    }
}




