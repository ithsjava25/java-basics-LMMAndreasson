package com.example;

import com.example.api.ElpriserAPI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

import java.time.LocalDate;
import java.util.Date;

public class Main { //WIP


    public static void main(String[] args) {

        ElpriserAPI elpriserAPI = new ElpriserAPI();
        ElpriserAPI.Prisklass prisklass = ElpriserAPI.Prisklass.SE1;
        List<ElpriserAPI.Elpris> elpriser = getCurrentPrices(elpriserAPI, prisklass);

        System.out.println(elpriser);
    }

    private static List<ElpriserAPI.Elpris> getCurrentPrices(ElpriserAPI elpriserAPI, ElpriserAPI.Prisklass prisklass) {
        LocalDate idag = LocalDate.now();
        return getPrices(elpriserAPI, prisklass, idag);
    }

    private static List<ElpriserAPI.Elpris> getPrices(ElpriserAPI elpriserAPI, ElpriserAPI.Prisklass prisklass, LocalDate datum) {
        return elpriserAPI.getPriser(datum, prisklass);
    }




}
