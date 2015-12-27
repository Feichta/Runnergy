package com.ffeichta.runnergy;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

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

 public void testDBAccesHelperObject() {
  assertNotNull(db);
 }


 public void testCountTracks() {
  assertEquals(2, db.getTracks().size());
 }

 public void testInsertTrack() {
  Track t = new Track();
  t.setName("Bruneck-Bozen");
  assertEquals(0, db.insertTrack(t));
 }

 /**
  DBAccessHelper db = DBAccessHelper.getInstance(this);
  Track t = new Track();
  t.setName("Bruneck-Bozen");
  Log.d(TAG, "#### " + String.valueOf(db.insertTrack(t)));
  if (t.getError() != null) {
  Log.d(TAG, "#### " + String.valueOf(t.getError().get("name")));
  }
  ArrayList<Track> tracks = db.getTracks();
  if (tracks != null) {
  for (Track track : tracks) {
  Log.d(TAG, "#### " + track.toString());
  ArrayList<Activity> activities = db.getActivities(track);
  if (activities != null) {
  for (Activity activity : activities) {
  Log.d(TAG, "#### " + activity.toString());
  }
  } else {
  Log.d(TAG, "#### " + activities);
  }
  }
  }
  Log.d(TAG, "#### " + db.getActivity(2).toString());
  Log.d(TAG, "#### " + db.getActivity(100));

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