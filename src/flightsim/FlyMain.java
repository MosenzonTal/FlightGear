package flightsim;

import client.SimpleClient;
import language.interpreter.Lexer;
import language.interpreter.Parser;
import server.DataReaderServer;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FlyMain
{
    private final Environment env = new Environment();

    public FlyMain(){
        Parser parser = new Parser(env);
        env.setParser(parser);
        env.setServer(new DataReaderServer(env));
        env.setClient(new SimpleClient());
    }

    public static void main(String[] args)
    {
        FlyMain flyMain=new FlyMain();
        File file=new File("../PTM2 Project/scripts/script.txt");
        flyMain.runSimulator(file);
        flyMain.env.getParser().Resume();

    }


    public void runSimulator(String... lines){
        for (String line : lines) {
            InputStream is = new ByteArrayInputStream(line.getBytes());
            List<String> lexer = Lexer.Lexer(new InputStreamReader(is));
            this.env.getParser().threadparse(lexer);
        }

    }

    public void runSimulator(File fileScript){
        try {
            String[] lines = Files.lines(fileScript.toPath()).toArray(String[]::new);
            runSimulator(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setAutoPilot(boolean autoPilot){
        if (autoPilot){
            this.env.getParser().Resume();
        }else if (!autoPilot){
            this.env.getParser().stop();
        }
    }

}
