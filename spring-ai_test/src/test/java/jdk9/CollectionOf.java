package jdk9;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.Map;

public class CollectionOf {

    @Test
    public void listOf() {
        var list = List.of(1,2,3,4,5);
        System.out.println(list);
    }

    @Test
    public void setOf() {
        var set = Set.of(1,2,3,4,5);
        System.out.println(set);
    }

    @Test
    public void mapOf() {
        var map = Map.of("1",1,"2",2,"3",3,"4",4,"5",5);
        System.out.println(map);
    }
}
