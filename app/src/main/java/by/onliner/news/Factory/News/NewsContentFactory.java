package by.onliner.news.Factory.News;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;

import by.onliner.news.Structures.News.News;
import by.onliner.news.Structures.News.ViewsObjects.ViewObject;

/**
 * Фабрика компонентов окна чтения новости
 */
public class NewsContentFactory {
    public static ArrayList<ViewObject> create(News news) {
        Document document = news.getContent();

        Element rootElement = document.getElementsByClass("news-text").first();
        if (rootElement == null)
            throw new IllegalArgumentException("News content factory not found news-text container from html");

        ArrayList<ViewObject> viewObjects = new ArrayList<>(rootElement.getAllElements().size());

        // Блок заголовка новости
        viewObjects.add(new HeaderFactory().create(news.getHeader()));

        // Выбор фабрики
        for (Element element : rootElement.getAllElements()) {
            switch (element.tagName()) {
                case "p":
                    viewObjects.add(new SpanFactory().create(element));
                    break;
                case "hr":
                    viewObjects.add(new HRFactory().create(element));
                    break;
                case "h2":
                    viewObjects.add(new H2Factory().create(element));
                    break;
                case "ul":
                    viewObjects.add(new ULFactory().create(element));
                    break;
                case "blockquote":
                    viewObjects.add(new QuoteFactory().create(element));
                    break;
                case "div": {
                    // Изображения по одному и видео
                    if (element.className().indexOf("news-media_extended") != -1 || element.className().indexOf("news-media_condensed") != -1) {
                        // Видео
                        if (element.getElementsByTag("iframe").size() > 0)
                            viewObjects.add(new YoutubeVideoFactory().create(element));
                        else
                            viewObjects.add(new ImageFactory().create(element));
                    }
                    // Заголовок
                    else if (element.className().indexOf("news-header__title") != -1)
                        viewObjects.add(new TitleFactory().create(element));
                    //
                    else if (element.className().indexOf("news-entry__speech") != -1)
                        viewObjects.add(new SpeechFactory().create(element));
                    // Голосование
                    else if (element.className().indexOf("news-vote") != -1 && !element.attr("data-post-id").isEmpty())
                        viewObjects.add(new VoteFactory().create(element));
                    // Слайдер изображений
                    else if (element.className().indexOf("news-media__gallery") != -1)
                        viewObjects.add(new ImageSliderFactory().create(element));
                    break;
                }
                default:
                    break;
            }
        }

        return validateViewObjectFactory(viewObjects);
    }

    private static ArrayList<ViewObject> validateViewObjectFactory(ArrayList<ViewObject> objects) {
        Iterator<ViewObject> it = objects.iterator();
        while (it.hasNext()) {
            ViewObject object = it.next();
            if (object == null || !object.isValid())
                it.remove();
        }

        return objects;
    }
}
