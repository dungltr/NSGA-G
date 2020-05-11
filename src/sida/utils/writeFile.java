package sida.utils;



import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class writeFile {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
	public static void writeOut (String link, StringBuilder output){
    	String Text = output.toString();
    	Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(link), "utf-8"));
            writer.write(Text);
        }
        catch (IOException ex) {
            // report
        }
        finally {
            try {writer.close();}
            catch (Exception ex) {/*ignore*/}
        }
    }
	public static void writeString (String link, String content){
    	String Text = content;
    	Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(link), "utf-8"));
            writer.write(Text);
        }
        catch (IOException ex) {
            // report
        }
        finally {
            try {writer.close();}
            catch (Exception ex) {/*ignore*/}
        }
    }

    public static void createfile(String OperatorFolder,String filename, String content){

        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(OperatorFolder+"/"+filename), "utf-8"));
            writer.write(content);
        }
        catch (IOException ex) {
            // report
        }
        finally {
            try {writer.close();}
            catch (Exception ex) {/*ignore*/}
        }
    }


    public static void Writematrix2CSV(double[][] a, String fileName, String FILE_HEADER){
        int m = a.length;
        int n = a[0].length;
        int i = 0;
        String tmp = "";
        double[] b = null;
        List arrays = new ArrayList();
        for (i = 0; m >i; i++)
            arrays.add(a[i]);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (Iterator it = arrays.iterator(); it.hasNext();) {
                double[] array = (double[]) it.next();
                tmp = Arrays.toString(array);
                tmp = tmp.replace("[","");
                tmp = tmp.replace("]","");
                tmp = tmp.replace(" ","");
                fileWriter.append(tmp);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
}
