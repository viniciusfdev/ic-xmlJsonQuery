package util;

import java.util.ArrayList;


/**
 * @version 2.0
 * @author Evandrino Barros, Jônatas Tonholo
 */
public final class Util {
    public static int binarySearch(
            ArrayList<Integer> occurrenceList, int firstPos, Integer tagId) {
        int min = firstPos;
        int max = occurrenceList.size() == 0 ? 0 : occurrenceList.size() - 1;
        int middle = (min + max) / 2;
        if (occurrenceList.size() != 0) {
            do {
                middle = (min + max) / 2;
                //System.out.println("Min: "+min+" - Max: "+max+" - Mid: "+mid);
                if (tagId > occurrenceList.get(middle)) {
                    min = middle + 1;
                } else {
                    max = middle - 1;
                }

            } while ((tagId != occurrenceList.get(middle)) && (min <= max));
        }
        return middle;
    }
}
