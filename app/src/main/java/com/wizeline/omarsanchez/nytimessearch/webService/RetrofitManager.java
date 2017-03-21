package com.wizeline.omarsanchez.nytimessearch.webService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.wizeline.omarsanchez.nytimessearch.R;
import com.wizeline.omarsanchez.nytimessearch.interfaces.DocsRequest;
import com.wizeline.omarsanchez.nytimessearch.interfaces.ResultRequest;
import com.wizeline.omarsanchez.nytimessearch.interfaces.RetrofitInterface;
import com.wizeline.omarsanchez.nytimessearch.models.Filter;
import com.wizeline.omarsanchez.nytimessearch.models.SearchResults;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.wizeline.omarsanchez.nytimessearch.webService.Config.API_KEY;
import static com.wizeline.omarsanchez.nytimessearch.webService.Config.API_URL;

/**
 * Created by omarsanchez on 3/16/17.
 */

public class RetrofitManager implements DocsRequest {

    private RetrofitInterface retrofitInterface;

    public RetrofitManager() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        retrofitInterface = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(RetrofitInterface.class);
    }


    @Override
    public void getArticles(Context context, String search, String page, final ResultRequest result, Filter filter) {
        if (isNetworkAvailable(context)) {
            if (search.isEmpty()) {
                search = null;
            }

            retrofitInterface.searchDocs(API_KEY, search, page, getDate(filter), getSortName(filter), getDesk(filter)).enqueue(new Callback<SearchResults>() {
                @Override
                public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                    if(response.body()!=null)  {
                        result.onRecive(response.body().getResponse().getDocs());
                    }else {
                        Toast.makeText(context,R.string.noResults,Toast.LENGTH_SHORT).show();
                        result.onFailed(new NullPointerException());
                    }
                }

                @Override
                public void onFailure(Call<SearchResults> call, Throwable t) {
                    result.onFailed(t);
                }
            });

        } else {
            Toast.makeText(context, context.getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getNewPage(Context context, String search, String page, final ResultRequest resultRequest, Filter filter) {
        if (isNetworkAvailable(context)) {
            if (search.isEmpty()) {
                search = null;
            }
            retrofitInterface.searchDocs(API_KEY, search, page, getDate(filter), getSortName(filter), getDesk(filter)).enqueue(new Callback<SearchResults>() {
                @Override
                public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                    if(response.body()!=null) {
                        resultRequest.onNewPageRecive(response.body().getResponse().getDocs());
                    }else {
                        Toast.makeText(context,R.string.noResults,Toast.LENGTH_SHORT).show();
                        resultRequest.onFailed(new NullPointerException());
                    }
                }

                @Override
                public void onFailure(Call<SearchResults> call, Throwable t) {
                    resultRequest.onFailed(t);
                }
            });

        } else {
            Toast.makeText(context, context.getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }

    }

    private String getDate(Filter filter) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat queryFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Date d;
        try {
            d = dateFormatter.parse(filter.getDate());
            return queryFormatter.format(d);
        } catch (ParseException e) {
            return "19200421";
        }

    }

    private String getSortName(Filter filter) {
        if (filter.getSort() == 0) {
            return "oldest";
        } else {
            return "newest";
        }
    }

    private String getDesk(Filter filter) {
        ArrayList<String> temp = new ArrayList<String>();
        if (filter.isArts()) {
            temp.add("Arts");
        }
        if (filter.isFashion()) {
            temp.add("Fashion & Style");
        }
        if (filter.isSports()) {
            temp.add("Sports");
        }
        if (temp.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("news_desk:(");
        for (String string : temp) {
            stringBuilder.append("\"" + string + "\" ");
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
