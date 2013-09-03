/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
public class ResultMap<K, V> extends HashMap<K, V> {

    public static <T extends Enum> ResultMap<String, T> allOf(Class<T> aClass) {
        ResultMap<String, T> items = new ResultMap<String, T>();
        for(T c : aClass.getEnumConstants()) {
            items.put(c.name(), c);
        }
        return items;
    }
    List<K> indx = new ArrayList<>();

    @Override
    public V put(K key, V value) {
        indx.add(key);
        return super.put(key, value); //To change body of generated methods, choose Tools | Templates.
    }
    
    public V put(K key, V value, int i) {
        indx.add(i, key);
        return super.put(key, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V remove(Object value) {
        for(K key : keySet()) {
            if(get(key).equals(value)) {
                indx.remove(key);
            }
        }
        return super.remove(value); //To change body of generated methods, choose Tools | Templates.
    }
    
    public K getKey(int i) {
      return indx.get(i);
    }
    
    public V getValue(int i) {
        return get(getKey(i));
    }

    @Override
    public void clear() {
        indx.clear();
        super.clear(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int getIndex(K key) {
        return indx.indexOf(key);
    }

    public Collection<V> valuesByOrder() {
        List<V> l = new ArrayList<>(indx.size());
        for(K k : indx) {
            l.add(get(k));
        }
        return l;
    }

    
    
    

    
}
