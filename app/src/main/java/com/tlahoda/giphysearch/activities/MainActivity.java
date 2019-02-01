package com.tlahoda.giphysearch.activities;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.tlahoda.giphysearch.R;
import com.tlahoda.giphysearch.adapters.SearchResultsAdapter;
import com.tlahoda.giphysearch.databinding.ActivityMainBinding;
import com.tlahoda.giphysearch.factories.SingleArgumentViewModelFactory;
import com.tlahoda.giphysearch.fragments.GifDetailFragment;
import com.tlahoda.giphysearch.itemdecorations.SearchResultsItemDecoration;
import com.tlahoda.giphysearch.listeners.OnItemTouchListenerAdapter;
import com.tlahoda.giphysearch.listeners.SearchResultsScrollListener;
import com.tlahoda.giphysearch.loaders.ImageViewLoader;
import com.tlahoda.giphysearch.loaders.PicassoLoader;
import com.tlahoda.giphysearch.loaders.callbacks.GifLoaderCallback;
import com.tlahoda.giphysearch.models.GifObject;
import com.tlahoda.giphysearch.models.GiphySearchResponse;
import com.tlahoda.giphysearch.providers.DataProvider;
import com.tlahoda.giphysearch.providers.GiphyDataProvider;
import com.tlahoda.giphysearch.repositories.GifRepository;
import com.tlahoda.giphysearch.repositories.ImageRepository;
import com.tlahoda.giphysearch.viewmodels.GiphyViewCellViewModel;
import com.tlahoda.giphysearch.viewmodels.MainActivityViewModel;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String GIF_DETAIL_FRAGMENT_TAG = "GIF_DETAIL_FRAGMENT";
    private static final String LOAD_FAILED_MESSAGE_FORMAT = "Failed to get %s";

    private static final int DEBOUNCE_DELAY = 300;

    private ActivityMainBinding binding;

    private SearchResultsAdapter adapter;

    private CompositeDisposable disposables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        DataProvider<GiphySearchResponse> dataProvider = new GiphyDataProvider(response -> {
            MainActivity.this.adapter.notifyDataSetChanged();
            MainActivity.this.adapter.checkIfEmpty();

            MainActivity.this.binding.loading.setVisibility(View.GONE);

        }, t -> {
            MainActivity.this.adapter.checkIfEmpty();
            MainActivity.this.binding.loading.setVisibility(View.GONE);

            closeKeyboard();

            Snackbar.make(MainActivity.this.binding.getRoot(), t.getMessage(), Snackbar.LENGTH_LONG).show();
        });

        ImageRepository<GifObject> repository = new GifRepository<>(dataProvider);

        binding.setViewModel(ViewModelProviders.of(this, new SingleArgumentViewModelFactory<>(repository, ImageRepository.class)).get(MainActivityViewModel.class));

        //noinspection unchecked
        ImageViewLoader imageViewLoader = new PicassoLoader((GifLoaderCallback<GiphyViewCellViewModel>) viewModel -> new Handler(Looper.getMainLooper()).post(adapter::notifyDataSetChanged),

                (GifLoaderCallback<GiphyViewCellViewModel>) viewModel -> Snackbar.make(binding.getRoot(), String.format(LOAD_FAILED_MESSAGE_FORMAT, viewModel.getTitle()), Snackbar.LENGTH_LONG).show());

        adapter = new SearchResultsAdapter(repository, imageViewLoader, viewModel -> {
            MainActivity.this.closeKeyboard();

            MainActivity.this.addFragment(viewModel);

            MainActivity.this.resetActionBar(viewModel.getTitle(), true);

        }, empty -> {
            if (empty) {
                MainActivity.this.binding.emptyView.setVisibility(View.VISIBLE);
                MainActivity.this.binding.searchResults.setVisibility(View.GONE);

            } else {
                MainActivity.this.binding.emptyView.setVisibility(View.GONE);
                MainActivity.this.binding.searchResults.setVisibility(View.VISIBLE);
            }
        });

        binding.searchResults.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.searchResults.setAdapter(adapter);
        binding.searchResults.addItemDecoration(new SearchResultsItemDecoration());

        SearchResultsScrollListener scrollListener = new SearchResultsScrollListener(() -> repository.loadMore(MainActivity.this));

        binding.searchResults.addOnScrollListener(scrollListener);

        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                View view = MainActivity.this.binding.searchResults.findChildViewUnder(e.getX(), e.getY());

                if (view != null) {
                    view.callOnClick();
                }

                super.onLongPress(e);
            }
        });

        binding.searchResults.addOnItemTouchListener(new OnItemTouchListenerAdapter() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                gestureDetector.onTouchEvent(e);

                return super.onInterceptTouchEvent(rv, e);
            }
        });

        disposables = new CompositeDisposable();

        disposables.add(RxTextView
                .editorActions(binding.searchField)
                .subscribe(actionId -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        MainActivity.this.closeKeyboard();
                    }
                }));

        disposables.add(RxTextView
                .textChanges(binding.searchField)
                .debounce(DEBOUNCE_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    repository.cancel();

                    scrollListener.reset();

                    MainActivity.this.adapter.clear();

                    if (value.length() == 0) {
                        MainActivity.this.binding.loading.setVisibility(View.GONE);
                        MainActivity.this.binding.emptyView.setVisibility(View.VISIBLE);

                    } else {
                        MainActivity.this.binding.loading.setVisibility(View.VISIBLE);
                        MainActivity.this.binding.emptyView.setVisibility(View.GONE);
                    }

                    repository.load(MainActivity.this, value.toString());
                }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                removeFragment();

                resetActionBar(getResources().getString(R.string.app_name), false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addFragment(GiphyViewCellViewModel viewModel) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        GifDetailFragment gifDetailFragment = GifDetailFragment.newInstance(viewModel.getGifObject());
        fragmentTransaction.replace(R.id.gif_detail_container, gifDetailFragment, GIF_DETAIL_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    private void removeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(GIF_DETAIL_FRAGMENT_TAG);

        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }

        fragmentTransaction.commit();
    }

    private void resetActionBar(String title, boolean upEnabled) {
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();

            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(upEnabled);
            actionBar.setHomeButtonEnabled(upEnabled);
        }
    }

    @Override
    public void onDestroy() {
        if (disposables != null) {
            disposables.dispose();
        }

        binding.executePendingBindings();

        super.onDestroy();
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        }
    }
}