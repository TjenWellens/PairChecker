package eu.tjenwellens.pairtester.pairs;

import eu.tjenwellens.pairtester.groups.Group;

/**
 *
 * @author Tjen
 */
public interface GroupPair extends RatedPair
{
    // database keys
    static final String KEY_ID = "id";
    static final String KEY_GROUP_ID = "group_id";
    static final String KEY_KEY = "key";
    static final String KEY_VALUE = "value";
    static final String KEY_CORRECTS = "corrects";
    static final String KEY_WRONGS = "wrongs";
    static final String KEY_SKIPS = "last_try_correct";

    Group getGroup();
}
