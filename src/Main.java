import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    static Scanner scan = new Scanner(System.in);
    static final int PLAYER1 = 1;
    static final int PLAYER2 = 2;
    static final int FIELD_SIZE = 10;

    public static void main(String[] args) throws FileNotFoundException {
        String player1Path = takeFieldPath(PLAYER1);
        String player2Path = takeFieldPath(PLAYER2);
        String[][] player1Arr = inputField(player1Path, PLAYER1);
        String[][] player2Arr = inputField(player2Path, PLAYER2);
        char[][] player1UserMatrix = createUsersMatrix();
        char[][] player2UserMatrix = createUsersMatrix();
        startGame(player1Arr, player2Arr, player1UserMatrix, player2UserMatrix);
    }

    public static String takeFieldPath(final int PLAYER) throws FileNotFoundException {
        String path = inputEmptyFieldPath(PLAYER);
        createEmptyFieldFile(PLAYER, path);
        inputShips(PLAYER, path);
        return path;
    }

    public static void inputShips(final int PLAYER, String path) throws FileNotFoundException {
        boolean isIncorrect;
        String userAnswer;
        do {
            System.out.println("================================================================================");
            System.out.println("Player" + PLAYER + " place your ships in a file that is located along the path: " + path);
            System.out.println("When the ships are placed, type \"yes\" into the console");
            userAnswer = scan.nextLine();
            while(!Objects.equals(userAnswer, "yes")){
                System.out.println("Unknown command!");
                userAnswer = scan.nextLine();
            }
            isIncorrect = isCheckElemOfFile(path);
        }while(isIncorrect);
    }

    public static void createEmptyFieldFile(final int PLAYER_NUMBER, String path){
        boolean isIncorrect;
        do {
            isIncorrect = false;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                for (int j = 0; j < FIELD_SIZE; j++) {
                    for (int i = 0; i < FIELD_SIZE; i++) {
                        writer.write('??');
                        writer.write(' ');
                    }
                    writer.write('\n');
                }
            } catch (Exception ex) {
                System.out.println("Error! Failed to open file! Input correct path!");
                isIncorrect = true;
                path = inputEmptyFieldPath(PLAYER_NUMBER);
            }
        }while (isIncorrect);
    }

    public static String inputEmptyFieldPath(final int PLAYER_NUMBER){
        boolean isIncorrect;
        String path;
        do {
            System.out.println("================================================================================");
            System.out.printf("Enter the path to the file where an empty playing field for the Player" + PLAYER_NUMBER + " will be created: ");
            isIncorrect = false;
            path = scan.nextLine();
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("Error! This file was not found! Please enter a valid file path and try again!");
                isIncorrect = true;
            }
            if (!isIncorrect && !path.endsWith(".txt")) {
                System.out.println("Error! The file must have a .txt extension! Correct file path!");
                isIncorrect = true;
            }
            if (!isIncorrect && !file.canRead()) {
                System.out.println("Error! Impossible to read data from file.");
                isIncorrect = true;
            }
        } while (isIncorrect);
        return path;
    }
    private static String inputCoordinate() {
        final int START_NUM = 0;
        final int DASH = 45;
        final int MIN_SIZE = 3;
        final int MAX_SIZE = 4;
        final int ASKI_NUM = 97;
        final int HIGH_LIMIT = 1050;
        final int LOW_LIMIT = 1039;
        final int SPACE = 32;
        boolean isIncorrect;
        int num = 0;
        String str = scan.nextLine().toUpperCase();
        do {
            isIncorrect = false;
            if (str.length() < MIN_SIZE || str.length() > MAX_SIZE) {
                isIncorrect = true;
                System.out.println("Incorrect input coordinate.");
                str = scan.nextLine().toUpperCase();
            }
            else if (str.charAt(0) < LOW_LIMIT || str.charAt(0) > HIGH_LIMIT && str.charAt(0) != SPACE) {
                isIncorrect = true;
                System.out.println("Incorrect input coordinate.");
                str = scan.nextLine().toUpperCase();
            }
            else if (str.charAt(1) != DASH) {
                isIncorrect = true;
                System.out.println("Incorrect input coordinate.");
                str = scan.nextLine().toUpperCase();
            }
            else if (str.length() == MIN_SIZE) {
                try {
                    num = Integer.parseInt(String.valueOf(str.charAt(2)));
                } catch (Exception q) {
                    isIncorrect = true;
                    System.out.println("Incorrect input coordinate.");
                    str = scan.nextLine().toUpperCase();
                }
                if (num == START_NUM) {
                    isIncorrect = true;
                    System.out.println("Incorrect input coordinate.");
                    str = scan.nextLine().toUpperCase();
                }
            } else if (str.length() == MAX_SIZE) {

                isIncorrect = false;
                try {
                    num = Integer.parseInt(String.valueOf(str.charAt(2) + str.charAt(3)));
                } catch (Exception q) {
                    isIncorrect = true;
                    System.out.println("Incorrect input coordinate.");
                    str = scan.nextLine().toUpperCase();
                }
                if (num != ASKI_NUM) {
                    isIncorrect = true;
                    System.out.println("Incorrect input coordinate.");
                    str = scan.nextLine().toUpperCase();
                }
            } else {
                System.out.println("Incorrect input coordinate.");
                isIncorrect = true;
                str = scan.nextLine().toUpperCase();
            }
        } while (isIncorrect);
        return str;
    }
    // ???????? ???????????????????????? (??-5 ?? ??????????????)
    private static int[] GetIndex(String str) {
        final int SIZE = 2;
        Map<String, Integer> map = Map.of("??", 0, "??", 1, "??", 2, "??", 3, "??", 4, "??", 5, "??", 6, "??", 7, "??", 8, "??", 9);
        int[] arr = new int[SIZE];
        arr[1] = map.get(String.valueOf(str.charAt(0)));
        if (str.length() == 3) {
            arr[0] = Integer.parseInt(String.valueOf(str.charAt(2))) - 1;
        } else {
            arr[0] = 9;
        }
        return arr;
    }

    private static String[][] inputField(String path, final int PLAYER){
        boolean isIncorrect;
        final int SIZE = 10;
        String[][] arr = new String[SIZE][SIZE];
        do {
            Scanner fileReader;
            try {
                fileReader = new Scanner(new File(path));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            isIncorrect = false;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    try {
                        arr[i][j] = fileReader.next().toUpperCase();
                    } catch (Exception e) {
                        System.out.println("Mistake of reading elements from file.");
                        path = inputEmptyFieldPath(PLAYER);
                        isIncorrect = true;
                    }
                }
            }
        } while (isIncorrect);
        return arr;
    }

    private static void consoleOutput(final int PLAYER, String[][] arr) {
        final int SIZE = 10;
        System.out.println("================================================================================");
        System.out.println("Player's" + PLAYER + " field:");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void outputField(char[][] userMatrix) {
        final int SIZE = 10;
        System.out.println("   ?? ?? ?? ?? ?? ?? ?? ?? ?? ??");
        for (int i = 0; i < SIZE; i++) {
            if (i < 9)
                System.out.print(i + 1 + "  ");
            else
                System.out.print(i + 1 + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(userMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static char[][] createUsersMatrix() {
        char[][] usMatrix = new char[13][13];
        for (int i = 0; i < usMatrix.length; i++) {
            for (int j = 0; j < usMatrix.length; j++) {
                usMatrix[i][j] = '???';
            }
        }
        return usMatrix;
    }

    private static void getFieldAfterGun(String[][] arr, int[] arrIndex, char[][] userMatrix) {
        int i = arrIndex[0];
        int j = arrIndex[1];
        if (arr[i][j].equals("??")) {
            userMatrix[i][j] = '??';
            System.out.println("Miss");
        } else {
            if (i == 0 && j == 0) {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i + 1][j].equals("??") || arr[i][j + 1].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            } else if (i == 0 && j == 9) {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i + 1][j].equals("??") || arr[i][j - 1].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            } else if (i == 9 && j == 0) {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i - 1][j].equals("??") || arr[i][j + 1].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            } else if (i == 9 && j == 9) {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i - 1][j].equals("??") || arr[i][j - 1].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            } else if (i == 0) {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i + 1][j].equals("??") || arr[i][j + 1].equals("??") || arr[i][j - 1].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            } else if (j == 0) {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i + 1][j].equals("??") || arr[i][j + 1].equals("??") || arr[i - 1][j].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            } else if (i == 9) {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i - 1][j].equals("??") || arr[i][j + 1].equals("??") || arr[i][j - 1].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            } else if (j == 9) {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i + 1][j].equals("??") || arr[i - 1][j].equals("??") || arr[i][j - 1].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            } else {
                if (arr[i][j].equals("??")) {
                    if (!(arr[i + 1][j].equals("??") || arr[i - 1][j].equals("??") || arr[i][j + 1].equals("??") || arr[i][j - 1].equals("??"))) {
                        arr[i][j] = "0";
                        userMatrix[arrIndex[0]][arrIndex[1]] = '??';
                        System.out.println("Killed");
                    } else {
                        arr[i][j] = "0";
                        userMatrix[i][j] = '??';
                        System.out.println("Wounded");
                    }
                }
            }

        }
    }
    private static void checkUserMatrixDec(char[][] userMatrix, int[] arrIndex) {
        int i = arrIndex[0];
        int j = arrIndex[1];
        int fourDeckMinusJ = j - 3;
        int fourDeckPlusJ = j + 3;
        int fourDeckMinusI = i - 3;
        int fourDeckPlusI = i - 3;
        int threeDeckMinusJ = j - 2;
        int threeDeckPlusJ = j + 2;
        int threeDeckMinusI = i - 2;
        int threeDeckPlusI = i - 2;
        int doubleDeckMinusJ = j - 1;
        int doubleDeckPlusJ = j + 1;
        int doubleDeckMinusI = i - 1;
        int doubleDeckPlusI = i - 1;
        if (fourDeckMinusJ >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i][j - 3] == '??'))
                userMatrix[i][j - 3] = '??';
        }
        if (fourDeckPlusJ >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i][j + 3] == '??'))
                userMatrix[i][j + 3] = '??';
        }
        if (fourDeckMinusI >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i - 3][j] == '??'))
                userMatrix[i - 3][j] = '??';
        }
        if (fourDeckPlusI >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i + 3][j] == '??'))
                userMatrix[i + 3][j] = '??';
        }
        if (threeDeckMinusJ >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i][j - 2] == '??'))
                userMatrix[i][j - 2] = '??';
        }
        if (threeDeckPlusJ >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i][j + 2] == '??'))
                userMatrix[i][j + 2] = '??';
        }
        if (threeDeckMinusI >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i - 2][j] == '??'))
                userMatrix[i - 2][j] = '??';
        }
        if (threeDeckPlusI >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i + 2][j] == '??'))
                userMatrix[i + 2][j] = '??';
        }
        if (doubleDeckMinusJ >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i][j - 1] == '??'))
                userMatrix[i][j - 1] = '??';
        }
        if (doubleDeckPlusJ >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i][j + 1] == '??'))
                userMatrix[i][j + 1] = '??';
        }
        if (doubleDeckMinusI >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i - 1][j] == '??'))
                userMatrix[i - 1][j] = '??';
        }
        if (doubleDeckPlusI >= 0) {
            if (userMatrix[i][j] == '??' && (userMatrix[i + 1][j] == '??'))
                userMatrix[i + 1][j] = '??';
        }
    }

    private static boolean endOfGame(String[][] arr) {
        int counter = 0;
        boolean isIncorrect = true;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (arr[i][j].equals("??"))
                    counter++;
            }
        }
        if (counter == 0) {
            isIncorrect = false;
        }
        return isIncorrect;
    }

    private static void checkStillAliveShips(char[][] arr) {
        final int SIZE = 10;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (arr[i][j] == '??') {
                    if (i > 0 && arr[i - 1][j] != '??') {
                        arr[i - 1][j] = '??';
                    }
                    if (i > 0 && j > 0 && arr[i - 1][j - 1] != '??') {
                        arr[i - 1][j - 1] = '??';
                    }
                    if (i > 0 && j < 9 && arr[i - 1][j + 1] != '??') {
                        arr[i - 1][j + 1] = '??';
                    }
                    if (i < 9 && arr[i + 1][j] != '??') {
                        arr[i + 1][j] = '??';
                    }
                    if (i < 9 && j > 0 && arr[i + 1][j - 1] != '??') {
                        arr[i + 1][j - 1] = '??';
                    }
                    if (i < 9 && j < 9 && arr[i + 1][j + 1] != '??') {
                        arr[i + 1][j + 1] = '??';
                    }
                    if (j > 0 && arr[i][j - 1] != '??') {
                        arr[i][j - 1] = '??';
                    }
                    if (j < 9 && arr[i][j + 1] != '??') {
                        arr[i][j + 1] = '??';
                    }
                }
            }
        }
    }

    public static boolean isHit(String[][] arr, int[] arrIndex) {
        boolean isHit = false;
        int i = arrIndex[0];
        int j = arrIndex[1];
        if (!(arr[i][j].equals("??"))) {
            isHit = true;
        }
        return isHit;
    }

    public static void startGame(String[][] player1Arr, String[][] player2Arr, char[][] player1UserMatrix, char[][] player2UserMatrix) {
        boolean isIncorrect;
        boolean isHit;
        int winPlayer = 2;
        String[][] arr;
        char[][] userMatrix;
        int move = 1;
        do {
            System.out.println("================================================================================");
            System.out.println("Player" + move + " move");
            System.out.println("Input coordinate(for example, ??-3)");
            String coordinate = inputCoordinate();
            int[] arrIndex = GetIndex(coordinate);
            if (move % 2 == 1){
                arr = player1Arr;
                userMatrix = player1UserMatrix;
            }else {
                arr = player2Arr;
                userMatrix = player2UserMatrix;
            }
            isHit = isHit(arr, arrIndex);
            if(!isHit){
                move++;
            }
            getFieldAfterGun(arr, arrIndex, userMatrix);
            isIncorrect = endOfGame(arr);
            checkUserMatrixDec(userMatrix, arrIndex);
            checkStillAliveShips(userMatrix);
            outputField(userMatrix);
        } while (isIncorrect);
        for(int i = 0; i < player1Arr.length; i++){
            for(int j = 0; j < player1Arr.length; j++){
                if(Objects.equals(player1Arr[i][j], "??") || Objects.equals(player1Arr[i][j], "??")){
                    winPlayer = 1;
                    break;
                }
            }
        }
        System.out.println("Player" + winPlayer + " wins!");
    }

    private static boolean isCheckElemOfFile(String path) throws FileNotFoundException {
        String[][] temp = new String[15][15];
        boolean isIncorrect = false;
        int[] counter = new int[4];
        int counterOfShips = 0;

        Scanner fileReader = new Scanner(new File(path));

        for (int i = 0; i < 10; i++) {

            for (int j = 0; j < 10; j++) {

                try {
                    temp[i][j] = fileReader.next().toUpperCase();

                } catch (Exception e) {
                    isIncorrect = true;
                }
                if (!isIncorrect && temp[i][j].equals("??"))
                    counterOfShips++;

            }
        }

        if (counterOfShips == 20) {

            for (int i = 0; i < 10; i++) {

                for (int j = 0; j < 10; j++) {

                    if (!(temp[i][j].equals("??") || temp[i][j].equals("??"))) {
                        isIncorrect = true;
                    }

                    if (!isIncorrect && temp[i][j].equals("??") && i == 0 && j == 0) { // ?????? ????????????????, ??????????  i = 0 ?? j = 0

                        if (!((temp[i + 1][j] != null && temp[i + 1][j].equals("??")) || (temp[i][j + 1] != null && temp[i][j + 1].equals("??")))) {
                            counter[0]++;
                        } else if (temp[i + 1][j] != null && temp[i + 1][j].equals("??")) {
                            if (!((temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??"))|| (temp[i][j + 1] != null && temp[i][j + 1].equals("??")))) {
                                if (temp[i + 2][j] != null && temp[i + 2][j].equals("??")) {
                                    if (temp[i + 3][j] != null && temp[i + 3][j].equals("??")) {
                                        if (temp[i + 4][j] != null && temp[i + 4][j].equals("??")) {
                                            System.out.println("The length of the ship cannot be more than 4!");
                                            isIncorrect = true;
                                        } else
                                            counter[3]++;
                                    } else
                                        counter[2]++;
                                } else
                                    counter[1]++;
                            } else {
                                System.out.println("Error: ships are placed diagonally!");
                                isIncorrect = true;
                            }
                        } else if (temp[i][j + 1] != null && temp[i][j + 1].equals("??")) {
                            if (!((temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??")) || (temp[i + 1][j] != null && temp[i + 1][j].equals("??")))) {
                                if (temp[i][j + 2] != null && temp[i][j + 2].equals("??")) {
                                    if (temp[i][j + 3] != null && temp[i][j + 3].equals("??")) {
                                        if (temp[i][j + 4] != null && temp[i][j + 4].equals("??")) {
                                            System.out.println("The length of the ship cannot be more than 4!");
                                            isIncorrect = true;
                                        } else
                                            counter[3]++;
                                    } else
                                        counter[2]++;
                                } else
                                    counter[1]++;
                            } else {
                                System.out.println("Error: ships are placed diagonally!");
                                isIncorrect = true;
                            }
                        }
                    }

                    if (!isIncorrect && temp[i][j].equals("??") && (i == 0 && j > 0)) { // ?????? ????????????????, ??????????  i = 0 ?? j > 0

                        if (!((temp[i + 1][j] != null && temp[i + 1][j].equals("??")) || (temp[i][j + 1] != null && temp[i][j + 1].equals("??")) || (temp[i][j - 1] != null && temp[i][j - 1].equals("??")))) {
                            counter[0]++;
                        } else if ((temp[i + 1][j] != null && temp[i + 1][j].equals("??"))) {
                            if (!((temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??")) || (temp[i + 1][j - 1] != null && temp[i + 1][j - 1].equals("??")) || (temp[i][j + 1] != null && temp[i][j + 1].equals("??")) || (temp[i][j - 1] != null && temp[i][j - 1].equals("??")))) {
                                if (temp[i + 2][j] != null && temp[i + 2][j].equals("??")) {
                                    if (temp[i + 3][j] != null && temp[i + 3][j].equals("??")) {
                                        if (temp[i + 4][j] != null && temp[i + 4][j].equals("??")) {
                                            System.out.println("The length of the ship cannot be more than 4!");
                                            isIncorrect = true;
                                        } else
                                            counter[3]++;
                                    } else
                                        counter[2]++;
                                } else
                                    counter[1]++;
                            } else {
                                System.out.println("Error: ships are placed diagonally!");
                                isIncorrect = true;
                            }
                        } else if ((temp[i][j + 1] != null && temp[i][j + 1].equals("??")) && (temp[i][j - 1] != null && !(temp[i][j - 1].equals("??")))) {
                            if (!((temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??")) || (temp[i + 1][j - 1] != null && temp[i + 1][j - 1].equals("??"))|| (temp[i + 1][j] != null && temp[i + 1][j].equals("??")))) {
                                if (temp[i][j + 2] != null && temp[i][j + 2].equals("??")) {
                                    if (temp[i][j + 3] != null && temp[i][j + 3].equals("??")) {
                                        if (temp[i][j + 4] != null && temp[i][j + 4].equals("??")) {
                                            System.out.println("The length of the ship cannot be more than 4!");
                                            isIncorrect = true;
                                        } else
                                            counter[3]++;
                                    } else
                                        counter[2]++;
                                } else
                                    counter[1]++;
                            } else {
                                System.out.println("Error: ships are placed diagonally!");
                                isIncorrect = true;
                            }
                        }
                    }

                    if (!isIncorrect && temp[i][j].equals("??") && (j == 0 && i > 0)) { // ?????? ????????????????, ??????????  j = 0 ?? i > 0

                        if (!((temp[i + 1][j] != null && temp[i + 1][j].equals("??")) || (temp[i][j + 1] != null && temp[i][j + 1].equals("??")) || (temp[i - 1][j] != null && temp[i - 1][j].equals("??")))) {
                            counter[0]++;
                        } else if ((temp[i + 1][j] != null && temp[i + 1][j].equals("??")) && (temp[i - 1][j] != null && !(temp[i - 1][j].equals("??")))) {
                            if (!((temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??")) || (temp[i - 1][j + 1] != null && temp[i - 1][j + 1].equals("??")) || (temp[i][j + 1] != null && temp[i][j + 1].equals("??")))) {
                                if (temp[i + 2][j] != null && temp[i + 2][j].equals("??")) {
                                    if (temp[i + 3][j] != null && temp[i + 3][j].equals("??")) {
                                        if (temp[i + 4][j] != null && temp[i + 4][j].equals("??")) {
                                            System.out.println("The length of the ship cannot be more than 4!");
                                            isIncorrect = true;
                                        } else
                                            counter[3]++;
                                    } else
                                        counter[2]++;
                                } else
                                    counter[1]++;
                            } else {
                                System.out.println("Error: ships are placed diagonally!");
                                isIncorrect = true;
                            }
                        } else if ((temp[i][j + 1] != null && temp[i][j + 1].equals("??"))) {
                            if (!((temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??")) || (temp[i - 1][j + 1] != null && temp[i - 1][j + 1].equals("??")) || (temp[i + 1][j] != null && temp[i + 1][j].equals("??")) || (temp[i - 1][j] != null && temp[i - 1][j].equals("??")))) {
                                if (temp[i][j + 2] != null && temp[i][j + 2].equals("??")) {
                                    if (temp[i][j + 3] != null && temp[i][j + 3].equals("??")) {
                                        if (temp[i][j + 4] != null && temp[i][j + 4].equals("??")) {
                                            System.out.println("The length of the ship cannot be more than 4!");
                                            isIncorrect = true;
                                        } else
                                            counter[3]++;
                                    } else
                                        counter[2]++;
                                } else
                                    counter[1]++;
                            } else {
                                System.out.println("Error: ships are placed diagonally!");
                                isIncorrect = true;
                            }
                        }
                    }


                    if (!isIncorrect && temp[i][j].equals("??") && i != 0 && j != 0) { // ?????? ???????? ?????????????????? ??????????????????

                        if (!(((temp[i + 1][j] != null && temp[i + 1][j].equals("??")) || (temp[i - 1][j] != null && temp[i - 1][j].equals("??")) || (temp[i][j + 1] != null && temp[i][j + 1].equals("??")) || (temp[i][j - 1] != null && temp[i][j - 1].equals("??"))) && ((temp[i - 1][j - 1] != null && temp[i - 1][j - 1].equals("??")) || (temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??")) || (temp[i + 1][j - 1] != null && temp[i + 1][j - 1].equals("??")) || (temp[i - 1][j + 1] != null && temp[i - 1][j + 1].equals("??"))))) {
                            counter[0]++;
                        } else if ((temp[i + 1][j] != null && temp[i + 1][j].equals("??")) && (temp[i - 1][j] != null && !(temp[i - 1][j].equals("??")))) {
                            if (!((temp[i - 1][j - 1] != null && temp[i - 1][j - 1].equals("??")) || (temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??")) || (temp[i + 1][j - 1] != null && temp[i + 1][j - 1].equals("??")) || (temp[i - 1][j + 1] != null && temp[i - 1][j + 1].equals("??")) || (temp[i][j + 1] != null && temp[i][j + 1].equals("??")) || (temp[i][j - 1] != null && temp[i][j - 1].equals("??")))) {
                                if (temp[i + 2][j] != null && temp[i + 2][j].equals("??")) {
                                    if (temp[i + 3][j] != null && temp[i + 3][j].equals("??")) {
                                        if (temp[i + 4][j] != null && temp[i + 4][j].equals("??")) {
                                            System.out.println("The length of the ship cannot be more than 4!");
                                            isIncorrect = true;
                                        } else
                                            counter[3]++;
                                    } else
                                        counter[2]++;
                                } else
                                    counter[1]++;
                            }
                            else {
                                System.out.println("Error: ships are placed diagonally!");
                                isIncorrect = true;
                            }

                        } else if ((temp[i][j + 1] != null && temp[i][j + 1].equals("??")) && (temp[i][j - 1] != null && !(temp[i][j - 1].equals("??")))) {
                            if (!((temp[i - 1][j - 1] != null && temp[i - 1][j - 1].equals("??")) || (temp[i + 1][j + 1] != null && temp[i + 1][j + 1].equals("??")) || (temp[i + 1][j - 1] != null && temp[i + 1][j - 1].equals("??")) || (temp[i - 1][j + 1] != null && temp[i - 1][j + 1].equals("??")) || (temp[i + 1][j] != null && temp[i + 1][j].equals("??")) || (temp[i - 1][j] != null && temp[i - 1][j].equals("??")))) {
                                if (temp[i][j + 2] != null && temp[i][j + 2].equals("??")) {
                                    if (temp[i][j + 3] != null && temp[i][j + 3].equals("??")) {
                                        if (temp[i][j + 4] != null && temp[i][j + 4].equals("??")) {
                                            System.out.println("The length of the ship cannot be more than 4!");
                                            isIncorrect = true;
                                        } else
                                            counter[3]++;
                                    } else
                                        counter[2]++;
                                } else
                                    counter[1]++;
                            }
                            else {
                                System.out.println("Error: ships are placed diagonally!");
                                isIncorrect = true;
                            }
                        }
                    }
                }
            }
        }
        else {
            isIncorrect = true;
            System.out.println("The number of ships is out of proportion!");
        }

        if (counter[0] != 4 || counter[1] != 3 || counter[2] != 2 || counter[3] != 1) {
            System.out.println("Error in the distribution of ships!");
            isIncorrect = true;
        }


        System.out.println("The result of the check (if false, then there are no errors): " + isIncorrect);

        fileReader.close();
        return isIncorrect;
    }
}