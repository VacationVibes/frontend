package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.github.sviatoslavslysh.vacationvibes.MainActivity;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.model.HistoryViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.Place;
import com.github.sviatoslavslysh.vacationvibes.repository.PlaceRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.HistoryAdapter;
import com.github.sviatoslavslysh.vacationvibes.utils.LocationHelper;
import com.github.sviatoslavslysh.vacationvibes.utils.PlaceCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private PreferencesManager preferencesManager;
    private PlaceRepository placeRepository;
    private HistoryViewModel historyViewModel;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationHelper locationHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        preferencesManager = new PreferencesManager(requireContext());
        placeRepository = ((NavigationBarActivity) requireActivity()).getPlaceRepository();
        historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        locationHelper = LocationHelper.getInstance(requireContext());
        adapter = new HistoryAdapter(new ArrayList<Place>(), locationHelper);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        if (historyViewModel.getPlaces().isEmpty()) {
            loadHistory();
        } else {
            for (Place place : historyViewModel.getPlaces()) {
                adapter.addPlace(place);
            }
        }

        registerScrollListener();
        registerRefreshListener();

        return view;
    }

    public void loadHistory() {
        if (historyViewModel.isAwaitingResponse()) {
            return;
        }
        long startTime = System.nanoTime();
        Log.d("loadHistory start", "loadHistory startTime " + startTime);
        historyViewModel.setAwaitingResponse(true);
        placeRepository.getReactions(historyViewModel.getOffset(), historyViewModel.getLimit(), new PlaceCallback<List<Place>>() {
            @Override
            public void onSuccess(List<Place> places) {
                historyViewModel.setAwaitingResponse(false);
                if (isAdded()) {
                    long endTime = System.nanoTime();
                    Log.d("loadHistory end", "loadHistory endTime " + endTime + " (time taken " + (endTime - startTime) + "ms)");
                    ToastManager.showToast(requireActivity(), (endTime - startTime) + "ms");
                    for (Place place : places) {
                        adapter.addPlace(place);
                        historyViewModel.addPlace(place);
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                historyViewModel.setAwaitingResponse(false);
                if (isAdded()) {
                    long endTime = System.nanoTime();
                    Log.d("loadHistory end", "loadHistory endTime " + endTime + " (time taken " + (endTime - startTime) + "ms)");
                    ToastManager.showToast(requireActivity(), errorMessage);
                }
            }

            @Override
            public void onInvalidAuth() {
                historyViewModel.setAwaitingResponse(false);
                if (isAdded()) {
                    ToastManager.showToast(requireActivity(), "Token expired");
                    preferencesManager.removeToken();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        requireActivity().finish();
                    }, 200);
                }
            }
        });
    }

    public void registerScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1 &&
                        adapter.getItemCount() % 20 == 0) {
                    loadHistory();
                }
            }
        });
    }

    public void registerRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            historyViewModel.setAwaitingResponse(false);
            if (isAdded()) {
                if (!historyViewModel.isAwaitingResponse()) {
                    adapter.clean();
                    historyViewModel.clean();
                    loadHistory();
                } else {
                    ToastManager.showToast(requireActivity(), "Not so fast!");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
