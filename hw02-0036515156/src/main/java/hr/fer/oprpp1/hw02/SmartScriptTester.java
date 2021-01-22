package hr.fer.oprpp1.hw02;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.nodes.*;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class represents SmartScriptTester and it tests the functionality
 * @author leokiparje
 *
 */

public class SmartScriptTester {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Argumenti nisu ispravno zadani!");
            System.exit(1);
        }

        String docBody = null;

        try {
            docBody = new String(
                    Files.readAllBytes(Paths.get(args[0])),
                    StandardCharsets.UTF_8
            );
        } catch (IOException ex) {
            System.out.println("Putanja do datoteke nije ispravna!");
            System.exit(1);
        }

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Unable to parse document!");
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("If this line ever executes, you have failed " +
                    "this class!");
            System.exit(1);
        }
        DocumentNode document = parser.getDocumentNode();
        System.out.println(document.toString());
        
    }

}


