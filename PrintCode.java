//Author: David McKay
//Date: 4/1/2015

//Mission: My goal is to help people save time and it can be frustrating trying
//  to print source code out. Text Editors or print features never seem to get
//  things right, but Text Editors can print out pictures effectively.
//  So, instead of wasting time and cropping let the code do it for you, and
//  stop wasting time with changing Fonts and Sizes.

//Description: Just wanted to create a faster way of printing code on paper,
//  without wasting time cropping and formatting. This is mainly practical
//  in the Academic world, when the source code is requested by the professor.
//  Other times is when people want to analyize their code without staring at a
//  screen.

//Improvements: (Not necessary, but can be useful for future development)
//  * Use JFileSelector and have it set up so the person can select the location
//    where they want to place the pictures.
//  * Find a Library which can create PDF's and insert pictures into it, so
//    the user doesn't have to use Word.

//How to use the Application:
// * Unzip the File.
// * Run the Jar or compile and run the Java File.
// * Select a name for the code you want to print. (Note: you can use folder
//   directories, but if it doesn't exist it won't print.)
// * Copy and Paste the desired source code into the JTextArea.
// * Hit Convert Text into Pictures.
// * The pictures will be populated in the Active Directory where the code was
//   ran.
// * If, using the Jar executable it will be inside that file directory.
// * Otherwise, it will be in the IDE Editor where the project was ran from.
//   (Example: Netbeans it will be in the Documents->Netbeans->Project and the
//      photos will be there.)
// * Open up word, change the layout if desired, go to insert and insert photos.
//   Select all the photos you want to print and Word will auto-populate the
//   Photos.
// * The pictures are ready to print, or if you want it as a PDF.

package testing.java;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PrintCode extends JPanel {
    
    //Instance Variables:
    private JLabel _fileNameLabel;
    private JTextField _fileNameTextField;
    
    private JLabel _textAreaLabel;
    private JScrollPane _scrollTextArea;
    private JTextArea _textArea;
    
    private JButton _processButton;
    
    //Constructor: Just creating a simple way to create photos, so it can be
    //  printed out easier and faster.
    public PrintCode(){
        _fileNameLabel = new JLabel("Enter File Name:");
        _fileNameTextField = new JTextField();
        
        _textAreaLabel = new JLabel("Enter the Text Below:");
        _textArea = new JTextArea();
        _scrollTextArea = new JScrollPane(_textArea);
        
        _textArea.setFont(new Font("Lucida Console", Font.PLAIN, 12));
        
        _processButton = new JButton("Convert Text into Pictures");
        
        ActionListener processTextButton;
        
        //The method to process the JTextArea into Images:
        processTextButton = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(e.getSource() instanceof JButton){
                    //Have to reset the Scroll Position, otherwise it won't
                    //process corectly.
                    _scrollTextArea.getVerticalScrollBar().setValue(0);
                    _scrollTextArea.getHorizontalScrollBar().setValue(0);
                    
                    //Used for making a JComponent into a Picture:
                    BufferedImage img = getImage( _textArea );
                    BufferedImage tempImg = null;
                    int height = img.getHeight();
                    int width = img.getWidth();
                    
                    int totalHeight = height;
                    int totalWidth = width;
                    
                    //Just have to play around with textCount to determine
                    //how many lines it can take per page.
                    int textCount = 1172;
                    
                    //Allowing the user to have 100 columns or more, but they
                    //should try to get at least 80 columns, but if it goes over
                    //slightly it won't punish them by cutting off the text.
                    //width = (width<700? width : 700);
                    height = (height<textCount? height : textCount);
                    
                    System.out.println("Total Height:" + totalHeight);
                    
                    try {
                        boolean done = false;
                        int counter = 1;
                        int rev = 0;
                        
                        ImageIO.write(img,
                                    "png", 
                                    new File( _fileNameTextField.getText() + ".png"));
                        
                        while(!done){
                            tempImg = img.getSubimage(0, rev, width, (height-rev));
                            ImageIO.write(tempImg, 
                                    "png", 
                                    new File( _fileNameTextField.getText() + 
                                            (counter++) + ".png"));
                            
                            System.out.println("Rev: " + rev);
                            System.out.println("Height: " + height);
                            System.out.println();
                            
                            //Previous:
                            //Current:
                            rev += textCount;
                            height += textCount;
                            
                            //Small End of the Document:
                            if(height >= totalHeight){
                                System.out.println("Inner Loop:");
                                height = totalHeight - rev;
                                
                                tempImg = img.getSubimage(0, rev, width, height);
                                ImageIO.write(tempImg, 
                                    "png", 
                                    new File( _fileNameTextField.getText() + 
                                            (counter++) + ".png"));
                                
                                done = true;
                            }
                       }

                    } catch (IOException ex) {

                    }
                }
            }
        };

        _processButton.addActionListener(processTextButton);
        
        //Setting the Layout:
        setLayout(new BorderLayout());
        
        //Creating and adding the Panels to the main Panel:
        JPanel titlePanel;
        JPanel textPanel;
        
        titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        
        titlePanel.add(_fileNameLabel, BorderLayout.NORTH);
        titlePanel.add(_fileNameTextField, BorderLayout.CENTER);
        
        textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        
        textPanel.add(_textAreaLabel, BorderLayout.NORTH);
        textPanel.add(_scrollTextArea, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        add(_processButton, BorderLayout.SOUTH);
    }
    
    //Just a way to Convert JComponents into Pictures:
    private static BufferedImage getImage(JComponent c) {
            Rectangle region = c.getBounds();
            BufferedImage image = new BufferedImage(region.width, region.height, 
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.translate(-region.x, -region.y);
            g2d.setColor(c.getBackground() );
            g2d.fillRect(region.x, region.y, region.width, region.height);
            c.paint(g2d);
            g2d.dispose();
       return image;
   }
    
    //Just a generic start up for the Swing Application:
    public static void main(String[] args){
        JFrame GUI = new JFrame("Print Code");
        
        //Setting up the Size and adding the component:
        GUI.setSize(600,600);
        GUI.add(new PrintCode());
        
        //Setting up Exit and Visible to true:
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GUI.setVisible(true);
    }
}
