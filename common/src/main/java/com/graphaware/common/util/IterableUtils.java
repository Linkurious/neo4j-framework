/*
 * Copyright (c) 2013 GraphAware
 *
 * This file is part of GraphAware.
 *
 * GraphAware is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.common.util;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.collection.Iterables;
import org.neo4j.tooling.GlobalGraphOperations;

import java.util.*;

/**
 * Utility methods for dealing with {@link Iterable}s.
 */
public final class IterableUtils {

    /**
     * Count all nodes in a database.
     * <p/>
     * Please note that this can be an expensive operation! As such, it is intended mainly for testing.
     *
     * @param database in which to count nodes.
     * @return all nodes in the database (including root with ID 0, i.e. an brand new database will have 1 node).
     */
    public static long countNodes(GraphDatabaseService database) {
        return count(GlobalGraphOperations.at(database).getAllNodes());
    }

    /**
     * Count items in an iterable.
     *
     * @param iterable to count items in.
     * @return number of items in the iterable.
     */
    public static long count(Iterable iterable) {
        if (iterable instanceof Collection) {
            return ((Collection) iterable).size();
        }

        return Iterables.count(iterable);
    }

    /**
     * Check whether an iterable contains the given object.
     *
     * @param iterable to check in.
     * @param object   to look for.
     * @param <T>      type of the objects stored in the iterable.
     * @return true iff the object is contained in the iterable.
     */
    public static <T> boolean contains(Iterable<T> iterable, T object) {
        if (iterable instanceof Collection) {
            return ((Collection) iterable).contains(object);
        }

        for (T t : iterable) {
            if (t.equals(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert an iterable to a list.
     *
     * @param iterable to convert.
     * @param <T>      type of the items held.
     * @return a list.
     */
    public static <T> List<T> toList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();

        if (iterable instanceof Collection) {
            list.addAll((Collection<T>) iterable);
        } else {
            for (T next : iterable) {
                list.add(next);
            }
        }

        return list;
    }

    /**
     * Get a random element of the given iterable.
     *
     * @param iterable to get a random element from.
     * @param <T>      type of the element.
     * @return random element.
     */
    public static <T> T random(Iterable<T> iterable) {
        if (!iterable.iterator().hasNext()) {
            throw new IllegalArgumentException("Empty iterable can't be randomized!");
        }

        List<T> list = toList(iterable);
        Collections.shuffle(list);
        return list.get(0);
    }

    /**
     * Get a single element from iterable.
     *
     * @param iterable to find a single element.
     * @param <T>      type of the element.
     * @return the element iff there is exactly one, null iff there is 0.
     * @throws IllegalStateException in case the iterable contains more than 1 element.
     */
    public static <T> T getSingle(Iterable<T> iterable) {
        T result = null;

        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            result = iterator.next();
        }

        if (iterator.hasNext()) {
            throw new IllegalStateException("Iterable has more than one element, which is unexpected");
        }

        return result;
    }

    private IterableUtils() {
    }
}
