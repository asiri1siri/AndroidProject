package com.example.rkjc.news_app_2;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    private static final String TAG = "MainActivity";
    private String newsSearchResults;
    private static final int LOADER_ID = 1;

    private Toolbar toolBar;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private ArrayList<NewsItem> newsItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateLoader(0 , savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.news_recyclerview);
        mAdapter = new NewsAdapter(this, newsItems);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progressCircle);

        mTextView = (TextView) findViewById(R.id.queryJSON);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.get_news)
        {
            Bundle bundle = new Bundle();
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> newsSearchLoader = loaderManager.getLoader(LOADER_ID);

            if(newsSearchLoader == null)
            {
                loaderManager.initLoader(LOADER_ID, bundle, this).forceLoad();
            }
            else
            {
                loaderManager.restartLoader(LOADER_ID, bundle, this).forceLoad();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private URL searchUrl()
    {
        URL newsSearchURL = NetworkUtils.buildUrl();
        return newsSearchURL;
    }

    private void populateNewsData(String data)
    {
        mTextView.setText(data);
    }

    private void populateRecyclerView(String searchResults)
    {
        newsItems = JsonUtils.parseNews(searchResults);
        mAdapter.mNewsItems.addAll(newsItems);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args)
    {
        Log.d(TAG, NetworkUtils.buildUrl() + "\n\n\n\n\n");
        return new AsyncTaskLoader<String>(this)
        {
            @Override
            protected void onStartLoading()
            {
                super.onStartLoading();
                if(args == null)
                {
                    return;
                }
                mTextView.setText("");
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public String loadInBackground()
            { ;

                try
                {
                    newsSearchResults = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                return newsSearchResults;
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data)
    {
        Log.d("Logging exit codes on console", data);
        mProgressBar.setVisibility(View.GONE);
        populateNewsData(data);
        populateRecyclerView(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader)
    {

    }
}