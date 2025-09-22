package com.example;

import com.example.api.ElpriserAPI;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;

import java.time.LocalDate;

public class Main {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input;
        String zoneCode;

        if (args.length==0){
            System.out.println("Zone code is required");
            zoneCode = scanner.nextLine();
        }
        else {
            zoneCode=args[0];
        }

        System.out.println(zoneCode);//bugtesting

        while (!isInEnum(zoneCode)) {
            System.out.println("Zone code is required");
            zoneCode = scanner.nextLine();
        }


        ElpriserAPI elpriserAPI = new ElpriserAPI();
        ElpriserAPI.Prisklass prisklass = ElpriserAPI.Prisklass.valueOf(zoneCode);
        List<ElpriserAPI.Elpris> elpriser = getCurrentPrices(elpriserAPI, prisklass);

        System.out.println("lägsta pris är " + formatOre(findCheapest(elpriser).sekPerKWh())+ " Öre");
        System.out.println("högsta pris är " + formatOre(findExpensive(elpriser).sekPerKWh())+ " Öre");
        System.out.println("medelpris är " + formatOre(findMean(elpriser)) + " Öre");
    }

    private static List<ElpriserAPI.Elpris> getCurrentPrices(ElpriserAPI elpriserAPI, ElpriserAPI.Prisklass prisklass) {
        LocalDate idag = LocalDate.now();
        return getPrices(elpriserAPI, prisklass, idag);
    }

    private static List<ElpriserAPI.Elpris> getPrices(ElpriserAPI elpriserAPI, ElpriserAPI.Prisklass prisklass, LocalDate datum) {
        return elpriserAPI.getPriser(datum, prisklass);
    }


    private static ElpriserAPI.Elpris findCheapest(List<ElpriserAPI.Elpris> elpriser){
        if (elpriser.isEmpty() ){
            throw new RuntimeException("No prices found");
        }
        ElpriserAPI.Elpris cheapest = elpriser.getFirst();
        for (ElpriserAPI.Elpris elpris : elpriser) {
            if (elpris.sekPerKWh() < cheapest.sekPerKWh()) {
                cheapest = elpris;
            }
        }
        return cheapest;
    }

    private static ElpriserAPI.Elpris findExpensive(List<ElpriserAPI.Elpris> elpriser){
        if (elpriser.isEmpty() ){
            throw new RuntimeException("No prices found");
        }
        ElpriserAPI.Elpris expensive = elpriser.getFirst();
        for (ElpriserAPI.Elpris elpris : elpriser) {
            if (elpris.sekPerKWh() > expensive.sekPerKWh()) {
                expensive = elpris;
            }
        }
        return expensive;
    }
    private static boolean isInEnum(String value) {
        for (Enum e : ElpriserAPI.Prisklass.values()){
            if (e.name().equals(value)){return true;}
        }
        return false;
    }

    private static double findMean(List<ElpriserAPI.Elpris> elpriser){
        if (elpriser.isEmpty() ){
            throw new RuntimeException("No prices found");
        }
        double sektotal =0;
        double eurtotal =0;

        for (ElpriserAPI.Elpris elpris : elpriser) {
            sektotal += elpris.sekPerKWh();
            eurtotal += elpris.eurPerKWh();
        }

        double sekMean = sektotal/elpriser.size();
        double eurMean = eurtotal/elpriser.size();

        return sekMean;
    }

    private static void printHelp(){
        System.out.println(
                """
                        Welcome to the Swedish electricity optimizer CLI
                        Supported arguments are as follows
                        --zone --date  -- charging -- sorted --help
                        Please select your zone:
                        SE1 SE2 SE3 SE4
                        """
        );
    }

    private static String formatOre(double sekPerKWh) {//This is copied from MainTest. I am lazy.
        double ore = sekPerKWh * 100.0;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("sv", "SE"));
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(ore);
    }

    /*

        Functionality TBD

        Fetch data for today, tomorrow (if available, else fetch today and give a warning)
        Find cheapest and most expensive hours, print them
        Calculate and display mean price for current 24-hour period
        Sliding window algorithm for finding the best charging times 2-4-8 hours
        Different price zones - decided with CLI or as prompt



    *   Supported mainline arguments
    *  --zone  --date -- charging -- sorted --help
    *
    *
    *   java -cp target/classes com.example.Main --zone SE3 --date 2025-09-04
        java -cp target/classes com.example.Main --zone SE1 --charging 4h
        java -cp target/classes com.example.Main --zone SE2 --date 2025-09-04 --sorted
        java -cp target/classes com.example.Main --help
    *
    *
    *
    * */




}
