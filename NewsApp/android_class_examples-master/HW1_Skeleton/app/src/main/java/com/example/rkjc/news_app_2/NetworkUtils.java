package com.example.rkjc.news_app_2;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils
{
    final static String NEWS_BASE_URL = "https://newsapi.org/v1/articles";
    final static String PARAM_SOURCE = "source";
    final static String PARAM_QUERY_SOURCE = "the-next-web";
    final static String PARAM_SORTBY = "sortBy";
    final static String PARAM_QUERY_SORTBY = "latest";
    final static String PARAM_APIKEY = "apiKey";
    final static String PARAM_QUERY_APIKEY = "a9f6a1e8f38448bd8a8c0dbdb4b784a9";

    public static URL buildUrl()
    {
        Uri builtUri = Uri.parse(NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SOURCE, PARAM_QUERY_SOURCE)
                    .appendQueryParameter(PARAM_SORTBY, PARAM_QUERY_SORTBY)
                        .appendQueryParameter(PARAM_APIKEY, PARAM_QUERY_APIKEY)
                            .build();

        URL url = null;
        try
        {
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
            {
                return scanner.next();
            }
            else
            {
                return null;
            }

        }
        finally
        {
            urlConnection.disconnect();
        }
    }
}