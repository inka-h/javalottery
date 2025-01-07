/* Coursework for 5G00DL96-3009 Programming Languages 1, TAMK
 * 
 * December 2023
 * 
 * Inka Honkala
 * inka-h */

import java.util.Arrays;
import java.util.Scanner;

public class LotteryMachine {

    //Initializing max values to define the scope of the game
    //These max values should be changeable without changing any values elsewhere in the code!
    private static final int MAX_NUMBER = 39;
    private static final int MAX_TICKETS_PER_ROUND = 100;
    private static final int MAX_PLAYERS = 10;

    private static Player[] players = new Player[MAX_PLAYERS];
    private static int totalTicketsThisRound = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
        //Printing basic info only once..
        System.out.println();
        System.out.println("Maximum number of players per round: " + MAX_PLAYERS);
        System.out.println("Maximum number of tickets per player per round: " + MAX_TICKETS_PER_ROUND);
        System.out.println("Good luck!");

        //..and then repeating the menu every time
        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("Choose (1-6): ");
            //The selection doesn't need input methods because of the switch structure
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    readPrizes();
                    break;
                case "2":
                    generateTickets();
                    break;
                case "3":
                    inputTickets();
                    break;
                case "4":
                    showAllTickets();
                    break;
                case "5":
                    play();
                    break;
                case "6":
                    running = false;
                    break;
                default:
                    System.out.println("Make a valid selection, please!");
            }
        }

        System.out.println("Thanks for playing. Bye!");
    }

    //What the menu shows
    private static void showMenu() {
        System.out.println();
        System.out.println("MENU:");
        System.out.println("1. Read Prizes");
        System.out.println("2. Generate Tickets");
        System.out.println("3. Input Tickets");
        System.out.println("4. Show all tickets");
        System.out.println("5. PLAY!");
        System.out.println("6. Quit");
        System.out.println();
    }

    //Printout of the prize table *** (selection 1/6) ***
    //NOTE: If these are changed, the changes have to be made in calculateWinnings() as well!
    private static void readPrizes() {
        System.out.println("WIN TABLE:");
        System.out.println("Match 3: 50 e");
        System.out.println("Match 4: 100 e");
        System.out.println("Match 5: 1000 e");
        System.out.println("Match 5 + 1 bonus number: 10 000 e");
        System.out.println("Match 6: 100 000 e");
        System.out.println("Match 6 + 1 bonus number: 500 000 e");
        System.out.println("Match 7: 1 000 000 e");
        System.out.println("Match 7 + 1 bonus number: 1 200 000 e");
        System.out.println("Press <Enter> to return to menu");
    
        scanner.nextLine();
    }    

    //Generate a specific number of random tickets (up to total maximum per round) *** (selection 2/6) ***
    private static void generateTickets() {

        //Checking if there's space for more tickets in the game
        int availableTickets = MAX_TICKETS_PER_ROUND - totalTicketsThisRound;
        //If more don't fit, tell user that and prompt for acknowledgement before returning to menu
        if (availableTickets <= 0) {
            System.out.println("All " + MAX_TICKETS_PER_ROUND + " tickets are already used!");
            System.out.println("Press <Enter> to return to menu");
            scanner.nextLine();
            return;
        }

        //If more can fit, ask how many to generate and assign tickets to player (inputs are tested first)
        int numTickets = goodIntInput(1, MAX_TICKETS_PER_ROUND - totalTicketsThisRound, "Enter number of tickets to generate: ");
        String playerName = goodStringInput("Enter player name for the tickets: ");
        Player player = findOrCreatePlayer(playerName);
    
        //If there are still tickets available but no player slots free for a new player, findOrCreatePlayer returns 'null'
        if (player == null) {
            System.out.println("No new players can be added. Please add tickets to an existing player.");
            playerName = goodStringInput("Enter an existing player's name for the tickets: ");
            //findExistingPlayer doesn't have the option to add a new player (because we know there's no space if we're here)
            player = findExistingPlayer(playerName);
        
            //If tickets still can't be allocated to anyone, give up and go back to menu
            if (player == null) {
                System.out.println("Player not found.");
                System.out.println("Press <Enter> to return to menu");
                scanner.nextLine();
                return;
            }
        }

        //For the number of tickets wanted, generate arrays of 7 regular numbers and 3 bonus numbers..
        for (int i = 0; i < numTickets; i++) {
            int[] regularNumbers = generateRandomNumbers(7, MAX_NUMBER);
            int[] bonusNumbers = generateRandomNumbers(3, MAX_NUMBER);
            //.. and turn them into a lottery ticket, which is shown
            Ticket ticket = new Ticket(regularNumbers, bonusNumbers);
            player.addTicket(ticket);
            ticket.displayTicket();
        }
        
        //Increase the number of tickets used up during the round
        totalTicketsThisRound += numTickets;
    }

    //Manually input a specific number of lottery tickets (up to total maximum per round) *** (selection 3/6) ***
    private static void inputTickets() {

        //Checking if there's space for more tickets in the game
        int availableTickets = MAX_TICKETS_PER_ROUND - totalTicketsThisRound;
        //If more don't fit, tell user that and prompt for acknowledgement before returning to menu
        if (availableTickets <= 0) {
            System.out.println("All " + MAX_TICKETS_PER_ROUND + " tickets are already used!");
            System.out.println("Press <Enter> to return to menu");
            scanner.nextLine();
            return;
        }

        //When there's space, ask for the tickets to input
        int numTickets = goodIntInput(1, MAX_TICKETS_PER_ROUND - totalTicketsThisRound, "Enter number of tickets to input: ");
        String playerName = goodStringInput("Enter player name for the tickets: ");
        Player player = findOrCreatePlayer(playerName);

        //If there are still tickets available but no player slots free for a new player, findOrCreatePlayer returns 'null'
        if (player == null) {
            System.out.println("No new players can be added. Please add tickets to an existing player.");
            playerName = goodStringInput("Enter an existing player's name for the tickets: ");
            //findExistingPlayer doesn't have the option to add a new player (because we know there's no space if we're here)
            player = findExistingPlayer(playerName);
        
            //If tickets still can't be allocated to anyone, give up and go back to menu
            if (player == null) {
                System.out.println("Player not found.");
                System.out.println("Press <Enter> to return to menu");
                scanner.nextLine();
                return;
            }
        }
    
        for (int i = 0; i < numTickets; i++) {
            System.out.println("Ticket " + (i + 1) + ":");

            //Ask regular numbers
            System.out.println("*Enter 7 regular numbers*");
            int[] regularNumbers = readNumbersFromUser(7);

            //Ask bonus numbers
            System.out.println("*Enter 3 bonus numbers*");
            int[] bonusNumbers = readNumbersFromUser(3);

            //Turn them into a ticket
            Ticket ticket = new Ticket(regularNumbers, bonusNumbers);
            player.addTicket(ticket);
            ticket.displayTicket();
        }
    
        totalTicketsThisRound += numTickets;
    }    

    //Prints out all the tickets of each player in the round currently *** (selection 4/6) ***
    private static void showAllTickets() {

        //'players' is initialized as 10 spots of null, check if there's actual Players
        boolean hasPlayers = false;
        for (Player player : players) {
            if (player != null) {
                hasPlayers = true;
                break;
            }
        }

        //If there's no actual Players, tell user and wait for acknowledgement before returning to Menu
        if (!hasPlayers) {
            System.out.println("No tickets have been made yet!");
            System.out.println("Press <Enter> to return to menu");
            scanner.nextLine();
            return;
        }

        //For every spot in 'players' that isn't null, show the tickets
        for (Player player : players) {
            if (player != null) {
                player.displayTickets();
            }
        }

        System.out.println("Press <Enter> to return to menu");
        scanner.nextLine();
    }

    //Plays the round: generates the winning ticket and compares all player tickets against it to calculate wins (selection 5/6)
    private static void play() {

        //'players' is initialized as 10 spots of null, check if there's actual Players
        boolean hasPlayers = false;
        for (Player player : players) {
            if (player != null) {
                hasPlayers = true;
                break;
            }
        }

        if (!hasPlayers) {
            System.out.println("No-one has any tickets to play.");
            return;
        }

        //Generate winning ticket
        Ticket winningTicket = new Ticket(generateRandomNumbers(7, MAX_NUMBER), generateRandomNumbers(3, MAX_NUMBER));
        String dashLine = "- - ".repeat(20);

        System.out.println(dashLine);
        System.out.println("CORRECT NUMBERS FOR THIS ROUND:");
        System.out.println();
        winningTicket.displayTicket();
        System.out.println(dashLine);
        System.out.println();

            //Compare each player's tickets to the winning ticket, excluding the potential nulls left in 'players'
            for (Player player : players) {
                if (player != null) {  
                    System.out.println(player.getName() + "'s results:");
                    System.out.println();
                    int totalWinnings = 0;

            //Print the winning tickets and their prizes for each player
            for (Ticket ticket : player.getPlayerTickets()) {
                if (ticket != null) {
                    int matchRegular = countMatches(ticket.getRegularNumbers(), winningTicket.getRegularNumbers());
                    int matchBonus = countMatches(ticket.getBonusNumbers(), winningTicket.getBonusNumbers());
                    int winnings = calculateWinnings(ticket, winningTicket);
                    
                    // Display the ticket, the count of correct numbers and bonus numbers, and winnings
                    if (winnings > 0) {
                        ticket.displayTicket();
                        System.out.print("  " + matchRegular);
                        if (matchBonus > 0) {
                            System.out.print(" + " + matchBonus);
                        }
                        System.out.println(" correct, prize: " + winnings + " e");
                    }
                    totalWinnings += winnings;
                }
            }
            //Give the sum total of the wins
            System.out.println();
            System.out.println("Total winnings for " + player.getName() + ": " + totalWinnings + " e");
            System.out.println();
            System.out.println(dashLine);
            System.out.println();
                }
            }
    
        System.out.println("Press <Enter> to return to menu. The game will be reset.");
        scanner.nextLine();    
        //Setting 'players' and total tickets back to initial values
        players = new Player[MAX_PLAYERS];
        totalTicketsThisRound = 0;
    }


