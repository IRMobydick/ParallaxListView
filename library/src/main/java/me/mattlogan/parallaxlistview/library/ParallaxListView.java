package me.mattlogan.parallaxlistview.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

public class ParallaxListView extends FrameLayout {

    private ScrollView mScrollView;
    private LinearLayout mBackgroundLayout;
    private ImageView mBackgroundHeader;
    private int mHeaderHeight;
    private View mBackgroundLowerView;

    private ListView mListView;
    private View mTransparentHeader;

    private BaseAdapter adapter;

    public ParallaxListView(Context context) {
        super(context);
        init(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mHeaderHeight = (int) (context.getResources().getDisplayMetrics().heightPixels * 2 / 3f);

        mScrollView = new ScrollView(context);
        mScrollView.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mScrollView);

        mBackgroundLayout = new LinearLayout(context);
        mBackgroundLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBackgroundLayout.setOrientation(LinearLayout.VERTICAL);
        mScrollView.addView(mBackgroundLayout);

        mBackgroundHeader = new ImageView(context);
        mBackgroundHeader.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mHeaderHeight));
        mBackgroundLayout.addView(mBackgroundHeader);

        mBackgroundLowerView = new View(context);
        mBackgroundLowerView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mHeaderHeight));
        mBackgroundLayout.addView(mBackgroundLowerView);

        mListView = new ListView(context);
        mListView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override public void onScroll(AbsListView view, int firstVisibleItem,
                                            int visibleItemCount, int totalItemCount) {

                View firstChild = view.getChildAt(0);
                if (firstChild != null && firstChild == mTransparentHeader) {
                    int scrollY = -view.getChildAt(0).getTop();
                    Log.d("testing", "scrolly: " + scrollY);
                    mScrollView.scrollTo(0, (int) (scrollY / 2f));
                }
            }
        });
        addView(mListView);

        mTransparentHeader = new View(context);
        mTransparentHeader.setBackgroundColor(Color.TRANSPARENT);
        mTransparentHeader.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, mHeaderHeight));
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        mListView.setAdapter(new ActualAdapter());
    }

    public void setBackgroundHeaderDrawable(Drawable drawable) {
        mBackgroundHeader.setImageDrawable(drawable);
    }

    private class ActualAdapter extends BaseAdapter {

        @Override public int getCount() {
            return adapter.getCount() + 1;
        }

        @Override public Object getItem(int pos) {
            return pos == 0 ? null : adapter.getItem(pos - 1);
        }

        @Override public long getItemId(int pos) {
            return pos == 0 ? 0 : adapter.getItemId(pos - 1);
        }

        @Override public View getView(int pos, View view, ViewGroup viewGroup) {
            if (pos == 0) {
                return mTransparentHeader;
            }

            if (view == mTransparentHeader) {
                view = null;
            }

            return adapter.getView(pos - 1, view, viewGroup);
        }
    }
}