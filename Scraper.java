import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
/**
 * A Coolblue scraper which scrapes the title, price, amount of reviews, rating and all the specifications.
 */
public class Scraper {
    public static void main(String[] args) throws Exception {
        List<String> prices;
        List<String> reviews;
        for (int i = 819300; i<= 1000000; i++){
            //Fetch the URL's for the product pages and the reviews of the product pages
            String URL = "https://www.coolblue.nl/en/product/";
            URL = URL + i;
            Document document = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com").ignoreHttpErrors(true).get();

            String URLr = "https://www.coolblue.nl/en/product-reviews/";
            URLr = URLr + i;
            Document documentr = Jsoup.connect(URLr).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com").ignoreHttpErrors(true).get();

            //Fetch the price, if it is not available, skip the page
            prices = document.select("strong.sales-price--current").eachText();
            if (prices.size()>0){
                //Print the title, price, amount of reviews and the rating.
                reviews = document.select("a.review-rating--reviews-link").eachText();
                String ratings = documentr.select("span.review-rating--enhanced-score").text();
                String title = document.select("span.js-product-name").text();
                System.out.println("<product  product_ID = "+ i + ">" );
                System.out.println("    <title>" + title + "</title>");
                System.out.println("    <price>" + prices.get(0) + "</price>");
                //If there are no reviews written yet, replace it by "0 reviews" to be in line with others
                if (reviews.get(0).equals("Write first review")){
                    System.out.println("    <ratings>" + "0 reviews" + "</ratings>");
                }
                else{
                    System.out.println("    <reviews>" + reviews.get(0) + "</reviews>");
                    System.out.println("    <rating>" + ratings + "</rating>");
                }

                //Code that scrapes specifications
                System.out.println("    <specifications>");
                Elements spec_cats = document.select("div.js-specifications-content div.grid-section-xs--gap-6");
                for (Element category: spec_cats){
                    String thisCategory = category.select("h3").text();
                    System.out.println("         <"+thisCategory.toLowerCase().replaceAll(" ", "-" ) +"-specifications>");
                    Elements specification = category.select("div");
                    for (Element  s: specification) {
                        String spec = "               <"+deDup(s.select("dt").text()).toLowerCase()+">"+s.select("dd").text()+"</"+deDup(s.select("dt").text()).toLowerCase()+">";
                        if (!s.select("dd").text().equals("")) {
                            System.out.println(spec);
                        }
                    }
                    System.out.println("         </"+thisCategory.toLowerCase().replaceAll(" ", "-" )  +"-specifications>");
                }
                System.out.println("	</specifications>");
                //End of specification scraping code

                System.out.println("</product>");
                System.out.println("");
            }
        }
    }

    /**
     * Removes double words from a String
     * @param s a string containing double words
     * @return a String that does not contain double words anymore
     */
    public static String deDup(String s) {
        return new LinkedHashSet<String>(Arrays.asList(s.split(" "))).toString().replaceAll("(^\\[|\\]$)", "").replace(", ", "-");
    }
}