package rpiplanner;

import rpiplanner.model.Course;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: okeefm
 * Date: Feb 15, 2009
 * Time: 5:22:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Apcredit {
    Course[] getcourse(ArrayList<String> tests, ArrayList<String> scores);
}
