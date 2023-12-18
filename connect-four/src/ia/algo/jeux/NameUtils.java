package ia.algo.jeux;

public class NameUtils {

    private static int index = 0;
    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static String result = "";
    private static int count = 0;

    public static String next() {
        if (index >= alphabet.length) {
            count++;
            index = 0;
        }

        result = result.substring(0, count) + alphabet[index++];

        return result;

    }

    public static void reset(){
        index = 0;
        result = "";
        count = 0;
    }

}