/* ------------------ HELP METHODS -------------------- */

    //Integer within range min-max from user
    private static int goodIntInput(int min, int max, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Bad input. Please enter a valid number.");
            }
        }
    }

    //String of length >0 from user
    private static String goodStringInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Name can't be empty. Please enter a valid name.");
            }
        }
    }
    
    //Checks whether the player name is in use, adds tickets to player if player exists and creates new player if it doesn't
    private static Player findOrCreatePlayer(String name) {
        //First we look for players that aren't null and see if they match the name..
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getName().equalsIgnoreCase(name)) {
                return players[i];
            }
        }

        //..if there's no match, we look for the first available 'null' spot and create the player
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(name, MAX_TICKETS_PER_ROUND);
                return players[i];
            }
        }

        //If there's no nulls left in players, notify and return 'null'
        System.out.println("Maximum number of players reached.");
        return null;
    }

    //Only checks for a match for a player, without attempting to make a new one
    private static Player findExistingPlayer(String name) {
        // Look for players that aren't null and see if they match the name
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getName().equalsIgnoreCase(name)) {
                return players[i];
            }
        }
    
        // If there's no match, just return null
        return null;
    }

    //Generate 'count' amount of random numbers between 1 and 'max'
    private static int[] generateRandomNumbers(int count, int max) {
        int[] numbers = new int[count];
        int index = 0;

        //Math.random() returns 0.0 to <1.0 so we multiply by max and add 1 to make it 1 to max
        while (index < count) {
            int number = (int) (Math.random() * max) + 1;
            //Check if the number is already in..
            boolean isDuplicate = false;
            for (int i = 0; i < index; i++) {
                if (numbers[i] == number) {
                    isDuplicate = true;
                    break;
                }
            }
            //..and add it if it's not
            if (!isDuplicate) {
                numbers[index] = number;
                index++;
            }
        }
        //Sort from smallest to largest and return the randomized numbers
        Arrays.sort(numbers);
        return numbers;
    }
    
    //Read 'count' amount of numbers from the user, inputs are checked
    private static int[] readNumbersFromUser(int count) {
        int[] numbers = new int[count];
        int index = 0;
        while (index < count) {
            int number = goodIntInput(1, MAX_NUMBER, "Enter number " + (index + 1) + ": ");
            //Check for uniqueness
            boolean isDuplicate = false;
            for (int i = 0; i < index; i++) {
                if (numbers[i] == number) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                numbers[index] = number;
                index++;
            } else {
                System.out.println("That number is already on this ticket. Pick some other number!");
            }
        }
        return numbers;
    }    
     
    //Check how much a ticket won by comparing it to the winning ticket
    private static int calculateWinnings(Ticket ticket, Ticket winningTicket) {
        //Matches in regular numbers and bonus numbers checked separately
        int matchRegular = countMatches(ticket.getRegularNumbers(), winningTicket.getRegularNumbers());
        int matchBonus = countMatches(ticket.getBonusNumbers(), winningTicket.getBonusNumbers());
        
        //Returns winnings
        if (matchRegular == 7 && matchBonus >= 1) return 1200000;
        if (matchRegular == 7) return 1000000;
        if (matchRegular == 6 && matchBonus >= 1) return 500000;
        if (matchRegular == 6) return 100000;
        if (matchRegular == 5 && matchBonus >= 1) return 10000;
        if (matchRegular == 5) return 1000;
        if (matchRegular == 4) return 100;
        if (matchRegular == 3) return 50;
        
        //Or 0 if no winnings
        return 0;
    }
    
    //Takes a player's regular numbers or bonus numbers, and compares them against the correct numbers of the round
    private static int countMatches(int[] playerNumbers, int[] winningNumbers) {
        //Comparison is made for every playerNumber against every winningNumber and matches counted
        int matchCount = 0;
        for (int playerNumber : playerNumbers) {
            for (int winningNumber : winningNumbers) {
                if (playerNumber == winningNumber) {
                    matchCount++;
                    break;
                }
            }
        }
        return matchCount;
    }
    
}



