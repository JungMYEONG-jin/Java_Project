package crawler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WikiManagerTest {

    @Test
    public void getNumTest(){
        WikiManager wikiManager = new WikiManager();
        System.out.println(wikiManager.getNumSize(100));
        System.out.println(wikiManager.getNumSize(1));
        System.out.println(wikiManager.getNumSize(11));

    }

}