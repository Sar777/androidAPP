package by.onliner.news.factory.news;

import org.jsoup.nodes.Element;

import by.onliner.news.factory.IFactoryViewObjects;
import by.onliner.news.structures.news.viewsObjects.HRViewObject;
import by.onliner.news.structures.news.viewsObjects.ViewObject;

/**
 * Тег <hr> </hr>
 */
public class HRFactory implements IFactoryViewObjects<Element, ViewObject> {
    @Override
    public ViewObject create(Element element) {
        return new HRViewObject();
    }
}
