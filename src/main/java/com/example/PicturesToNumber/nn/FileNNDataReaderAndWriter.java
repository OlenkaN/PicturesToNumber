package com.example.PicturesToNumber.nn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

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
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String nnData = gson.toJson(nn);

                file.write(nnData);
                file.flush();
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
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                JsonReader jsonReader = new JsonReader(new FileReader(name));
                nn = gson.fromJson(jsonReader, NN.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return nn;
        }


}
