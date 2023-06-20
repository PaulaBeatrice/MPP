package com.example.laborator_3;

import com.example.laborator_3.domain.Participant;
import com.example.laborator_3.domain.Referee;
import com.example.laborator_3.repository.ParticipantDBRepository;
import com.example.laborator_3.repository.RefereeDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props=new Properties();
        try {
            props.load(new FileReader("C:\\FACULTATE\\Anul 2\\Semestrul 2\\Medii de Proiectare si Programare\\Laboratoare\\mpp-proiect-java-PaulaBeatrice\\Laborator_3\\bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        System.out.println(props);




    }
}
