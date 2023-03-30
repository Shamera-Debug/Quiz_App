package com.example.Quiz_App;

import android.app.Activity;
import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class StorageManager
{
    static String fileName = "dd.txt";
    FileOutputStream fOut;
    FileInputStream fIn;

    public ArrayList<Integer> readAllValuesFromFile(Activity context)
    {
        ArrayList<Integer> list = new ArrayList<>(0);
        StringBuilder stringBuffer = new StringBuilder();

        try
        {
            File file = new File(context.getFilesDir(), fileName);
            if(!file.exists())
            {
                fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                String temp = 0 + "$" + 0 + "$";
                fOut.write(temp.getBytes());
                fOut.close();
            }
            fIn = context.openFileInput(fileName);

            InputStreamReader inputStreamReader = new InputStreamReader(fIn, StandardCharsets.UTF_8);

            int read;
            while ((read = inputStreamReader.read()) != -1)
            {
                stringBuffer.append((char)read);
            }
            {
                int index = 0;
                for (int i = 0; i < stringBuffer.toString().toCharArray().length; i++)
                {
                    if (stringBuffer.toString().toCharArray()[i] == '$')
                    {
                        String firstValue = stringBuffer.substring(index, i);
                        list.add(Integer.parseInt(firstValue));
                        index = i + 1;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void writeResultToFile(Activity context, int correctAnswers, int attemptQuestions)
    {
        try
        {
            fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            String resultString = correctAnswers + "$" + attemptQuestions + "$";
            fOut.write(resultString.getBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                fOut.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


}
