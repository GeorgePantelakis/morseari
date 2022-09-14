//        _____ ______   ________  ________  ________  _______   ________  ________  ___
//        |\   _ \  _   \|\   __  \|\   __  \|\   ____\|\  ___ \ |\   __  \|\   __  \|\  \
//        \ \  \\\__\ \  \ \  \|\  \ \  \|\  \ \  \___|\ \   __/|\ \  \|\  \ \  \|\  \ \  \
//        \ \  \\|__| \  \ \  \\\  \ \   _  _\ \_____  \ \  \_|/_\ \   __  \ \   _  _\ \  \
//        \ \  \    \ \  \ \  \\\  \ \  \\  \\|____|\  \ \  \_|\ \ \  \ \  \ \  \\  \\ \  \
//        \ \__\    \ \__\ \_______\ \__\\ _\ ____\_\  \ \_______\ \__\ \__\ \__\\ _\\ \__\
//        \|__|     \|__|\|_______|\|__|\|__|\_________\|_______|\|__|\|__|\|__|\|__|\|__|                                        \|_________|

package com.morseari;

import java.util.Objects;

public class Main {

    static int windowWidth = 700;
    static int windowHeight = 600;

    public static void main(String[] args) {
        View view = new View(windowWidth, windowHeight);
        Model model = new Model();

        view.addDefaultPanel();
        view.addSelectionPanel(model.getAvailableOptions());
        view.addTextPanels();

        view.getEncodingBox().addActionListener(e -> {
            view.changeLanguageBoxOptions(model.getAvailableLanguages(
                    Objects.requireNonNull(view.getEncodingBox().getSelectedItem()).toString()));
            model.loadDecodeSource(Objects.requireNonNull(view.getEncodingBox().getSelectedItem()).toString(),
                    Objects.requireNonNull(view.getLanguageBox().getSelectedItem()).toString());
            if (model.getOutputType() == Model.Type_m.TEXT){
                view.changeActionBoxOptions(new String[]{"Encode", "Decode"});
                view.changeToTextOutput();
                view.setOutputFieldText("");
            }
            else if (model.getOutputType() == Model.Type_m.IMAGES){
                view.changeActionBoxOptions(new String[]{"Encode"});
                view.changeToImageOutput();
            }
        });

        view.getActionBox().addActionListener(e -> {
            model.changeAction(view.getActionBox().getSelectedIndex());
            if (!view.getOutputFieldText().equalsIgnoreCase("Waiting for input...")){
                view.setInputText(view.getOutputFieldText());
                view.setOutputFieldText("");
            }
        });

        view.getLanguageBox().addActionListener(e ->
                model.changeLanguage(Objects.requireNonNull(view.getLanguageBox().getSelectedItem()).toString()));

        view.addSubmitButtonListener(e -> {
            if (model.getOutputType() == Model.Type_m.TEXT)
                view.setOutputFieldText(model.convertTextString(view.getInputText()));
            else if (model.getOutputType() == Model.Type_m.IMAGES)
                view.addToImageOutput(model.convertImageString(view.getInputText()));

        });

        //initialize boxes
        view.changeLanguageBoxOptions(model.getAvailableLanguages(
                Objects.requireNonNull(view.getEncodingBox().getSelectedItem()).toString()));
        model.loadDecodeSource(Objects.requireNonNull(view.getEncodingBox().getSelectedItem()).toString(),
                Objects.requireNonNull(view.getLanguageBox().getSelectedItem()).toString());
        model.changeAction(view.getActionBox().getSelectedIndex());
        if (model.getOutputType() == Model.Type_m.TEXT){
            view.changeActionBoxOptions(new String[]{"Encode", "Decode"});
            view.changeToTextOutput();
            view.setOutputFieldText("Waiting for input...");
        }
        else {
            view.changeActionBoxOptions(new String[]{"Encode"});
            view.changeToImageOutput();
        }

        view.repaint();
    }
}
