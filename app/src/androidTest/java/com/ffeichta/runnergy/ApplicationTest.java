package com.ffeichta.runnergy;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.Coordinate;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;
import java.util.Date;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    DBAccessHelper db = null;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        db = DBAccessHelper.getInstance(getContext());
    }
   /* protected void tearDown() throws Exception {
        super.tearDown();
        getContext().deleteDatabase(db.getDatabaseName());
        db.insertTestData(db.getWritableDatabase());
    }*/

    /**
     * Test objects
     */
    public void testDBAccesHelperObject() {
        assertNotNull(db);
    }


    /**
     * Test get methods
     */
    public void testCountTracks() {
        assertEquals(2, db.getTracks().size());
    }

    public void testOrderOfGetTracksMethod() {
        Track t = db.getTracks().get(1);
        assertEquals("Ahornach-Rein", t.getName());
    }

    public void testCountOfActivitiesOfTrack() {
        Track t = db.getTracks().get(1);
        assertEquals(1, db.getActivities(t).size());
    }

    public void testTypeOfActivityOfTrack() {
        Activity a = db.getTracks().get(0).getActivities().get(0);
        assertEquals(Activity.Type.CYCLING.toString(), a.getType().toString());
    }

    public void testCountOfCoordinatesOfActivity() {
        Activity a = db.getTracks().get(1).getActivities().get(0);
        ArrayList<Coordinate> c = db.getCoordinates(a);
        assertEquals(4, c.size());
    }

    public void testGetUnknownActivity() {
        assertEquals(null, db.getActivity(100));
    }

    public void testGetUnknownCoordinate() {
        assertEquals(null, db.getCoordinate(100));
    }

    public void testClosestCoordinate() {
        assertEquals(1, db.getIDOfClosestCoordinateInActivity(11.354912, 46.499562, 1));
    }


    /**
     * Test insert methods
     */
    public void testInsertTrack() {
        Track t = new Track();
        t.setName("Bruneck-Bozen");
        assertEquals(0, db.insertTrack(t));
    }

    public void testInsertTrackWhichAlreadyExists() {
        Track t = new Track();
        t.setName("Bruneck-Bozen");
        db.insertTrack(t);
        assertEquals(Track.NAME_ALREADY_EXISTS, (int) t.getError().get("name"));
    }

    public void testInsertActivityWithNoTrack() {
        Activity a1 = new Activity();
        assertEquals(-1, db.insertActivity(a1));
    }

    public void testInsertActivityWithErrorInCoordinate() {
        Activity a = new Activity();
        a.setType(Activity.Type.RUNNING);
        a.setDate(new Date().getTime());
        a.setDuration(1259);
        a.setTrack(db.getTracks().get(1));
        Coordinate c = new Coordinate();
        c.setLongitude(11.354912);
        c.setLatitude(46.499562);
        c.setStart(true);
        c.setEnd(false);
        c.setTimeFromStart(0);
        c.setDistanceFromPrevious(0);
        ArrayList<Coordinate> coordinatesInsert = new ArrayList<>();
        coordinatesInsert.add(c);
        c.setActivity(a);
        a.setCoordinates(coordinatesInsert);
        assertEquals(0, db.insertActivity(a));
    }

    public void testInsertActivity() {
        Activity a = new Activity();
        a.setType(Activity.Type.RUNNING);
        a.setDate(new Date().getTime());
        a.setDuration(1259);
        a.setTrack(db.getTracks().get(1));
        Coordinate c = new Coordinate();
        c.setLongitude(11.0003);
        c.setLatitude(59.00089);
        c.setStart(true);
        c.setEnd(false);
        c.setTimeFromStart(0);
        c.setDistanceFromPrevious(0);
        ArrayList<Coordinate> coordinatesInsert = new ArrayList<>();
        coordinatesInsert.add(c);
        c.setActivity(a);
        a.setCoordinates(coordinatesInsert);
        assertEquals(0, db.insertActivity(a));
    }

    /**
     * Test update methods
     */
    public void testUpdateTrack() {
        Track t = db.getTracks().get(1);
        t.setName("Nals-Mals");
        db.updateTrack(t);
        assertEquals("Nals-Mals", db.getTracks().get(1).getName());
    }

    /**
     * Test delete methods
     */
    public void testDeleteActivity() {
        Activity a = db.getTracks().get(1).getActivities().get(0);
        assertEquals(0, db.deleteActivity(a));
        assertEquals(null, db.getCoordinates(a));
    }

    public void testDeleteTrack() {
        Track t = db.getTracks().get(1);
        assertEquals(0, db.deleteTrack(t));
    }

    /**


     Activity a = new Activity();
     a.setType(Activity.Type.RUNNING);
     a.setDate(new Date().getTime());
     a.setDuration(1259);
     a.setTrack(t);
     Coordinate c = new Coordinate();
     c.setLongitude(11.0003);
     c.setLatitude(59.00089);
     c.setStart(true);
     c.setEnd(false);
     c.setTimeFromStart(0);
     c.setDistanceFromPrevious(0);
     ArrayList<Coordinate> coordinatesInsert = new ArrayList<>();
     coordinatesInsert.add(c);
     c.setActivity(a);
     a.setCoordinates(coordinatesInsert);
     Log.d(TAG, "#### " + db.insertActivity(a));
     /*ArrayList<Coordinate> coordinates = db.getCoordinates(a);
     for (Coordinate coordinate : coordinates) {
     Log.d(TAG, "#### " + coordinate.toString());
     }
     Log.d(TAG, "#### " + db.getCoordinate(1).toString());

     Setting s = new Setting("interval", "60");
     Log.d(TAG, "#### " + db.insertSetting(s));
     ArrayList<Setting> settings = db.getSettings();
     for (Setting setting : settings) {
     Log.d(TAG, "#### " + setting.toString());
     }

     //Log.d(TAG, "#### " + db.deleteTrack(t));
     //Log.d(TAG, "#### " + db.deleteActivity(a));

     s.setValue("70");
     Log.d(TAG, "#### " + db.updateSetting(s));
     ArrayList<Setting> settingss = db.getSettings();
     for (Setting setting : settingss) {
     Log.d(TAG, "#### " + setting.toString());
     }

     t.setName("Bozen-Rom");
     Log.d(TAG, "#### " + db.updateTrack(t));
     ArrayList<Track> trackss = db.getTracks();
     for (Track track : trackss) {
     Log.d(TAG, "#### " + track.toString());
     }

     Activity tmp = new Activity();
     tmp.setId(1);
     ArrayList<Coordinate> coordinatess = db.getCoordinates(tmp);
     for (Coordinate coordinate : coordinatess) {
     Log.d(TAG, "#### " + coordinate.toString());
     }
     Log.d(TAG, "#### " + db.getIDOfClosestCoordinateInActivity(11.354921, 46.497891, 1));
     **/

}