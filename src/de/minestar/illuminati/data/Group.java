/*
 * Copyright (C) 2011 MineStar.de 
 * 
 * This file is part of ContaoPlugin.
 * 
 * ContaoPlugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * ContaoPlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ContaoPlugin.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.illuminati.data;
/**
 * The name of all groups they are existing on the server ordered hierarchically
 * 
 * @author Meldanor
 * 
 */
public enum Group {

    //@formatter:off
    ADMIN   ("admins",  "a:2:{i:0;s:1:\"3\";i:1;s:1:\"2\";}"),
    MOD     ("mods",    "a:2:{i:0;s:1:\"6\";i:1;s:1:\"2\";}"),
    PAY     ("pay",     "a:1:{i:0;s:1:\"2\";}"),
    FREE    ("vip",     "a:1:{i:0;s:1:\"1\";}"),
    PROBE   ("probe",   "a:1:{i:0;s:1:\"5\";}"),
    DEFAULT ("default", "a:1:{i:0;s:1:\"4\";}"),
    X       ("X",       "a:1:{i:0;s:1:\"4\";}");
    //@formatter:on

    // The groupmnanger groupname
    private String name;
    // The serialized string in contao database
    private String contaoString;

    private Group(String name, String contaoString) {
        this.name = name;
        this.contaoString = contaoString;
    }

    /** @return The GroupManager group name as defined in the group.yml */
    public String getName() {
        return name;
    }

    /**
     * @return The serialized String in the contao database representing the
     *         group of member
     */
    public String getContaoString() {
        return contaoString;
    }

    public static Group getGroup(String groupName) {
        for (Group group : Group.values())
            if (group.getName().equalsIgnoreCase(groupName))
                return group;
        throw new RuntimeException("Unknown group name '" + groupName + "'!");
    }
}
