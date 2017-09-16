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

        for (int i = 1; i < 470; i++)
        {
            try
            {
                Document doc = Jsoup.connect("https://www.admin.ch/ch/d/pore/vi/vis" + i + ".html").get();
                Elements initiativeTitle = doc.select("head title");

                doc = Jsoup.connect("https://www.admin.ch/ch/d/pore/vi/vis" + i + "t.html").get();
                Elements initiativeTextElements = doc.select("body div div.container-fluid").get(1).select("div.col-sm-12 p span");
                String initativeText = "";

                for (Element element : initiativeTextElements)
                {
                    initativeText.concat(element.text());
                }

                initiatives.add(new InitiativeCral(initiativeTitle.text(), initativeText));

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
