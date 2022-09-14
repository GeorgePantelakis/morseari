package com.morseari;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View extends JFrame {
    private JTextArea inputField;
    private JButton submitButton;
    private JTextArea outputField;
    private JComboBox<String> encodingBox;
    private JComboBox<String> languageBox;
    private JComboBox<String> actionBox;
    private JPanel outputPanel;
    private JScrollPane scrollOutputText;
    private JScrollPane scrollOutputImage;
    private JPanel outputImage;

    private int currentGridy = 0;

    GridBagConstraints gbc = new GridBagConstraints();

    public View(int width, int height){
        super();
        setTitle("MORSEari");
        setLayout(new GridBagLayout());
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        getContentPane().setBackground(Color.white);
    }

    private void addSpacePanel(int height){
        JPanel spacePanel = new JPanel();
        spacePanel.setBackground(Color.white);
        spacePanel.add(Box.createVerticalStrut(height));
        gbc.gridy = currentGridy++;
        add(spacePanel, gbc);
    }

    public void addDefaultPanel() {
        JPanel defaultPanel = new JPanel();
        defaultPanel.setBackground(Color.white);
        defaultPanel.setLayout(new FlowLayout());

        JLabel welcomeLabel = new JLabel("Welcome to MORSEari");
        welcomeLabel.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 35));
        defaultPanel.add(welcomeLabel);

        gbc.gridx = 0;
        gbc.gridy = currentGridy++;
        add(defaultPanel, gbc);

        addSpacePanel(15);
    }

    public void addSelectionPanel(String[] selectionOptions) {
        JPanel selectionPanel = new JPanel();
        selectionPanel.setBackground(Color.white);
        selectionPanel.setLayout(new GridLayout(0,1));

        JPanel encodingPanel = new JPanel();
        encodingPanel.setBackground(Color.white);
        encodingPanel.setLayout(new FlowLayout());

        JLabel EncodingLabel = new JLabel("Please select an encoding form: ");
        encodingPanel.add(EncodingLabel);

        encodingBox = new JComboBox<>(selectionOptions);
        encodingBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        encodingPanel.add(encodingBox);

        selectionPanel.add(encodingPanel);

        JPanel languagePanel = new JPanel();
        languagePanel.setBackground(Color.white);
        languagePanel.setLayout(new FlowLayout());

        JLabel languageLabel = new JLabel("Please select a language: ");
        languagePanel.add(languageLabel);

        languageBox = new JComboBox<>();
        languageBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        languagePanel.add(languageBox);

        selectionPanel.add(languagePanel);

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.white);
        actionPanel.setLayout(new FlowLayout());

        JLabel actionLabel = new JLabel("Encode or Decode the text: ");
        actionPanel.add(actionLabel);

        actionBox = new JComboBox<>(new String[]{"Encode"});
        actionBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionPanel.add(actionBox);

        selectionPanel.add(actionPanel);

        gbc.gridy = currentGridy++;
        add(selectionPanel, gbc);

        addSpacePanel(10);

    }

    public void addTextPanels() {
        inputField = new JTextArea ( 3, 30 );
        JScrollPane scrollInput = new JScrollPane ( inputField );
        scrollInput.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.white);
        inputPanel.setBorder(new TitledBorder(new EtchedBorder(), "Input your text here:"));
        inputPanel.add(scrollInput);
        gbc.gridy = currentGridy++;
        add(inputPanel, gbc);

        addSpacePanel(2);

        submitButton = new JButton("Release the Magic!");
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JPanel submitPanel = new JPanel();
        submitPanel.setBackground(Color.white);
        submitPanel.add(submitButton);
        gbc.gridy = currentGridy++;
        add(submitPanel, gbc);

        addSpacePanel(2);

        outputField = new JTextArea ( 3, 30 );
        outputField.setEditable(false);
        outputField.setText("Waiting for input...");
        scrollOutputText = new JScrollPane ( outputField );
        scrollOutputText.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        scrollOutputText.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        outputImage = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outputImage.setPreferredSize(new Dimension(380, 80));
        outputImage.setBackground(Color.white);

        scrollOutputImage = new JScrollPane();
        scrollOutputImage.setPreferredSize(new Dimension(400, 80));
        scrollOutputImage.setViewportView(outputImage);

        outputPanel = new JPanel();
        outputPanel.setBackground(Color.white);
        outputPanel.setBorder(new TitledBorder(new EtchedBorder(), "Output:"));
        gbc.gridy = currentGridy++;
        add(outputPanel, gbc);
    }

    public void addSubmitButtonListener(ActionListener action){
        if (action != null) {
            submitButton.addActionListener(action);
        }
    }

    public void addToImageOutput(ArrayList<ImageIcon> output){
        outputImage.removeAll();

        for (ImageIcon imageIcon : output) {
            JLabel image = new JLabel(imageIcon);
            outputImage.add(image);
        }
        outputImage.setPreferredSize(new Dimension(380, ((output.size() / 15) + 1) * 27));
        outputImage.revalidate();
    }

    public String getInputText(){
        return inputField.getText();
    }

    public void setInputText(String s){ inputField.setText(s); }

    public String getOutputFieldText(){ return outputField.getText(); }

    public void setOutputFieldText(String text){
        outputField.setText(text);
    }

    public JComboBox<String> getEncodingBox() { return encodingBox; }

    public JComboBox<String> getActionBox(){ return actionBox; }

    public JComboBox<String> getLanguageBox(){ return languageBox; }

    public void changeActionBoxOptions(String[] options){
        DefaultComboBoxModel<String> optionModel = new DefaultComboBoxModel<>(options);
        actionBox.setModel(optionModel);
    }

    public void changeLanguageBoxOptions(String[] options){
        DefaultComboBoxModel<String> optionModel = new DefaultComboBoxModel<>(options);
        languageBox.setModel(optionModel);
    }

    public void changeToTextOutput(){
        outputPanel.removeAll();
        outputPanel.add(scrollOutputText);
        repaint();
    }

    public void changeToImageOutput(){
        outputImage.removeAll();
        outputPanel.removeAll();
        outputPanel.add(scrollOutputImage);
        repaint();
    }
}
