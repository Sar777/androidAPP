package by.onliner.news.Factory.News;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

import by.onliner.news.Factory.IFactoryViewObjects;
import by.onliner.news.Structures.News.ViewsObjects.ViewObject;
import by.onliner.news.Structures.News.ViewsObjects.VoteViewObject;

/**
 * Формирование опроса
 */
public class VoteFactory implements IFactoryViewObjects<Element, ViewObject> {
    @Override
    public ViewObject create(Element element) {
        ArrayList<String> options = new ArrayList<>();

        for (Element checkboxElement : element.getElementsByClass("news-form__checkbox-item"))
            options.add(checkboxElement.getElementsByClass("news-form__checkbox-sign").first().text());

        Element title = element.getElementsByClass("news-vote__title").first();
        Element speech = element.getElementsByClass("news-vote__speech").first();
        Element note = element.getElementsByClass("news-vote__stripe-note").first();

        return new VoteViewObject(title.text(), speech.text(), note != null ? note.text() : "", options);
    }
}
