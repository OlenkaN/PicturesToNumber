package com.example.PicturesToNumber.nn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Need to save weights of nn
 */
public class FileNNDataReaderAndWriter {


    /**
     * Save nn data to file
     * @param nn
     * @param fileName
     */
        public static void writeToFile(NN nn, String fileName){
            String name = fileName;

            if (fileName == null) {
                name = "nn_data";
            }

            try {
                FileWriter file = new FileWriter(name + ".json");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(file,nn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    /**
     * Method upload data into nn
     * @param fileName
     * @return nn with initialized  fields
     */
    public static NN readFromFile(String fileName) {
            NN nn = null;
            String name = fileName;

            if (fileName == null) {
                name = "nn_data.json";
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                nn=objectMapper.readValue(new File(name),NN.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return nn;
        }


}
