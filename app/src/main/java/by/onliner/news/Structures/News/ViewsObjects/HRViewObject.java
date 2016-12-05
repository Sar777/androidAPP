package by.onliner.news.Structures.News.ViewsObjects;

import android.os.Parcel;

import by.onliner.news.Enums.ViewNewsType;

/**
 * Created by orion on 15.11.16.
 */
public class HRViewObject extends ViewObject {
    public HRViewObject(Parcel in) {
        super(ViewNewsType.TYPE_VIEW_HR);
    }

    public HRViewObject() {
        super(ViewNewsType.TYPE_VIEW_HR);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
