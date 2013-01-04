package eu.tjenwellens.pairtester.groups;

/**
 *
 * @author Tjen
 */
public interface Group
{
    // database keys
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_SIZE = "size";
    static final String KEY_CORRECTS = "correct";
    static final String KEY_WRONGS = "wrong";
    static final String KEY_SKIPS = "skip";

    int getGroupId();

    String getName();

    int getSize();

    int getScore();

    int getCorrects();

    int getWrongs();

    int getSkips();

    int getProgress();
    
    void setGroupId(int id);
}
