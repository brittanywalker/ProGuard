/* $Id: ListUtil.java,v 1.3 2003/12/06 22:12:42 eric Exp $
 *
 * ProGuard -- obfuscation and shrinking package for Java class files.
 *
 * Copyright (c) 2002-2003 Eric Lafortune (eric@graphics.cornell.edu)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.util;

import java.util.*;


/**
 * This class provides some utility methods for working with
 * <code>java.util.List</code> objects.
 *
 * @author Eric Lafortune
 */
public class ListUtil
{
    /**
     * Creates a comma-separated String from the given List of String objects.
     */
    public static String commaSeparatedString(List list)
    {
        if (list == null)
        {
            return null;
        }

        StringBuffer buffer = new StringBuffer();

        for (int index = 0; index < list.size(); index++)
        {
            if (index > 0)
            {
                buffer.append(',');
            }

            buffer.append(list.get(index));
        }

        return buffer.toString();
    }


    /**
     * Creates a List of String objects from the given comma-separated String.
     */
    public static List commaSeparatedList(String string)
    {
        if (string == null)
        {
            return null;
        }

        List list = new ArrayList();
        int index = 0;
        while (index < string.length())
        {
            int nextIndex = string.indexOf(',', index);
            if (nextIndex < 0)
            {
                nextIndex = string.length();
            }

            list.add(string.substring(index, nextIndex).trim());

            index = nextIndex + 1;
        }

        return list;
    }
}