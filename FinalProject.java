//this is the version with the music//
package FinalProject;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

//Comments from Win

//This code is inefficient and can be improved upon by significantly also please read comments before doing anything

//Please tell me if there are any bugs or issues because as far as I know there isn't a single one

public class FinalProject extends JFrame{
	
	//Defining variables and tables
	
	//Defining random
	
	private static final long serialVersionUID = 1L;

	Random randomNum = new Random();
	
	//Setting up UI (moved outside of public FinalProject because they are edited outside of it)
	
	JTextField TextArea;
    JTextArea resultTextArea = new JTextArea();
    Sound sound = new Sound();
    JLabel playOptionTextArea = new JLabel("Do you want to play yes or no?");
    JLabel inputPromptLabel = new JLabel("Enter what mode you would like to play: Normal, Bot, Hard");
    JButton generateButton = new JButton("Enter");
    JPanel inputPanel = new JPanel();
    
    boolean won;
    String word;
	ArrayList<String> wordList = new ArrayList<String>(Arrays.asList("Horse", "Badger", "Zebra", "Elephant", "Duck", "Dog"));
	
	//Bot stuff
	
	ArrayList<String> botWordList = new ArrayList<String>(Arrays.asList("Horse", "Badger", "Zebra", "Elephant", "Duck", "Dog"));
	ArrayList<String> botUsedWordList = new ArrayList<String>();
	ArrayList<String> botLetters = new ArrayList<String>(Arrays.asList("b", "c", "d", "f","g","h","j","k","l","m","n","p","q","r","s","t","v","w","x","y","z"));
	ArrayList<String> botUsedLetters = new ArrayList<String>();
	ArrayList<String> botVowels = new ArrayList<String>(Arrays.asList("a", "e", "i", "o", "u"));
	ArrayList<String> botUsedVowels = new ArrayList<String>();
	
	//Bot guesses for stat display
	
    ArrayList<String> botAlreadyGuessedLetters = new ArrayList<String>();
    ArrayList<String> botAlreadyGuessedWords = new ArrayList<String>(); 
    ArrayList<String> botCorrectGuessedLetters = new ArrayList<String>();
	
    //Bot odds to guess a word in percentage
    
    int botOddsToGuessWord = 10;
    
    //Table to store hangman art
    
	static ArrayList<ArrayList<String>> hangmanArt = new ArrayList<>();
	
	//Variables used to store three words that have been in circulation in order to avoid having a word pop up again before three more rounds have been played
	
	String firstWord = "";
	String secondWord = "";
	String thirdWord = "";
	
	ArrayList<String> wordsUsedInPastThreeGames = new ArrayList<String>();
	
	//Tables used to display player stats
	
	ArrayList<String> alreadyGuessedLetters = new ArrayList<String>();
    ArrayList<String> alreadyGuessedWords = new ArrayList<String>(); 
    ArrayList<String> correctGuessedLetters = new ArrayList<String>();
    
    //Used to build stats that will be displayed
    
    StringBuilder stats = new StringBuilder();
    
    //Random variables
    
    String guessType;
    String guess;
    
    String mode = "";
    
    boolean hasCorrectlyGuessedALetter = false;
    
    boolean startNewGame = true;
    boolean start = true;
    
    int lives = 7;
    
    
    
