package com.morseari;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Model {

    enum Type_m {
        TEXT,
        IMAGES,
        UNKNOWN
    }

    enum Language_m {
        GREEK,
        ENGLISH,
        UNKNOWN
    }

    enum Action_m {
        ENCODE,
        DECODE
    }

    private HashMap<String, HashMap<String, File>> directories;
    String activeFormKey;
    String activeLanguageKey;

    private Type_m outputType;
    private Language_m language;
    private Action_m actionType;
    private String[] decodeTable = null;
    private String decodeSpitSequence = "";

    char startAlphabetChar;
    char endAlphabetChar;

    String rootDir = "./media/";

    private int findIndexInStringArray(Object[] elements, Object item){
        String element;

        for (int i=0; i < elements.length; i++) {
            element = elements[i].toString().toLowerCase();
            if (element.equals(item)) {
                return i;
            }
        }
        return -1;
    }

    public String filterGreekText(String text){
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++){
            switch (text.charAt(i)) {
                case 'ά' -> result.append("α");
                case 'έ' -> result.append("ε");
                case 'ί' -> result.append("ι");
                case 'ή' -> result.append("η");
                case 'ύ' -> result.append("υ");
                case 'ό' -> result.append("ο");
                case 'ώ' -> result.append("ω");
                default -> result.append(text.charAt(i));
            }
        }

        return result.toString();
    }

    public String convertTextString(String text){
        text = text.toLowerCase();

        if (actionType == Action_m.ENCODE){
            StringBuilder result = new StringBuilder();

            if (language == Language_m.GREEK){
                text = filterGreekText(text);
            }

            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);
                if (character == ' '){
                    result.append(decodeTable[0]).append(decodeSpitSequence);
                }
                else if (character == '\n'){
                    result.append(decodeSpitSequence).append('\n');
                }
                else if (character >= startAlphabetChar && character <= endAlphabetChar) {
                    result.append(decodeTable[text.charAt(i) - startAlphabetChar + 11]).append(decodeSpitSequence);
                }
                else if (character >= '0' && character <= '9'){
                    System.out.println((int)character);
                    result.append(decodeTable[text.charAt(i) - '0' + 1]).append(decodeSpitSequence);
                }
            }

            return result.toString();
        }
        else{
            String[] lettersToDecode = text.split(decodeSpitSequence);
            StringBuilder result = new StringBuilder();
            int index;

            for (String s : lettersToDecode) {
                index = findIndexInStringArray(decodeTable, s);
                if (index != -1) {
                    if (index == 0)
                        result.append(' ');
                    else if (index < 11) {
                        result.append((char) ('0' + index - 1));
                    } else {
                        if (startAlphabetChar + index - 11 == 'ς')
                            index++;
                        result.append((char) (startAlphabetChar + index - 11));
                    }
                }
            }

            return result.toString();
        }
    }

    public ArrayList<ImageIcon> convertImageString(String text) {
        ArrayList<ImageIcon> result = new ArrayList<>();
        text = text.toLowerCase();

        if (language == Language_m.GREEK) {
            text = filterGreekText(text);
        }

        try {
            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);
                if (character == ' ') {
                    if (decodeTable[0] != null)
                        result.add(new ImageIcon(ImageIO.read(new File(decodeTable[0]))
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                    else
                        result.add(new ImageIcon(ImageIO.read(new File("media/General Images/notFound.png"))
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                } else if (character == '\n') {
                    if (decodeTable[0] != null)
                        result.add(new ImageIcon(ImageIO.read(new File(decodeTable[0]))
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                    else
                        result.add(new ImageIcon(ImageIO.read(new File("media/General Images/notFound.png"))
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                } else if (character >= startAlphabetChar && character <= endAlphabetChar) {
                    if (decodeTable[text.charAt(i) - startAlphabetChar + 11] != null)
                        result.add(new ImageIcon(ImageIO.read(new File(decodeTable[text.charAt(i) - startAlphabetChar + 11]))
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                    else
                        result.add(new ImageIcon(ImageIO.read(new File("media/General Images/notFound.png"))
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                } else if (character >= '0' && character <= '9') {
                    if (decodeTable[text.charAt(i) - '0' + 1] != null)
                        result.add(new ImageIcon(ImageIO.read(new File(decodeTable[text.charAt(i) - '0' + 1]))
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                    else
                        result.add(new ImageIcon(ImageIO.read(new File("media/General Images/notFound.png"))
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }


    public String[] getAvailableOptions(){
        File[] tempDirectories = new File("media").listFiles(File::isDirectory);
        File[] languages;
        File infoFile;
        File alphabetFile;
        File firstImageFile;
        directories = new HashMap<>();

        for(int i = 0; i < Objects.requireNonNull(tempDirectories).length; i++){
            infoFile = new File(tempDirectories[i].getPath() + "/.info");
            if(infoFile.exists()) {
                directories.put(tempDirectories[i].getName(), new HashMap<>());
                languages = new File(tempDirectories[i].getPath()).listFiles(File::isDirectory);
                if (languages != null) {
                    for (File language : languages) {
                        alphabetFile = new File(language.getPath() + "/alphabet.txt");
                        firstImageFile = new File(language.getPath() + "/0.png");
                        if (alphabetFile.exists() || firstImageFile.exists()) {
                            directories.get(tempDirectories[i].getName()).put(language.getName(), language);
                        }
                    }
                }
            }
        }

        ArrayList<String> temp = new ArrayList<>();

        for (String key : directories.keySet()){
            if(directories.get(key).size() == 0){
                temp.add(key);
            }
        }

        for (String key : temp){
            directories.remove(key);
        }

        return directories.keySet().toArray(new String[0]);
    }

    public String[] getAvailableLanguages(String formKey){
        return directories.get(formKey).keySet().toArray(new String[0]);
    }

    public void loadDecodeSource(String formKey, String languageKey){
        File formDirectory = new File(rootDir + formKey);
        File languageDirectory = new File(formDirectory.getPath() + "/" + languageKey);

        activeFormKey = formKey;
        activeLanguageKey = languageKey;

        decodeTable = null;
        outputType = Type_m.UNKNOWN;
        language = Language_m.UNKNOWN;

        try {
            File file = new File(formDirectory.getPath() + "/.info");
            String data;
            Scanner myReader = new Scanner(file);
            if (myReader.hasNextLine()){
                data = myReader.nextLine().replaceAll("//.*", "").trim();
                if(data.equals("text")){
                    outputType = Type_m.TEXT;
                }
                else if(data.equals("image")){
                    outputType = Type_m.IMAGES;
                }
                else{
                    outputType = Type_m.UNKNOWN;
                }
            }
            if (outputType == Type_m.TEXT){
                if (myReader.hasNextLine()) {
                    decodeSpitSequence = myReader.nextLine().replaceAll(" //.*", "");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("There is no .info file on the directory media/" + formKey);
        }

        if(languageKey.equals("English")){
            language = Language_m.ENGLISH;
            startAlphabetChar = 'a';
            endAlphabetChar = 'z';
        }
        else if(languageKey.equals("Greek")){
            language = Language_m.GREEK;
            startAlphabetChar = 'α';
            endAlphabetChar = 'ω';
        }

        if (outputType == Type_m.TEXT){
            decodeTable = new String[(int)endAlphabetChar - (int)startAlphabetChar + 12];
            try {
                File file = new File(languageDirectory.getPath() + "/alphabet.txt");
                Scanner myReader = new Scanner(file);
                for (int i = 0; i < (int)endAlphabetChar - (int)startAlphabetChar + 12; i++){
                    if(myReader.hasNextLine()){
                        decodeTable[i] = myReader.nextLine().replaceAll(" //.*", "");
                    }
                    else{
                        decodeTable[i] = "~";
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred alphabet.txt file");
            }
        }
        else if (outputType == Type_m.IMAGES){
            decodeTable = new String[(int)endAlphabetChar - (int)startAlphabetChar + 12];
            int index = 0;

            File file = new File(languageDirectory.getPath() + "/0.png");
            decodeTable[index++] = file.getPath();

            for (int i = '0'; i <= '9'; i++){
                file = new File(languageDirectory.getPath() + "/char(" + i + ").png");
                if (file.exists())
                    decodeTable[index++] = file.getPath();
                else
                    decodeTable[index++] = null;
            }

            for (int i = startAlphabetChar; i <= endAlphabetChar; i++){
                file = new File(languageDirectory.getPath() + "/char(" + i + ").png");
                if (file.exists())
                    decodeTable[index++] = file.getPath();
                else{
                    decodeTable[index++] = null;
                    System.out.println(i);
                }
            }
        }
        else{
            System.out.println("Choose something else");
        }

    }

    public void changeAction(int action){
        if (action == 0)
            actionType = Action_m.ENCODE;
        else
            actionType = Action_m.DECODE;
    }

    public void changeLanguage(String languageKey){
        File languageDirectory = directories.get(activeFormKey).get(languageKey);

        activeLanguageKey = languageKey;

        decodeTable = null;
        language = Language_m.UNKNOWN;

        if(languageKey.equals("English")){
            language = Language_m.ENGLISH;
            startAlphabetChar = 'a';
            endAlphabetChar = 'z';
        }
        else if(languageKey.equals("Greek")){
            language = Language_m.GREEK;
            startAlphabetChar = 'α';
            endAlphabetChar = 'ω';
        }

        if (outputType == Type_m.TEXT){
            decodeTable = new String[(int)endAlphabetChar - (int)startAlphabetChar + 12];
            try {
                File file = new File(languageDirectory.getPath() + "/alphabet.txt");
                Scanner myReader = new Scanner(file);
                for (int i = 0; i < (int)endAlphabetChar - (int)startAlphabetChar + 12; i++){
                    if(myReader.hasNextLine()){
                        decodeTable[i] = myReader.nextLine().replaceAll(" //.*", "");
                    }
                    else{
                        decodeTable[i] = "~";
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred alphabet.txt file");
            }
        }
        else if (outputType == Type_m.IMAGES){
            decodeTable = new String[(int)endAlphabetChar - (int)startAlphabetChar + 12];
            int index = 0;

            File file = new File(languageDirectory.getPath() + "/0.png");
            decodeTable[index++] = file.getPath();

            for (int i = '0'; i <= '9'; i++){
                file = new File(languageDirectory.getPath() + "/char(" + i + ").png");
                if (file.exists())
                    decodeTable[index++] = file.getPath();
                else
                    decodeTable[index++] = null;
            }

            for (int i = startAlphabetChar; i <= endAlphabetChar; i++){
                file = new File(languageDirectory.getPath() + "/char(" + i + ").png");
                if (file.exists())
                    decodeTable[index++] = file.getPath();
                else{
                    decodeTable[index++] = null;
                    System.out.println((char)i + " " + i);
                }
            }
        }
        else{
            System.out.println("Choose something else");
        }
    }

    public Type_m getOutputType() {
        return outputType;
    }
}
