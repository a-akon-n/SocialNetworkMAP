package ro.ubbcluj.map.repository;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.InMemoryRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected String filename;

    public FileRepository(Validator<E> validator, String filename) {
        super(validator);
        this.filename = filename;
    }

    public void writeData(String data){
        try{
            FileWriter writer = new FileWriter(filename);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> loadData(){
        ArrayList<String> dataList = new ArrayList<>();
        String[] data = new String[0];
        try{
            File file = new File(filename);
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                data = line.split(";");
                dataList.addAll(Arrays.asList(data));
            }
            reader.close();
        }catch(FileNotFoundException e){
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
        return dataList;
    }
}