/* ------------------ CLASSES -------------------- */

class Ticket {
    //Every ticket has regular numbers and bonus numbers
    private int[] regularNumbers;
    private int[] bonusNumbers;

    public Ticket(int[] regularNumbers, int[] bonusNumbers) {
        //The numbers should always be in ascending order
        Arrays.sort(regularNumbers);
        Arrays.sort(bonusNumbers);

        this.regularNumbers = regularNumbers;
        this.bonusNumbers = bonusNumbers;
    }

    public int[] getRegularNumbers() {
        return regularNumbers;
    }

    public int[] getBonusNumbers() {
        return bonusNumbers;
    }

    public void displayTicket() {
        System.out.print("Regular Numbers: " + Arrays.toString(regularNumbers) + " | ");
        System.out.println("Bonus Numbers: " + Arrays.toString(bonusNumbers));
    }
}

class Player {
    //Every player has a name and some tickets
    private String name;
    private Ticket[] playerTickets;

    public Player(String name, int maxTickets) {
        this.name = name;
        this.playerTickets = new Ticket[maxTickets];
        //Initialize everything as null
        for (int i = 0; i < playerTickets.length; i++) {
            playerTickets[i] = null;
        }
    }

    //Added tickets replace nulls
    public void addTicket(Ticket ticket) {
        for (int i = 0; i < playerTickets.length; i++) {
            if (playerTickets[i] == null) {
                playerTickets[i] = ticket;
                return;
            }
        }
    }

    public void displayTickets() {
        System.out.println(name + "'s Tickets:");
        for (Ticket ticket : playerTickets) {
            if (ticket != null) {
                ticket.displayTicket();
            }
        }
    }

    public String getName() {
        return name;
    }

    public Ticket[] getPlayerTickets() {
        return playerTickets;
    }
}

