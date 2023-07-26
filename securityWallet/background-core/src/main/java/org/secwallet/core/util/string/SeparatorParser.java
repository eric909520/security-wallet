package org.secwallet.core.util.string;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeparatorParser {
    /**
     * Split the string according to the specified delimiter
     *
     * @param original   raw string
     * @param separator0 first-level separator
     * @param separator1 secondary separator
     * @return
     */
    public static List<SplitItem> split2List(String original, String separator0, String separator1) {
        ArrayList<SplitItem> items = Lists.newArrayList();
        List<String> list = split2List(original, separator0);
        if (CollectionUtils.isNotEmpty(list)) {
            for (String ele : list) {
                SplitItem item = split2Item(ele, separator1);
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Split the string according to the specified delimiter
     *
     * @param original  raw string
     * @param separator
     * @return
     */
    public static List<String> split2List(String original, String separator) {
        if (StringUtils.isBlank(original) || StringUtils.isBlank(separator)) {
            throw new IllegalArgumentException("Invalid original or separator");
        }
        List<String> list = Lists.newArrayList();
        if (StringUtils.contains(original, separator)) {
            String[] array = StringUtils.split(original, separator);
            list.addAll(Arrays.asList(array));
        } else {
            list.add(original);
        }
        return list;
    }

    /**
     * Split the string according to the specified delimiter
     *
     * @param original
     * @param separator
     * @return
     */
    public static SplitItem split2Item(String original, String separator) {
        List<String> list = split2List(original, separator);
        if (CollectionUtils.isNotEmpty(list) && list.size() == 2) {
            SplitItem item = new SplitItem();
            item.setFront(list.get(0));
            item.setBehind(list.get(1));
            return item;
        }
        return null;
    }
    public static class SplitItem {
        String front;
        String behind;

        public SplitItem() {
        }

        public SplitItem(String front, String behind) {
            this.front = front;
            this.behind = behind;
        }

        public String getFront() {
            return front;
        }

        public void setFront(String front) {
            this.front = front;
        }

        public String getBehind() {
            return behind;
        }

        public void setBehind(String behind) {
            this.behind = behind;
        }
    }

}
