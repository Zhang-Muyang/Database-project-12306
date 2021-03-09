import java.io.*;

public class MyTrainTable {
    public static void main(String[] args) throws IOException {
        BufferedReader inFile = new BufferedReader(new FileReader("train.csv"));
        StringBuilder sb = new StringBuilder();

        PrintWriter pw = new PrintWriter("train2.csv");

        for (int i = 1; i <= 20278; i = i+2) {
            String[] str1 = inFile.readLine().split(",");
            String[] str2 = inFile.readLine().split(",");
            pw.write(str1[0]+","+str1[1]+","+str2[1]+","+str1[3]+","+str2[2]
                     +","+str2[4]+","+str2[5]+"\n");
        }



        pw.close();
    }
}
