package by.onliner.news.factory.comment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.onliner.news.App;
import by.onliner.news.factory.IFactoryViewObjects;
import by.onliner.news.R;
import by.onliner.news.structures.comments.CommentQuote;

/**
 * Формирование цитат в комментариях
 */
public class CommentQuoteFactory implements IFactoryViewObjects<CommentQuote, View> {
    @Override
    public View create(CommentQuote quote) {
        View view = LayoutInflater.from(App.getContext()).inflate(R.layout.layout_comment_quote, null);
        CardView cardView = (CardView)view.findViewById(R.id.cv_comment_quote);

        View baseView = newQuote(quote.getText(), quote.getAuthor());
        cardView.addView(baseView, 0);

        quote = quote.getQuote();
        while (quote != null) {
            View childView = newQuote(quote.getText(), quote.getAuthor());
            ((ViewGroup)baseView).addView(childView, 0);
            baseView = childView;
            quote = quote.getQuote();
        }

        return view;
    }

    private ViewGroup newQuote(String text, String author) {
        LinearLayout layout = new LinearLayout(App.getContext());
        layout.setPadding(20, 0, 16, 16);
        layout.setBackgroundResource(R.drawable.test_borders);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView textViewComment = new TextView(App.getContext());
        textViewComment.setTextColor(ResourcesCompat.getColor(App.getContext().getResources(), R.color.colorOnlinerBlockquoteText, null));
        textViewComment.setTextSize(13);
        textViewComment.setPadding(0, 10, 0, 0);

        textViewComment.setText(text);

        LinearLayout.LayoutParams textViewParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textViewComment.setLayoutParams(textViewParams1);
        layout.addView(textViewComment);

        TextView textViewAuthor = new TextView(App.getContext());
        textViewAuthor.setPadding(15, 0, 0, 0);
        textViewAuthor.setTextColor(Color.BLACK);
        textViewAuthor.setTypeface(null, Typeface.BOLD);

        textViewAuthor.setText(author);

        LinearLayout.LayoutParams textViewParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textViewAuthor.setLayoutParams(textViewParams2);
        layout.addView(textViewAuthor);

        return layout;
    }
}
