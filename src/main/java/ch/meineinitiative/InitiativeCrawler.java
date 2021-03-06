package ch.meineinitiative;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;


public class InitiativeCrawler {
    public static List<InitiativeCral> crawl()
    {
        List<InitiativeCral> initiatives = new LinkedList<>();

        for (int i = 200; i < 490; i++)
        {
            try
            {
                Document doc = Jsoup.connect("https://www.admin.ch/ch/d/pore/vi/vis" + i + ".html").get();
                Elements initiativeTitle = doc.select("head title");

                doc = Jsoup.connect("https://www.admin.ch/ch/d/pore/vi/vis" + i + "t.html").get();
                Elements initiativeTextElements = doc.select("body div div.container-fluid div.col-sm-12 span,p");
                String initativeText = "";

                if(initiativeTextElements.size()==1)
                {
                    System.out.println();
                }

                for (Element element : initiativeTextElements)
                {
                    initativeText = initativeText.concat(element.text());
                }

                initiatives.add(new InitiativeCral(initiativeTitle.text(), initativeText));
                System.out.println("SUCCESS FOR ID: " + i);
            }
            catch (Exception ex)
            {
                System.out.println("FAILED FOR ID: " + i);
                System.out.println(ex);
            }
        }

        return initiatives;
    }

    public static class InitiativeCral
    {
        public String title;

        public String text;

        InitiativeCral(String title, String text){
            this.title = title;
            this.text = text;
        }

        public String getTitle(){
            return title;
        }

        public String getText(){
            return text;
        }
    }
}