    public FinalProject() {
    	
    	//Setting up UI
    	
    	setTitle("Hangman");
        setSize(1080, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        
        inputPanel.setLayout(new FlowLayout());

        TextArea = new JTextField(10);
        
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        
        JLabel resultLabel = new JLabel("Hangman output:");

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(new JScrollPane(resultTextArea), BorderLayout.CENTER);
        
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(resultPanel, BorderLayout.CENTER);
        playMusic(3);
        
        //If it is the start of a new game there will be text asking if the player wants to play or not
        
    	if (startNewGame == true) {
    		inputPanel.add(playOptionTextArea);
            inputPanel.add(TextArea);
            inputPanel.add(generateButton);
    	}
    	else {
            inputPanel.add(inputPromptLabel);
            inputPanel.add(TextArea);
            inputPanel.add(generateButton);
    	}
        
    	//This is what happens after the button is pressed
    	
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (startNewGame == true) {
            		
            		//Setup if it is the start of a new game
            		
            		if (TextArea.getText().toString().equalsIgnoreCase("yes")) {
            			inputPanel.remove(playOptionTextArea);
            			inputPanel.add(inputPromptLabel);
            			inputPanel.remove(TextArea);
            			inputPanel.remove(generateButton);
                        inputPanel.add(TextArea);
                        inputPanel.add(generateButton);
                        resultTextArea.setText("");
                        TextArea.setText("");
            			inputPanel.revalidate();
            			inputPanel.repaint();
            	        startNewGame = false;
                        newGame();
            		}
            		
            		//Player decided to quit
            		
            		else if (TextArea.getText().toString().equalsIgnoreCase("no")) {
            			System.exit(0);
            		}
            		
            		//Player did not type yes or no to keep playing or quit
            		
            		else {
            			resultTextArea.setText("You did not enter yes or no");
            		}
            	}
            	
            	//Important because this is how the player interacts with the rest of the code
            	
            	else if (mode != "") {
            		UserInput();
            	}
            	
            	//Player is picking their mode
            	
            	else {
            		System.out.println("Picking mode");
            		PickMode();
            	}
            }
        });
    	
        //Set UI
        
        setContentPane(contentPane);
        setVisible(true);
        
    }
    
    private void newGame() {
    	
    	System.out.println("Starting a new game");
    	
    	//Picks random word
    	
    	int number = randomNum.nextInt(wordList.size());
		word = wordList.get(number);
		
		//Makes sure that word does not reappear before three games		
		
		if (firstWord == "") {
			firstWord = word;
			wordList.remove(word);
			wordsUsedInPastThreeGames.add(word);
		}
		else if (secondWord == "") {
			secondWord = word;
			wordList.remove(word);
			wordsUsedInPastThreeGames.add(word);
		}
		else if (thirdWord == "") {
			thirdWord = word;
			wordList.remove(word);
			wordsUsedInPastThreeGames.add(word);
		}
		else {
			firstWord = secondWord;
			secondWord = thirdWord;
			thirdWord = word;
			wordList.add(wordsUsedInPastThreeGames.get(0));
			wordsUsedInPastThreeGames.remove(0);
			wordList.remove(word);
			wordsUsedInPastThreeGames.add(word);
		}
		
		//Sets the blank spots for the player to guess the letters e.g. ___ being Dog
		
		for (int i = 0; i < word.length(); i++) {
			correctGuessedLetters.add("_");
		}
		
		//Debugging
		
		System.out.println(correctGuessedLetters);
		
    }
    
    private void PickMode() {
    	
		inputPromptLabel.setText("Enter what mode you would like to play: Normal, Bot, Hard");
		
		//Converts Player input into lowercase
		
		mode = TextArea.getText().toLowerCase();

		//Runs if player did not enter normal, hard or bot
		
		if (mode.equals("normal") == false && mode.equals("hard") == false && mode.equals("bot") == false) {
			
			//Runs if the player did not put Normal, Bot or Hard
			
			mode = "";
			resultTextArea.setText("You did not enter Normal, Bot or Hard");
		}
		
		//Runs if player picked one of the three options
		
		else {
			
			//Adds the empty slot for the Bot to guess, same as player
			
			if (mode.equals("bot")) {
				for (int i = 0; i < word.length(); i++) {
					botCorrectGuessedLetters.add("_");
				}
				System.out.println(botCorrectGuessedLetters);
			}
			
			inputPromptLabel.setText("Enter if you want to guess word or letter:");
		}
		
		//Clears area to write text
		
		TextArea.setText("");
    }
    
    private void UserInput() {
    	
    	//IMPORTANT
    	
    	//Runs if player currently has the option to pick a word or letter
    	
    	if (inputPromptLabel.getText().equals("Enter if you want to guess word or letter:")) {
    		
    		//Converts user input to lowercase
    		
    		guessType = TextArea.getText().toLowerCase();
    		
    		//Runs if the player did not enter word or letter
    		
        	if (guessType.equals("word") == false && guessType.equals("letter") == false) {
                resultTextArea.setText("You did not enter word or letter");
                stats.delete(0,  stats.length());
                stats.append("You did not enter word or letter\n");
                if (start == false) {
                    HangmanImage();
                }
        	}
        	
        	//Runs if player entered word or letter
        	
        	else if (guessType.equals("word") || guessType.equals("letter")) {
        		
        		//Runs if player entered word
        		
                if (guessType.equals("word")) {
                    inputPromptLabel.setText("Enter the word that you would like to guess");	
                }
                
                //Runs if player entered letter
                
                else {
                    inputPromptLabel.setText("Enter the letter that you would like to guess");
                }
        	}
    	}
    	
    	//Runs if player guesses something
    	
    	else {
    		
    		//Converts Player's guess into lowercase
    		
    		guess = TextArea.getText().toLowerCase();
    		
    		//Runs if player did not enter anything
    		
    		if (guess.equals("")) {
    			resultTextArea.setText("You did not enter anything");
    			stats.delete(0,  stats.length());
    			stats.append("You did not enter anything\n");
    			HangmanImage();
    			return;
    		}
    		
    		//Shows you correct word
    		
    		System.out.println("Guess is: " + guess);
    		System.out.println("Word is: " + word.toLowerCase());
    		
    		//Runs if the player guesses a word
    		
    		if (guessType.equals("word")) {
    			
    			//Runs if player wins
    			
    			if (guess.equals(word.toLowerCase())) {
    				alreadyGuessedWords.add(guess);
        			won = true;
        		}
    			
    			//Runs if player gets it wrong
    			
    			else {
    				
    				alreadyGuessedWords.add(guess);
    				
    				//Takes away lives and 2 lives if the player is playing in hard mode
    				
    				if (mode.equals("hard")) {
    					if (lives == 1) {
    						lives -= 1;
    					}
    					else {
            				lives -= 2;		
    					}
    				}
    				else {
        				lives -= 1;	
    				}
    				
    				//If the player chooses the Bot mode then it is now the Bot's turn
    				
			    	if (mode.equals("bot")) {
			    		
			    		System.out.println("Bot's turn");
			    		
			    		BotTurn();
			    	}
    			}
    			
    			//Displays stats
    			
    			stats.delete(0, stats.length());
				HangmanImage();
				
    		}
    		
    		//Runs if the Player guesses a letter
    		
    		else {
    			
    			//Runs if the player enters more than one letter
    			
    			if (guess.length() != 1){
    				inputPromptLabel.setText("Enter the letter that you would like to guess");
    				stats.delete(0, stats.length());
    				stats.append("You entered more than 1 letter\n");
    				HangmanImage();
    			}
    			
    			//Runs if player only enters one letter
    			
    			else {
    				
    				boolean alreadyGuessed = false;
    				boolean correctGuess = false;
    				
    				//Runs if the Player has already guessed a letter
    				
    				if (alreadyGuessedLetters.size() != 0) {
    					
    					//Loops through the alreadyGuessedLetters table to see if the player has already guessed the letter
    					
        				for (int i = 0; i < alreadyGuessedLetters.size(); i++) {
        					
        					//Runs if the player has already guessed the letter before
        					
        					if (guess.charAt(0) == (alreadyGuessedLetters.get(i).charAt(0))) {
        						alreadyGuessed = true;
        	    				inputPromptLabel.setText("Enter the letter that you would like to guess");
        	    				stats.delete(0, stats.length());
        	    				stats.append("You already tried this letter before\n");
        	    				HangmanImage();
        						break;
        					}
        					
        				}
        				
    				}
    				
    				//Debugging
    				
    				System.out.println(alreadyGuessed);
    				
    				//Runs if the player has not already guessed the letter
    				
    				if (alreadyGuessed == false) {
    					
    					//Loops through the word to see if the letter is in the word
    					
        				for (int i = 0; i < word.length(); i++) {
        					
        					//Runs if the letter is in the word
        					
        					if (guess.charAt(0) == word.toLowerCase().charAt(i)) {
        						
        						//Sets the letter to a capital if it is the first letter of the word e.g. D_g
        						
        						if (i == 0) {
        							correctGuessedLetters.set(i, guess.toUpperCase());
        						}
        						else {
        							
        							//Sets letter to the spot where it would be in the word
        							
        							correctGuessedLetters.set(i, guess);	
        						}
        						
        						//Adds to the alreadyGuessedLetters table and sets correctGuess to true, if correctGuess is false then a life will be taken
        						
        						alreadyGuessedLetters.add(guess);
        						correctGuess = true;
        					}
        					
        				}
        				
        				//Runs if the guess was wrong
        				
        				if (correctGuess == false) {
        					
        					//Adds to the alreadyGuessedLetters table which is displayed so the player doesn't guess the same thing again
        					
        					alreadyGuessedLetters.add(guess);
        					
        					//Checks if the mode is hard and if it is 2x lives will be lost
        					
            				if (mode.equals("hard")) {
            					
            					//Reason that only one life is taken if there is only one life left is because it would go to -1 if it was the normal -2 which would be out of bounds
            					
            					if (lives == 1) {
            						lives -= 1;
            					}
            					else {
                    				lives -= 2;		
            					}
            				}
            				else {
                				lives -= 1;	
            				}
        				}
        				
        				//Displays stats
        				
        				stats.delete(0,  stats.length());
    					HangmanImage();
    					
    					//Runs if the Player didn't win and its the bot's turn
    					
    					if (won == false) {
    				    	if (mode.equals("bot")) {
    				    		
    				    		System.out.println("Bot's turn");
    				    		
    				    		BotTurn();
    				    	}
    					}
    					
    				}
    			}
    		}
    	}
    	TextArea.setText("");
    	System.out.println("Cleared");
    	
    }
    
    private void BotTurn() {
    	
    	//Not working right now
    	
    	System.out.println("Bot run");
    	
    	int botChoice = (int) (Math.random() * 100);
    	
    	if (botChoice <= (100 - botOddsToGuessWord)) {
    		
    		//Alphabet
    		
    		int botLetterChoice = (int) (Math.random() * 100);
    		
    		if (botLetterChoice <= 80) {
    			
    			//Vowel
    			
    			int randomNumber = randomNum.nextInt(botVowels.size());
    			
    			String randomVowel = botVowels.get(randomNumber);
    			
    			botVowels.remove(randomNumber);
    			
    			botUsedVowels.add(randomVowel);
    			
    			
    		}
    		else {
    			
    			int randomNumber = randomNum.nextInt(botLetters.size());
    			
    			String randomLetter = botLetters.get(randomNumber);
    			
    			botLetters.remove(randomNumber);
    			
    			botUsedLetters.add(randomLetter);
    			
    		}
    		
    	}
    	else {
    		
    		//Word
    		
    		int randomNumber = randomNum.nextInt(botWordList.size());
    		
			String randomWord = botWordList.get(randomNumber);
			
			botWordList.remove(randomNumber);
			
			botUsedWordList.add(randomWord);
    		
			botOddsToGuessWord = 10;
			
    	}
    	
    	System.out.println(botUsedLetters);
    	System.out.println(botUsedVowels);
    	System.out.println(botUsedWordList);
    	
    }
    
    private void DisplayStats() {
    	
    	System.out.println("Run display stats");
    	
    	//Checks if the player has won and if the player has won then all of the empty slots will be filled in, e.g. word is dog and this is already filled in D__ and the player guesses dog so it turns into Dog
    	
    	if (won == true) {
    		for (int i = 0; i < word.length(); i++) {
    			correctGuessedLetters.set(i, String.valueOf(word.charAt(i)));
    		}
    	}
    	
    	//Runs to see if the player has filled in all letters
		if (guess.length() == 1) {
			CheckIfCorrectLetter();
		}
		
		//Reason for (hasCorrectlyGuessedALetter == true || won == true) is because the player may just win by guessing the word and not entering a letter so this is just a preference
		
		//Adds the tables to stat which is then displayed as one big text
		
		if (hasCorrectlyGuessedALetter == true || won == true) {
			stats.append("You have correctly guessed these letters: " + correctGuessedLetters.toString() + "\n");
			
		}
		if (alreadyGuessedWords.size() != 0) {
			stats.append("You have already guessed these words: " + alreadyGuessedWords.toString() + "\n");	
		}
		if (alreadyGuessedLetters.size() != 0) {
			stats.append("You have already guessed these letters: " + alreadyGuessedLetters.toString() + "\n");	
		}
		
		stats.append("You have " + lives + " lives left");
		
		//Shows stats
		
		resultTextArea.setText(stats.toString());
		
		//Turns start to false (Do not remove)
		
		if (start == true) {
			start = false;
		}
		
		//Runs in order to see if the player won or not
		
		CheckIfWonOrLost();
		
    }
    
    private void CheckIfCorrectLetter() {
    	
    	//Checks if this letter guess is correct because if (hasCorrectlyGuessedALetter = false) then lives will be deducted 
    	//hasCorrectlyGuessedALetter is false by default and gets reset each time
    	
		//Runs through the entire word and see if the letter matches any of the letters in the words
    	
    	for (int i = 0; i < word.length(); i++) {
    		
    		//If i == 0 then that means that the character is the first letter of the word which then gets capitalized
    		
    		//Puts letters into all their correct slots e.g. e_epha_t
    		
			if (guess.charAt(0) == word.toLowerCase().charAt(i)) {
				
				if (i == 0) {
					correctGuessedLetters.set(i, guess.toUpperCase());
				}
				else {
					correctGuessedLetters.set(i, guess);	
				}
				hasCorrectlyGuessedALetter = true;
			}
		}
    	
    	//Checks if all letters have been correctly guessed or not
    	
		for (int i = 0; i < word.length(); i++) {
			
			//Debugging
			
			System.out.println(correctGuessedLetters.get(i));
			System.out.println(Character.toString(word.charAt(i)));
			
			//Breaks out of for loop if all of the letters are not correct
			
			if (correctGuessedLetters.get(i).equalsIgnoreCase(Character.toString(word.charAt(i))) == false){
				break;
			}
			
			//Player guessed all letters correctly
			
			if (i + 1 == word.length()) {
				won = true;
			}
		}
		
    }
    
    private void CheckIfWonOrLost() {
    	
    	//Debugging
    	
    	System.out.println(stats.toString());
    	System.out.println("Run check if won");
    	
		//Checking if won
    	
    	if (won == true || lives == 0) {
    		
    		//Runs if player run out of lives
    		
    		if (lives == 0 && won == false) {
    			
    			
				FinalProject gp = null;
				//Player Lost
    			gp.playSE(1);
        		stats.append("\nYou lost!");
        		
        		stats.append("\nThe correct word was: " + word);
    		}
    		
    		//Runs if player wins
    		
    		else {
    			
    			//Player Won
    			
        		stats.append("\nYou won!");	
        		FinalProject gp = null;
				gp.playSE(0);
    		}
    		    		
    		//Resetting Bot stuff
    		
    		if (mode.equals("bot")) {
        		botOddsToGuessWord = 10;	
    		}
    		
    		//Resetting Player stuff
    		
    		startNewGame = true;
    		won = false;
    		hasCorrectlyGuessedALetter = false;
    		start = true;
    		mode = "";
    		lives = 7;
    		
    		//Resetting UI
    		
        	alreadyGuessedLetters.clear();
        	alreadyGuessedWords.clear();
        	correctGuessedLetters.clear();
        	inputPromptLabel.setText("Enter what mode you would like to play: Normal, Bot, Hard");
        	inputPanel.remove(inputPromptLabel);
        	inputPanel.remove(generateButton);
        	inputPanel.add(playOptionTextArea);
        	inputPanel.add(TextArea);
        	TextArea.setText("");
        	resultTextArea.setText(stats.toString());
        	inputPanel.add(generateButton);
			inputPanel.revalidate();
			inputPanel.repaint();
			
			//Resetting stats
			
			stats.delete(0, stats.length());
			
    		System.out.println("Cleared stats");
			
    	}
    	else {
    		
    		//Player neither won or loss
    		
    		inputPromptLabel.setText("Enter if you want to guess word or letter:");
    	}
    }
    
    private void HangmanImage() {
    	
		//Displays hangman art if player lives are below 7
    	
    	if (lives != 7) {
    		
        	stats.append(hangmanArt.get(7 - (lives + 1)).toString());	
    	}
    	
    	//Displays stats
    	
    	DisplayStats();
    	
    }
    public void playMusic(int i) {
    	
		sound.setFile(i);
		sound.play();
		sound.loop();
	}
	public void stopMusic() {
		sound.stop();
	}
	public void playSE(int i) {
		sound.setFile(i);
		sound.play();
	}

    
    public static void main(String[] args) {
    	
    	//Makes UI visible when game starts
    	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FinalProject();
            }
        });
        
        //Appends hangman UI into hangmanArt Array because I don't know how to hard set tables into array list
        
        hangmanArt.add(new ArrayList<>(Arrays.asList(
                "   +---+\n",
                "  |   |\n",
                "      |\n",
                "      |\n",
                "      |\n",
                "      |\n",
                "=========\n"
        )));
        
        hangmanArt.add(new ArrayList<>(Arrays.asList(
                "  +---+\n",
                "  |   |\n",
                " O  |\n",
                "      |\n",
                "      |\n",
                "      |\n",
                "=========\n"
        )));
        
        hangmanArt.add(new ArrayList<>(Arrays.asList(
                "  +---+\n",
                "  |   |\n",
                " O  |\n",
                "  |   |\n",
                "      |\n",
                "      |\n",
                "=========\n"
        )));
        
        hangmanArt.add(new ArrayList<>(Arrays.asList(
                "  +---+\n",
                "  |   |\n",
                " O  |\n",
                " /|   |\n",
                "      |\n",
                "      |\n",
                "=========\n"
        )));
        
        hangmanArt.add(new ArrayList<>(Arrays.asList(
                "  +---+\n",
                "  |   |\n",
                " O  |\n",
                " /|\\  |\n",
                "      |\n",
                "      |\n",
                "=========\n"
        )));
        
        hangmanArt.add(new ArrayList<>(Arrays.asList(
                "  +---+\n",
                "  |   |\n",
                " O  |\n",
                " /|\\  |\n",
                " /    |\n",
                "      |\n",
                "=========\n"
        )));
        
        hangmanArt.add(new ArrayList<>(Arrays.asList(
                " +---+\n",
                "  |   |\n",
                " O  |\n",
                " /|\\  |\n",
                " / \\  |\n",
                "      |\n",
                "=========\n"
        )));
        
    }
    
}
