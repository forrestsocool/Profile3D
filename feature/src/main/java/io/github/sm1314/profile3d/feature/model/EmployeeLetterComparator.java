package io.github.sm1314.profile3d.feature.model;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Comparator;


public class EmployeeLetterComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee l, Employee r) {
        if (l == null || r == null) {
            return 0;
        }

        String lhsSortLetters = Pinyin.toPinyin(l.getName(),"").substring(0, 1).toUpperCase();
        String rhsSortLetters = Pinyin.toPinyin(r.getName(),"").substring(0, 1).toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}