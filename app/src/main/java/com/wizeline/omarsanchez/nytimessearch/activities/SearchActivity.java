package com.wizeline.omarsanchez.nytimessearch.activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.wizeline.omarsanchez.nytimessearch.R;
import com.wizeline.omarsanchez.nytimessearch.adapters.DocsAdapter;
import com.wizeline.omarsanchez.nytimessearch.databinding.ActivitySearchBinding;
import com.wizeline.omarsanchez.nytimessearch.fragments.FilterDialog;
import com.wizeline.omarsanchez.nytimessearch.interfaces.FilterSaved;
import com.wizeline.omarsanchez.nytimessearch.interfaces.ResultRequest;
import com.wizeline.omarsanchez.nytimessearch.interfaces.ScrollInterfece;
import com.wizeline.omarsanchez.nytimessearch.models.Docs;
import com.wizeline.omarsanchez.nytimessearch.models.Filter;
import com.wizeline.omarsanchez.nytimessearch.utils.InfiniteScroll;
import com.wizeline.omarsanchez.nytimessearch.webService.RetrofitManager;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ResultRequest, ScrollInterfece, FilterSaved {
    private static final String FILTER_SAVED = "objectFilter";
    private static final String MY_PREFERENCES_FILTER = "filterPreferences";
    private ActivitySearchBinding binding;
    private RetrofitManager cliente;
    DocsAdapter docsAdapter;
    AlertDialog filterDialog;
    SharedPreferences sharedPreferences;
    Filter filter;
    InfiniteScroll infiniteScroll;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES_FILTER, MODE_PRIVATE);
        updateFilter();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.swipeRefresh.setOnRefreshListener(this);
        binding.RecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.columms), StaggeredGridLayoutManager.VERTICAL);
        binding.RecyclerView.setLayoutManager(manager);
        docsAdapter = new DocsAdapter(new ArrayList<Docs>());
        binding.RecyclerView.setAdapter(docsAdapter);
        infiniteScroll = new InfiniteScroll(manager, 10, getResources().getInteger(R.integer.columms), this);
        binding.RecyclerView.addOnScrollListener(infiniteScroll);
        getCliente().getArticles(this, "", "0", this, filter);
        search = "";
    }

    private void updateFilter() {
        String tempObject = sharedPreferences.getString(FILTER_SAVED, null);
        if (tempObject != null) {
            filter = new Gson().fromJson(tempObject, Filter.class);
        } else {
            filter = getDefaultFilter();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getCliente().getArticles(SearchActivity.this, query, "0", SearchActivity.this, filter);
                search = query;
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                createAlertDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getCliente().getArticles(this, "", "0", this, filter);
        search = "";
    }

    public RetrofitManager getCliente() {
        if (cliente == null) {
            cliente = new RetrofitManager();
        }
        return cliente;
    }


    @Override
    public void onRecive(List<Docs> docs) {
        binding.swipeRefresh.setRefreshing(false);
        docsAdapter.newData(docs);
    }

    @Override
    public void onNewPageRecive(List<Docs> docs) {
        binding.swipeRefresh.setRefreshing(false);
        infiniteScroll.LoadFinished();
        docsAdapter.addData(docs);
    }

    @Override
    public void onFailed(Throwable t) {
        binding.swipeRefresh.setRefreshing(false);
        infiniteScroll.LoadFinished();
        t.printStackTrace();
    }

    private void createAlertDialog() {
        updateFilter();
        filterDialog = new FilterDialog(this, this, filter).create();
        filterDialog.show();
    }

    @Override
    public void onFilterSaved(Filter filter) {
        filterSavePreferences(filter);
        filterDialog.dismiss();

    }

    private void filterSavePreferences(Filter filter) {
        sharedPreferences.edit().putString(FILTER_SAVED, new Gson().toJson(filter)).apply();
        updateFilter();
    }

    public Filter getDefaultFilter() {
        return new Filter("21-04-1920", 0, true, true, true);
    }

    @Override
    public void OnFinal(int page) {
        binding.swipeRefresh.setRefreshing(true);
        infiniteScroll.loadStarted();
        getCliente().getNewPage(this, search, String.valueOf(page), this, filter);
    }

}
