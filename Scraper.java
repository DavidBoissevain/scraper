import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

public class Scraper {
    public static void main(String[] args) throws Exception {
        List<String> prices;
        List<String> reviews;
        for (int i = 819300; i<= 1000000; i++){
            String URL = "https://www.coolblue.nl/en/product/";
            URL = URL + i;
            Document document = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com").ignoreHttpErrors(true).get();

            String URLr = "https://www.coolblue.nl/en/product-reviews/";
            URLr = URLr + i;
            Document documentr = Jsoup.connect(URLr).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com").ignoreHttpErrors(true).get();

            prices = document.select("strong.sales-price--current").eachText();
            if (prices.size()>0){
                reviews = document.select("a.review-rating--reviews-link").eachText();
                String ratings = documentr.select("span.review-rating--enhanced-score").text();
                String title = document.select("span.js-product-name").text();
                System.out.println("<product  product_ID = "+ i + ">" );
                System.out.println("    <title>" + title + "</title>");
                System.out.println("    <price>" + prices.get(0) + "</price>");
                if (reviews.get(0).equals("Write first review")){
                    System.out.println("    <ratings>" + "0 reviews" + "</ratings>");
                }
                else{
                    System.out.println("    <reviews>" + reviews.get(0) + "</reviews>");
                    System.out.println("    <rating>" + ratings + "</rating>");
                }

                //Code that scrapes specifications
                Elements spec_cats = document.select("div.js-specifications-content div.grid-section-xs--gap-6");
                boolean hasSpecs =false;
                for (Element category: spec_cats){
                    String thisCategory = category.select("h3").text();
                    String cat = "	<specifications category=\""+thisCategory+"\">";
                    System.out.println(cat);



                    Elements specification = category.select("div");
                    //System.out.println(specification.text());
                    //pk
                    for (Element  s: specification) {
                        String spec = "         <"+s.select("dt").text()+">"+s.select("dd").text()+"</"+s.select("dt").text()+">";
                        if (!s.select("dd").text().equals("")) {
                            System.out.println(spec);
                            hasSpecs = true;
                        }
                    }
                    String cat2 = "	</specifications category=\""+thisCategory+"\">";
                    System.out.println(cat2);
                }


                //makes sure there is no sole </specifications>
                if (hasSpecs){
                    System.out.println("	</specifications>");
                }
                //End of specification scraping code
                System.out.println("</product>");
                System.out.println("");
            }
        }
    }
}
