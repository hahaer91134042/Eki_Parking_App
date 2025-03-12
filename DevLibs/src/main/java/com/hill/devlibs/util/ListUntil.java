package com.hill.devlibs.util;

import com.hill.devlibs.EnumClass.OrderBy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Hill on 2018/11/5.
 */
public class ListUntil {

    public static void listOrderBy(List<?> list,Sorter sortBuilder){
        Collections.sort(list,sortBuilder);
    }

    public static abstract class Sorter<T> implements Comparator<T> {

        public abstract OrderBy sortBy();

        public abstract int convertVar1(T var1);

        public abstract int convertVar2(T var2);

        @Override
        public int compare(T o1, T o2) {
            OrderBy orderBy = sortBy();
            int v1 = convertVar1(o1);
            int v2 = convertVar2(o2);

            switch (orderBy) {
                case ASC:
                    if (v1 > v2)
                        return 1;
                    else if (v1 == v2)
                        return 0;
                    else
                        return -1;

                case DESC:
                    if (v1 > v2)
                        return -1;
                    else if (v1 == v2)
                        return 0;
                    else
                        return 1;

                default:
                    return 0;
            }
        }
    }

}
