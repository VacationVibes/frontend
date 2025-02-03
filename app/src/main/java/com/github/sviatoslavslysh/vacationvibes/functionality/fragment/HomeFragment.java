package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;


import com.github.sviatoslavslysh.vacationvibes.MainActivity;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.cardstack.CardStackAdapter;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.model.AddReactionResponse;
import com.github.sviatoslavslysh.vacationvibes.model.HomeViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.Place;
import com.github.sviatoslavslysh.vacationvibes.model.PlaceImageMin;
import com.github.sviatoslavslysh.vacationvibes.repository.PlaceRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.LocationHelper;
import com.github.sviatoslavslysh.vacationvibes.utils.PlaceCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements CardStackListener {
    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private PlaceRepository placeRepository;
    private PreferencesManager preferencesManager;
    private HomeViewModel homeViewModel;
    private LocationHelper locationHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        placeRepository = ((NavigationBarActivity) requireActivity()).getPlaceRepository();
        preferencesManager = ((NavigationBarActivity) requireActivity()).getPreferencesManager();
        cardStackView = rootView.findViewById(R.id.card_stack_view);
        locationHelper = LocationHelper.getInstance(requireContext());
        adapter = new CardStackAdapter(null, locationHelper);

        if (homeViewModel.isDataLoaded()) {
            adapter = new CardStackAdapter(homeViewModel.getPlaces(), locationHelper);
            manager = new CardStackLayoutManager(requireContext(), HomeFragment.this);
            setupCardStackView();
        } else if (preferencesManager.isFirstOpen()) {
            startTutorial();
            preferencesManager.setFirstOpen(false);
            loadPlaces(true);
        }
        if (!homeViewModel.isDataLoaded() && !homeViewModel.isAwaitingResponse()) {
            // do nothing if already awaiting response on another (probably already hidden) activity
            loadPlaces(true);
        }
        return rootView;
    }

    private void setupCardStackView() {
//        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(6);
//        manager.setTranslationInterval(8.0f);
//        manager.setScaleInterval(0.95f);
//        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(45.0f);
        manager.setDirections(Direction.HORIZONTAL);  // will be overwritten anyway by onCardAppeared
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
//        manager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        if (cardStackView.getItemAnimator() instanceof DefaultItemAnimator) {
            DefaultItemAnimator animator = (DefaultItemAnimator) cardStackView.getItemAnimator();
            animator.setSupportsChangeAnimations(false);
        }
    }

    private void startTutorial() {
        List<PlaceImageMin> tutorialRightImage = new ArrayList<>();
        tutorialRightImage.add(new PlaceImageMin("https://i.imgur.com/VJA3dmI.png"));
        List<PlaceImageMin> tutorialLeftImage = new ArrayList<>();
        tutorialLeftImage.add(new PlaceImageMin("https://i.imgur.com/TGqOLSX.png"));

        adapter.addPlace(new Place("tutorialRight", "A quick tutorial", 0, 0, "", "Swipe to the right to like place", new ArrayList<>(), tutorialRightImage, new ArrayList<>()));
        adapter.addPlace(new Place("tutorialLeft", "A quick tutorial", 0, 0, "", "Swipe to the left to dislike place", new ArrayList<>(), tutorialLeftImage, new ArrayList<>()));

        homeViewModel.setPlaces(adapter.getPlaces());
    }

    private void loadPlaces(Boolean setupLayout) {
        List<String> placeIdsArray = new ArrayList<>();
        for (Place place : homeViewModel.getPlaces()) {
            if (!List.of("tutorialRight", "tutorialLeft").contains(place.getId())) {
                placeIdsArray.add(place.getId());
            }
        }

        homeViewModel.setAwaitingResponse(true);
        placeRepository.getFeed(placeIdsArray, new PlaceCallback<List<Place>>() {
            @Override
            public void onSuccess(List<Place> places) {
                homeViewModel.setAwaitingResponse(false);
                if (isAdded()) {
                    // todo delete this toast
//                    ToastManager.showToast(requireActivity(), "Retrieved places successfully!");
                    for (Place place : places) {
                        adapter.addPlace(place);
                        homeViewModel.addPlace(place);
                    }
                    if (setupLayout) {
                        manager = new CardStackLayoutManager(requireContext(), HomeFragment.this);
                        setupCardStackView();
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                homeViewModel.setAwaitingResponse(false);
                if (isAdded()) {
                    ToastManager.showToast(requireActivity(), "HomeFragment onError: " + errorMessage);
                }
            }

            @Override
            public void onInvalidAuth() {
                homeViewModel.setAwaitingResponse(false);
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

    @Override
    public void onCardAppeared(@Nullable View view, int i) {
        Place currentPlace = adapter.getPlaces().get(i);
        if (currentPlace.getId().equals("tutorialLeft")) {
            manager.setDirections(List.of(Direction.Left));
        } else if (currentPlace.getId().equals("tutorialRight")) {
            manager.setDirections(List.of(Direction.Right));
        } else {
            manager.setDirections(Direction.HORIZONTAL);
        }
        // todo modify homeViewModel
    }

    @Override
    public void onCardDragging(@Nullable Direction direction, float v) {
        System.out.println("onCardDragging " + direction + " " + v);
    }

    @Override
    public void onCardSwiped(@Nullable Direction direction) {
        List<Place> allPlaces = adapter.getPlaces();
        System.out.println("top position " + manager.getTopPosition());
        List<Place> cachePlaces = allPlaces.subList(manager.getTopPosition(), allPlaces.size());
        homeViewModel.setPlaces(cachePlaces);
        String reaction = "";
        assert direction != null;
        if (direction.equals(Direction.Left)) {
            // dislike
            reaction = "dislike";
        } else if (direction.equals(Direction.Right)) {
            // like
            reaction = "like";
        }
        Place currentPlace = adapter.getPlaces().get(manager.getTopPosition() - 1);
        if (currentPlace.getNote() != null) {  // ignoring tutorial cards
            if (currentPlace.getId().equals("tutorialRight") || currentPlace.getId().equals("tutorialLeft")) {
                return;
            }
        }
        placeRepository.addReaction(currentPlace.getId(), reaction, new PlaceCallback<AddReactionResponse>() {
            @Override
            public void onSuccess(AddReactionResponse result) {
                // reaction sent
            }

            @Override
            public void onError(String errorMessage) {
                // todo maybe add to queue of not sent request
                ToastManager.showToast(requireActivity(), errorMessage);
            }

            @Override
            public void onInvalidAuth() {
                ToastManager.showToast(requireActivity(), "Token expired");
                preferencesManager.removeToken();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    requireActivity().finish();
                }, 200);
            }
        });
    }

    @Override
    public void onCardRewound() {
        System.out.println("onCardRewound");
    }

    @Override
    public void onCardCanceled() {
        System.out.println("onCardCanceled");
    }

    @Override
    public void onCardDisappeared(@Nullable View view, int i) {
        Log.d("onCardDisappeared", "Places amount " + String.valueOf(homeViewModel.getPlacesAmount()));

        if (homeViewModel.getPlacesAmount() <= 8) {
            if (!homeViewModel.isAwaitingResponse()) {
                loadPlaces(false);
                Log.d("HomeFragment.onCardDisappeared", "Adding additional places");
            }
        }
    }
}
