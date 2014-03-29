package com.example.chevbook.CustomsView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/*

EXAMPLE USAGE:

listView = (ScrollListView) findViewById(R.id.list);
listView.setOnBottomReachedListener(
	new ScrollListView.OnBottomReachedListener() {

		@Override
		public void onBottomReached() {
			requestPage();
		}
	}
);
*/


public class ScrollListView extends ListView implements OnScrollListener
{
    OnBottomReachedListener onBottomReachedListener;

    public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        defineScrolling();
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defineScrolling();
    }

    public ScrollListView(Context context) {
        super(context);
        defineScrolling();
    }

    private void defineScrolling()
    {
        this.setOnScrollListener(this);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        ScrollListView scrollListView = (ScrollListView) view;
        int position = firstVisibleItem+visibleItemCount;

        // Check if bottom has been reached
        if( position == totalItemCount && totalItemCount>0)
        {
            // validate if we have a listener
            if( scrollListView.onBottomReachedListener != null )
            {
                // trigger the listener
                scrollListView.onBottomReachedListener.onBottomReached();
            }
        }
    }

    public OnBottomReachedListener getOnBottomReachedListener() {
        return onBottomReachedListener;
    }

    public void setOnBottomReachedListener(
            OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public interface OnBottomReachedListener
    {
        public void onBottomReached();
    }
}
